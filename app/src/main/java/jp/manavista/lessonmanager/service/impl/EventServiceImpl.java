package jp.manavista.lessonmanager.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.dto.EventDto;
import jp.manavista.lessonmanager.model.entity.Event;
import jp.manavista.lessonmanager.model.vo.EventVo;
import jp.manavista.lessonmanager.repository.EventRepository;
import jp.manavista.lessonmanager.service.EventService;

/**
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
        return null;
    }

    @Override
    public Observable<EventVo> getVoListAll() {
        return repository.getRelation()
                .selector()
                .executeAsObservable()
                .map(new Function<Event, EventVo>() {
                    @Override
                    public EventVo apply(@NonNull Event event) throws Exception {
                        return EventVo.newInstance(event);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Event> save(EventDto dto) {
        return repository.getRelation()
                .upsertAsSingle(dto.toEntity())
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
