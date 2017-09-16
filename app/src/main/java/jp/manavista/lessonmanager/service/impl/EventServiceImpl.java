package jp.manavista.lessonmanager.service.impl;

import android.util.SparseArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.constants.DateLabel;
import jp.manavista.lessonmanager.model.entity.Event;
import jp.manavista.lessonmanager.model.entity.Event_Selector;
import jp.manavista.lessonmanager.model.vo.EventVo;
import jp.manavista.lessonmanager.repository.EventRepository;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.week.WeekViewEvent;

/**
 *
 * Member service implement
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<Event> getById(long id) {
        return repository.getRelation()
                .idEq(id)
                .getAsSingle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<WeekViewEvent> getEventListAll() {
        return repository.getSelector()
                .executeAsObservable()
                .map(new Function<Event, EventVo>() {
                    @Override
                    public EventVo apply(@NonNull Event event) throws Exception {
                        return EventVo.newInstance(event);
                    }
                })
                .map(new Function<EventVo, WeekViewEvent>() {
                    @Override
                    public WeekViewEvent apply(@NonNull EventVo vo) throws Exception {

                        final WeekViewEvent event = new WeekViewEvent(
                                vo.getId(),
                                vo.getName(),
                                StringUtils.EMPTY,
                                vo.getStartDateCalendar(),
                                vo.getEndDateCalendar(), true
                        );
                        event.setColor(vo.getBackgroundColor());
                        return event;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<EventVo> getVoListByCriteria(final boolean containPast, final SparseArray<String> labelMap) {

        /*
         * Natural Display Date
         *
         * today: "Today"
         * yesterday: "Yesterday"
         * tomorrow: "Tomorrow"
         * Within the Year: "MM/DD"
         * other: "YYYY/MM/DD"
         */
        final Date today = DateTimeUtil.today().getTime();
        final Date yesterday = DateUtils.addDays(today, -1);
        final Date tomorrow = DateUtils.addDays(today, 1);

        Event_Selector selector = repository.getSelector();

        if( !containPast ) {
            selector = selector.dateGe(DateTimeUtil.DATE_FORMAT_YYYYMMDD.format(today));
        }

        return selector
                .orderByDateAsc()
                .executeAsObservable()
                .map(new Function<Event, EventVo>() {
                    @Override
                    public EventVo apply(@NonNull Event event) throws Exception {

                        EventVo vo = EventVo.newInstance(event);
                        Date date = DateTimeUtil.DATE_FORMAT_YYYYMMDD.parse(vo.getDate());
                        if( DateUtils.isSameDay(date, today) ) {
                            vo.setDisplayDate(labelMap.get(DateLabel.TODAY.code()));
                        } else if( DateUtils.isSameDay(date, yesterday) ) {
                            vo.setDisplayDate(labelMap.get(DateLabel.YESTERDAY.code()));
                        } else if( DateUtils.isSameDay(date, tomorrow) ) {
                            vo.setDisplayDate(labelMap.get(DateLabel.TOMORROW.code()));
                        } else if( DateUtils.truncatedEquals(date, today, Calendar.YEAR) ) {
                            vo.setDisplayDate(DateTimeUtil.DATE_FORMAT_MMDD.format(date));
                        }
                        return vo;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Event> save(Event entity) {
        return repository.getRelation()
                .upsertAsSingle(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteById(long id) {
        return repository.getDeleter()
                .idEq(id)
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteAll() {
        return repository.getDeleter()
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
