/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.MemberLessonActivity;
import jp.manavista.lessonmanager.facade.MemberLessonFacade;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.MemberLessonDto;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.util.ArrayUtil;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.image.CircleImageView;

import static jp.manavista.lessonmanager.util.DateTimeUtil.DATE_PATTERN_YYYYMMDD;

/**
 *
 * Member Lesson Fragment
 *
 * <p>
 * Overview:<br>
 * {@code MemberLesson} control fragment.<br>
 * Handling of {@code MemberLesson} insert and update (database control) is defined in this class.
 * </p>
 */
public final class MemberLessonFragment extends Fragment implements Validator.ValidationListener {

    /** Logger tag string */
    public static final String TAG = MemberLessonFragment.class.getSimpleName();
    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";
    /** bundle key: member lesson id */
    public static final String KEY_MEMBER_LESSON_ID = "MEMBER_LESSON_ID";

    /** member id */
    private long memberId;
    /** member lesson id */
    private long id;
    /** DTO */
    private MemberLessonDto dto;
    /** Activity Contents */
    private Activity contents;

    @Inject
    MemberService memberService;
    @Inject
    MemberLessonService memberLessonService;
    @Inject
    TimetableService timetableService;
    @Inject
    SharedPreferences preferences;
    @Inject
    MemberLessonFacade facade;

    /** input validator */
    private Validator validator;
    /** Member disposable */
    private Disposable disposable;

    /** Constructor */
    public MemberLessonFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param memberId display member id
     * @return A new instance of fragment MemberLessonFragment.
     */
    public static MemberLessonFragment newInstance(final long memberId, final long memberLessonId) {
        final MemberLessonFragment fragment = new MemberLessonFragment();
        final Bundle args = new Bundle();
        args.putLong(KEY_MEMBER_ID, memberId);
        args.putLong(KEY_MEMBER_LESSON_ID, memberLessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            memberId = args.getLong(KEY_MEMBER_ID);
            id = args.getLong(KEY_MEMBER_LESSON_ID);
        }

        this.disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_member_lesson, container, false);

        dto = MemberLessonDto.builder()
                .id(id)
                .memberId(memberId)
                .memberName((TextView) rootView.findViewById(R.id.member_name))
                .photo((CircleImageView) rootView.findViewById(R.id.member_icon_image))
                .name((EditText) rootView.findViewById(R.id.name))
                .abbr((EditText) rootView.findViewById(R.id.abbr))
                .type((EditText) rootView.findViewById(R.id.type))
                .location((EditText) rootView.findViewById(R.id.location))
                .presenter((EditText) rootView.findViewById(R.id.presenter))
                .startTimeText((EditText) rootView.findViewById(R.id.start_time))
                .endTimeText((EditText) rootView.findViewById(R.id.end_time))
                .timetableIcon((ImageButton) rootView.findViewById(R.id.timetable_image_button))
                .dayOfWeek((EditText) rootView.findViewById(R.id.day_of_week))
                .startPeriod((EditText) rootView.findViewById(R.id.start_period))
                .endPeriod((EditText) rootView.findViewById(R.id.end_period))
                .previewText((TextView) rootView.findViewById(R.id.preview_text))
                .textColorImageButton((ImageButton) rootView.findViewById(R.id.text_color_image_button))
                .backgroundColorImageButton((ImageButton) rootView.findViewById(R.id.background_color_image_button))

                .dateFormat("%04d/%02d/%02d")

                .build();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        this.contents = getActivity();

        DependencyInjector.appComponent().inject(this);

        validator = new Validator(dto);
        validator.setValidationListener(this);

        prepareButtonListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        if( id > 0 ) {
            storeEntityToDto(id);
        } else {
            storeDefaultValueToDto();
        }

        if( memberId > 0 ) {
            storeMemberToDto();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.disposable.dispose();
    }

    @Override
    public void onValidationSucceeded() {

        if( !validatePeriod() ) {
            return;
        }

        facade.isEmptySchedule(id).subscribe(isEmpty -> {
            Log.d(TAG, "schedule is empty: " + isEmpty);

            if( !isEmpty ) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(contents);
                builder.setTitle(R.string.title_member_lesson_dialog_schedule_exists)
                        .setIcon(R.drawable.ic_info_black)
                        .setMessage(R.string.message_member_lesson_schedule_exists)
                        .setPositiveButton(R.string.label_member_lesson_dialog_schedule_exist_save_lesson_only, onSaveLessonOnlyListener)
                        .setNegativeButton(R.string.label_member_lesson_dialog_schedule_exist_force_add, onForceAddListener)
                        .setNeutralButton(android.R.string.cancel, null)
                        .show();
            } else {
                executeSave(true);
            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for( ValidationError error : errors ) {

            final String message = error.getCollatedErrorMessage(contents);
            final View view = error.getView();

            if( view instanceof EditText ) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(contents, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * Save Member Lesson
     *
     * <p>
     * Overview:<br>
     * Save the information on the member lesson you entered on the screen.<br>
     * First, validate the input value. Perform processing with another
     * function according to the result.
     * </p>
     */
    public void save() {
        validator.validate();
    }

    private final DialogInterface.OnClickListener onSaveLessonOnlyListener = (dialogInterface, i) -> executeSave(false);

    private final DialogInterface.OnClickListener onForceAddListener = (dialogInterface, i) -> executeSave(true);

    private void executeSave(boolean addSchedule) {
        facade.save(dto.getMemberId(), dto.toEntity(), addSchedule).subscribe(rows -> {
            final Intent intent = new Intent();
            intent.putExtra(MemberLessonActivity.Extra.LESSON_NAME, dto.getName().getText().toString());
            contents.setResult(Activity.RESULT_OK, intent);
            contents.finish();
        });
    }

    /**
     *
     * Prepare ImageButton Listener
     *
     * <p>
     * Overview:<br>
     * Define and set the event when ImageButton is clicked.
     * </p>
     */
    private void prepareButtonListener() {

        dto.getStartTimeText().setOnClickListener(view -> {
            final EditText startTime = (EditText) view;
            final String[] times = String.valueOf(startTime.getText()).split(DateTimeUtil.COLON);
            new TimePickerDialog(contents, dto.startTimeSetListener,
                    Integer.valueOf(times[0]), Integer.valueOf(times[1]), false).show();
        });

        dto.getEndTimeText().setOnClickListener(view -> {
            final EditText endTime = (EditText) view;
            final String[] times = String.valueOf(endTime.getText()).split(DateTimeUtil.COLON);
            new TimePickerDialog(contents, dto.endTimeSetListener,
                    Integer.valueOf(times[0]), Integer.valueOf(times[1]), false).show();
        });

        dto.getTimetableIcon().setOnClickListener(view -> {

            final List<String> labelList = new ArrayList<>();
            final List<TimetableDto> timetableList = new ArrayList<>();
            final StringBuilder sb = new StringBuilder();

            disposable = timetableService.getListAll().subscribe(timetable -> {
                TimetableDto dto = TimetableDto.copy(timetable);
                timetableList.add(dto);
                sb.setLength(0);
                sb.append(dto.getStartTimeFormatted())
                        .append(" - ")
                        .append(dto.getEndTimeFormatted());
                labelList.add(sb.toString());
            }, throwable -> {
                throw new RuntimeException("can not get timetable", throwable);
            }, () -> {
                dto.setTimetableDtoList(timetableList);
                AlertDialog.Builder builder = new AlertDialog.Builder(contents);
                builder.setTitle(R.string.title_member_lesson_dialog_timetable)
                        .setIcon(R.drawable.ic_event_black)
                        .setItems(labelList.toArray(new CharSequence[0]), dto.timetableSetLister)
                        .show();
            });
        });

        dto.getDayOfWeek().setOnClickListener(view -> {

            /* full name: Sunday, Monday, Tuesday... */
            final String[] daysFull = getResources().getStringArray(R.array.entries_day_of_week_full_name);
            /* short name: Sun, Mon, Tue, Wed... */
            final String[] days = getResources().getStringArray(R.array.entries_day_of_week);
            /* day decimal string value: 1, 2, 3... */
            final String[] dayValues = getResources().getStringArray(R.array.entry_values_day_of_week);

            final String dayOfWeek = dto.getDayOfWeekValue();
            final boolean[] index = ArrayUtil.convertIndexFromArray(dayOfWeek, dayValues, ",");

            AlertDialog.Builder builder = new AlertDialog.Builder(contents);
            builder.setTitle(R.string.label_member_lesson_day_of_week_dialog_title)
                    .setIcon(R.drawable.ic_event_black)
                    .setMultiChoiceItems(daysFull, index, (dialogInterface, which, isChecked) -> {
                        index[which] = isChecked;
                        ((AlertDialog) dialogInterface)
                                .getButton(AlertDialog.BUTTON_POSITIVE)
                                .setEnabled(ArrayUtils.contains(index, true));
                    })
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        final String label = ArrayUtil.concatIndexOfArray(days, index, ", ");
                        final String dayOfWeekValue = ArrayUtil.concatIndexOfArray(dayValues, index, ",");
                        dto.getDayOfWeek().setText(label);
                        dto.setDayOfWeekValue(dayOfWeekValue);
                    })
                    .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {

                    })
                    .show();
        });

        dto.getStartPeriod().setOnClickListener(view -> {
            final EditText startPeriod = (EditText) view;
            final String[] dates = String.valueOf(startPeriod.getText()).split(DateTimeUtil.SLASH);
            final int month = Integer.valueOf(dates[1]) - 1;
            new DatePickerDialog(contents, dto.startPeriodSetListener,
                    Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
        });

        dto.getEndPeriod().setOnClickListener(view -> {
            final EditText endPeriod = (EditText) view;
            final String[] dates = String.valueOf(endPeriod.getText()).split(DateTimeUtil.SLASH);
            final int month = Integer.valueOf(dates[1]) - 1;
            new DatePickerDialog(contents, dto.endPeriodSetListener,
                    Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
        });

        dto.getTextColorImageButton().setOnClickListener(view -> {

            final ImageButton textColorButton = (ImageButton) view;
            final int textColor = textColorButton.getTag(R.string.tag_member_lesson_preview_color) == null
                    ? ContextCompat.getColor(contents, R.color.black)
                    : (int) textColorButton.getTag(R.string.tag_member_lesson_preview_color);

            new SpectrumDialog.Builder(contents)
                    .setColors(R.array.color_picker_target)
                    .setSelectedColor(textColor)
                    .setOutlineWidth(1)
                    .setOnColorSelectedListener((positiveResult, color) -> {
                        dto.setTextColor(color);
                        dto.getPreviewText().setTextColor(color);
                        textColorButton.setTag(R.string.tag_member_lesson_preview_color, color);
                    }).build().show(getFragmentManager(), "text_color_select_dialog");
        });

        dto.getBackgroundColorImageButton().setOnClickListener(view -> {

            final ImageButton backgroundColorButton = (ImageButton) view;
            final int backgroundColor = backgroundColorButton.getTag(R.string.tag_member_lesson_preview_color) == null
                    ? ContextCompat.getColor(contents, R.color.amber_500)
                    : (int) backgroundColorButton.getTag(R.string.tag_member_lesson_preview_color);

            new SpectrumDialog.Builder(contents)
                    .setColors(R.array.color_picker_target)
                    .setSelectedColor(backgroundColor)
                    .setOutlineWidth(1)
                    .setOnColorSelectedListener((positiveResult, color) -> {
                        dto.setBackgroundColor(color);
                        dto.getPreviewText().setBackgroundColor(color);
                        backgroundColorButton.setTag(R.string.tag_member_lesson_preview_color, color);
                    }).build().show(getFragmentManager(), "background_color_select_dialog");
        });
    }

    /**
     *
     * Store Member
     *
     * <p>
     * Overview:<br>
     * </p>
     */
    private void storeMemberToDto() {

        final StringBuilder builder = new StringBuilder();
        final String key = getString(R.string.key_preference_member_name_display);
        final String defaultValue = getString(R.string.value_preference_member_name_display);
        final int displayNameCode = Integer.valueOf(preferences.getString(key, defaultValue));

        disposable = memberService.getById(memberId).subscribe(entity -> {
            dto.getMemberName().setText(memberService.getDisplayName(entity, displayNameCode, builder));
            if( entity.photo != null && entity.photo.length > 0 ) {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(entity.photo, 0, entity.photo.length);
                dto.getPhoto().setImageBitmap(bitmap);
            }
        }, throwable -> Log.e(TAG, "can not get member entity by id: " + memberId, throwable));
    }

    /**
     *
     * Store Initial Value to DTO
     *
     * <p>
     * Overview:<br>
     * Initial value is set to the screen element to be edited
     * when new event is created.
     * </p>
     */
    private void storeDefaultValueToDto() {

        /* Default dayOfWeek (Monday) */
        final String dayOfWeek = "2";
        final String[] dayOfWeeks = getResources().getStringArray(R.array.entries_day_of_week);

        dto.setDayOfWeekValue(dayOfWeek);
        dto.getDayOfWeek().setText(dayOfWeeks[1]);

        /* Default Time Period (10:00 - 12:00) */
        dto.getStartTimeText().setText(getResources().getString(R.string.default_member_lesson_start_time));
        dto.getEndTimeText().setText(getResources().getString(R.string.default_member_lesson_end_time));
        dto.setStartTime(DateTimeUtil.parseTime(DateTimeUtil.TIME_FORMAT_HHMM, dto.getStartTimeText().getText().toString()));
        dto.setEndTime(DateTimeUtil.parseTime(DateTimeUtil.TIME_FORMAT_HHMM, dto.getEndTimeText().getText().toString()));

        /* Default Date Period (Today - after 3 months)*/
        final Calendar calendar = DateTimeUtil.today();
        dto.getStartPeriod().setText(DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, calendar));
        calendar.add(Calendar.MONTH, 3);
        dto.getEndPeriod().setText(DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, calendar));

        /* Default Color (text: @color/black, background: @color/amber_500) */
        final int textColor = dto.getPreviewText().getCurrentTextColor();
        final int backgroundColor = ((ColorDrawable) dto.getPreviewText().getBackground()).getColor();
        final int tag = R.string.tag_member_lesson_preview_color;

        dto.setTextColor(textColor);
        dto.getTextColorImageButton().setTag(tag, textColor);

        dto.setBackgroundColor(backgroundColor);
        dto.getBackgroundColorImageButton().setTag(tag, backgroundColor);

    }

    /**
     *
     * Store Entity to DTO
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     * 
     * @param lessonId member lesson id
     */
    private void storeEntityToDto(final long lessonId) {

        disposable = memberLessonService.getById(lessonId).subscribe(entity -> {
            dto.store(entity);
            dto.getDayOfWeek().setText(buildDayOfWeek(dto.getDayOfWeekValue()));
        }, throwable -> Log.e(TAG, "can not read MemberLesson by id: " + lessonId, throwable));
    }

    private boolean validatePeriod() {

        if( dto.getStartTime().after(dto.getEndTime()) ) {
            dto.getEndTimeText().setError(getString(R.string.message_member_lesson_end_time_invalid));
            Log.w(TAG, "start time after end time!");
            return false;
        }

        final Calendar start = DateTimeUtil.parserCalendar(dto.getStartPeriod().getText().toString(), DATE_PATTERN_YYYYMMDD);
        final Calendar end = DateTimeUtil.parserCalendar(dto.getEndPeriod().getText().toString(), DATE_PATTERN_YYYYMMDD);

        if( start.after(end) ) {
            dto.getEndPeriod().setError(getString(R.string.message_member_lesson_end_period_invalid_relation));
            Log.w(TAG, "start period after end period!");
            return false;
        }

        start.add(Calendar.MONTH, 6);

        if( end.after(start) ) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(contents);
            builder.setTitle(R.string.title_member_lesson_dialog_invalid_period)
                    .setIcon(R.drawable.ic_info_black)
                    .setMessage(R.string.message_member_lesson_end_period_invalid_span)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();

            dto.getEndPeriod().setError(getString(R.string.message_member_lesson_end_period_invalid_span));
            Log.w(TAG, "End date exceeds 6 months after start date!");
            return false;
        }

        return true;
    }

    /**
     *
     * Build Day of Week String
     *
     * @param dayOfWeek saved string (e.g. "3,5")
     * @return converted display string (e.g. "Tue, Thr")
     */
    private String buildDayOfWeek(final String dayOfWeek) {

        /* short name: Sun, Mon, Tue, Wed... */
        final String[] days = getResources().getStringArray(R.array.entries_day_of_week);
        /* day decimal string value: 1, 2, 3... */
        final String[] dayValues = getResources().getStringArray(R.array.entry_values_day_of_week);
        final boolean[] index = ArrayUtil.convertIndexFromArray(dayOfWeek, dayValues, ",");

        return ArrayUtil.concatIndexOfArray(days, index, ", ");
    }
}
