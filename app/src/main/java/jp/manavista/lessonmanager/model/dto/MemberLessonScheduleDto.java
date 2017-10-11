/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.model.dto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;
import java.util.Locale;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.constants.MemberLessonScheduleStatus;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Member Lesson Schedule Data Transfer Object
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
@Data
@Builder
public final class MemberLessonScheduleDto implements Serializable {

    private static final String TAG = MemberLessonScheduleDto.class.getSimpleName();

    /** Tag argument: Set Color */
    public static final String TAG_SET_COLOR = "TAG_SET_COLOR";
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    private List<TimetableDto> timetableDtoList;

    private String dateFormat;

    /**
     *
     * Original Entity
     *
     * Save the acquired entity as it is.
     * When updating, make it the base to register.
     * In order to avoid having to get the current entity by id.
     */
    private MemberLessonSchedule original;

    private long id;

    private RadioGroup statusRadioGroup;
    private RadioButton statusScheduled;
    private RadioButton statusDone;
    private RadioButton statusAbsent;
    private RadioButton statusSuspend;
    private int status;

    @NotEmpty
    private EditText scheduleDate;

    @NotEmpty
    private EditText startTimeText;
    @NotEmpty
    private EditText endTimeText;
    private ImageButton timetableIcon;

    private Time startTime;
    private Time endTime;

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

    private TextView previewText;
    @ColorInt
    private int textColor;
    @ColorInt
    private int backgroundColor;

    private ImageButton textColorImageButton;
    private ImageButton backgroundColorImageButton;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthYear, int dayOfMonth) {
            monthYear += 1; /* monthYear is 0-11 */
            scheduleDate.setText(String.format(Locale.getDefault(), dateFormat, year, monthYear, dayOfMonth));
        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            startTime = DateTimeUtil.parseTime(hourOfDay, minute);
            startTimeText.setText(DateTimeUtil.TIME_FORMAT_HHMM.format(startTime));
        }
    };

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            endTime = DateTimeUtil.parseTime(hourOfDay, minute);
            endTimeText.setText(DateTimeUtil.TIME_FORMAT_HHMM.format(endTime));
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
    public final RadioGroup.OnCheckedChangeListener statusRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.scheduledRadioButton:
                    status = MemberLessonScheduleStatus.SCHEDULED.getId();
                    break;
                case R.id.doneRadioButton:
                    status = MemberLessonScheduleStatus.DONE.getId();
                    break;
                case R.id.suspendedRadioButton:
                    status = MemberLessonScheduleStatus.SUSPENDED.getId();
                    break;
                case R.id.absentRadioButton:
                    status = MemberLessonScheduleStatus.ABSENT.getId();
                    break;
            }
        }
    };


    public void store(@NonNull MemberLessonSchedule entity) {

        original = entity;

        scheduleDate.setText(entity.lessonDate);
        startTime = entity.lessonStartTime;
        endTime = entity.lessonEndTime;
        startTimeText.setText(DateTimeUtil.TIME_FORMAT_HHMM.format(startTime));
        endTimeText.setText(DateTimeUtil.TIME_FORMAT_HHMM.format(endTime));

        setId(entity.id);
        name.setText(entity.name);
        abbr.setText(entity.abbr);
        type.setText(entity.type);
        location.setText(entity.location);
        presenter.setText(entity.presenter);

        textColor = entity.textColor;
        backgroundColor = entity.backgroundColor;
        getPreviewText().setTextColor(entity.textColor);
        getPreviewText().setBackgroundColor(entity.backgroundColor);

        status = entity.status;
        switch (MemberLessonScheduleStatus.statusSparseArray().get(status)) {
            case SCHEDULED:
                statusScheduled.setChecked(true);
                break;
            case DONE:
                statusDone.setChecked(true);
                break;
            case SUSPENDED:
                statusSuspend.setChecked(true);
                break;
            case ABSENT:
                statusAbsent.setChecked(true);
                break;
            case UNDEFINED:
                break;
            default:
                Log.w(TAG, "schedule status code not exists !");
                break;
        }
    }

    public MemberLessonSchedule convert() {

        original.name = name.getText().toString();
        original.abbr = abbr.getText().toString();
        original.type = type.getText().toString();
        original.location = location.getText().toString();
        original.presenter = presenter.getText().toString();
        original.textColor = textColor;
        original.backgroundColor = backgroundColor;
        original.lessonDate = scheduleDate.getText().toString();
        original.lessonStartTime = DateTimeUtil.parseTime(DateTimeUtil.TIME_FORMAT_HHMM, startTimeText.getText().toString());
        original.lessonEndTime = DateTimeUtil.parseTime(DateTimeUtil.TIME_FORMAT_HHMM, endTimeText.getText().toString());
        original.status = status;

        return original;
    }
}
