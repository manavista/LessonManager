/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.model.dto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.manavista.lessonmanager.fragment.MemberLessonFragment;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.image.CircleImageView;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static jp.manavista.lessonmanager.util.DateTimeUtil.DATE_PATTERN_YYYYMMDD;
import static jp.manavista.lessonmanager.util.DateTimeUtil.TIME_FORMAT_HHMM;

/**
 *
 * MemberLesson Fragment Data Transfer Object
 *
 * <p>
 * Overview:<br>
 * Definition of objects used to input and output data on the screen.
 * </p>
 */
@Data
@Builder
public final class MemberLessonDto implements Serializable {

    /** Logger tag string */
    private static final String TAG = MemberLessonFragment.class.getSimpleName();

    /** Tag argument: Set Color */
    public static final String TAG_SET_COLOR = "TAG_SET_COLOR";
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    private List<TimetableDto> timetableDtoList;

    private String dateFormat;

    /** entity id */
    private long id;
    /** memberId */
    private long memberId;

    private TextView memberName;
    private CircleImageView photo;

    @NotEmpty
    @Length(max = 50)
    private EditText name;
    @Optional
    @Length(max = 50)
    private EditText abbr;
    @Optional
    @Length(max = 50)
    private EditText type;
    @Optional
    @Length(max = 50)
    private EditText location;
    @Optional
    @Length(max = 50)
    private EditText presenter;

    @NotEmpty
    private EditText startTimeText;
    @NotEmpty
    private EditText endTimeText;

    @NotEmpty
    private EditText dayOfWeek;
    private String dayOfWeekValue;

    @NotEmpty
    private EditText startPeriod;
    @NotEmpty
    private EditText endPeriod;

    private TextView previewText;
    @ColorInt
    private int textColor;
    @ColorInt
    private int backgroundColor;

    private ImageButton timetableIcon;
    private ImageButton textColorImageButton;
    private ImageButton backgroundColorImageButton;

    private Time startTime;
    private Time endTime;


    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            startTime = DateTimeUtil.parseTime(hourOfDay, minute);
            startTimeText.setText(TIME_FORMAT_HHMM.format(startTime));

            if( endTime != null && endTime.before(startTime) ) {
                endTime = DateTimeUtil.parseTime(hourOfDay, minute);
                endTimeText.setText(TIME_FORMAT_HHMM.format(endTime));
            }
        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            endTime = DateTimeUtil.parseTime(hourOfDay, minute);
            endTimeText.setText(TIME_FORMAT_HHMM.format(endTime));
        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DialogInterface.OnClickListener timetableSetLister = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, final int which) {

            if( timetableDtoList == null || timetableDtoList.isEmpty() ) {
                return;
            }
            if( timetableDtoList.size() - 1 < which ) {
                throw new RuntimeException("invalid select row");
            }

            final TimetableDto timetable = timetableDtoList.get(which);
            startTimeText.setText(timetable.getStartTimeFormatted());
            endTimeText.setText(timetable.getEndTimeFormatted());
        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DialogInterface.OnClickListener dayOfWeekSetListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {

        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DatePickerDialog.OnDateSetListener startPeriodSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthYear, int dayOfMonth) {
            monthYear += 1; /* monthYear is 0-11 */
            final String date = String.format(Locale.getDefault(), dateFormat, year, monthYear, dayOfMonth);
            startPeriod.setText(date);

            final Calendar start = DateTimeUtil.parserCalendar(startPeriod.getText().toString(), DATE_PATTERN_YYYYMMDD);
            final Calendar end = DateTimeUtil.parserCalendar(endPeriod.getText().toString(), DATE_PATTERN_YYYYMMDD);

            if( start.after(end) ) {
                endPeriod.setText(date);
            }
        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DatePickerDialog.OnDateSetListener endPeriodSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthYear, int dayOfMonth) {
            monthYear += 1; /* monthYear is 0-11 */
            endPeriod.setText(String.format(Locale.getDefault(), dateFormat, year, monthYear, dayOfMonth));
        }
    };

    /**
     *
     * Convert
     *
     * <p>
     * Overview:<br>
     * Convert DTO to entity.
     * </p>
     *
     * @return {@link MemberLesson} entity.
     */
    public MemberLesson toEntity() {

        final MemberLesson entity = new MemberLesson();

        entity.id = id;
        entity.memberId = memberId;
        entity.name = name.getText().toString();
        entity.abbr = abbr.getText().toString();
        entity.type = type.getText().toString();
        entity.location = location.getText().toString();
        entity.presenter = presenter.getText().toString();
        entity.textColor = textColor;
        entity.backgroundColor = backgroundColor;
        entity.startTime = DateTimeUtil.parseTime(TIME_FORMAT_HHMM, startTimeText.getText().toString());
        entity.endTime = DateTimeUtil.parseTime(TIME_FORMAT_HHMM, endTimeText.getText().toString());
        entity.dayOfWeeks = dayOfWeekValue;
        entity.periodFrom = startPeriod.getText().toString();
        entity.periodTo = endPeriod.getText().toString();

        return entity;
    }

    public void store(@NonNull final MemberLesson entity) {

        setId(entity.id);
        getName().setText(entity.name);
        getAbbr().setText(entity.abbr);
        getType().setText(entity.type);
        getLocation().setText(entity.location);
        getPresenter().setText(entity.presenter);

        setDayOfWeekValue(entity.dayOfWeeks);
        setStartTime(entity.startTime);
        setEndTime(entity.endTime);

        startTime = entity.startTime;
        endTime = entity.endTime;
        startTimeText.setText(TIME_FORMAT_HHMM.format(startTime));
        endTimeText.setText(TIME_FORMAT_HHMM.format(endTime));

        startPeriod.setText(entity.periodFrom);
        endPeriod.setText(entity.periodTo);

        textColor = entity.textColor;
        backgroundColor = entity.backgroundColor;
        getPreviewText().setTextColor(entity.textColor);
        getPreviewText().setBackgroundColor(entity.backgroundColor);

    }
}
