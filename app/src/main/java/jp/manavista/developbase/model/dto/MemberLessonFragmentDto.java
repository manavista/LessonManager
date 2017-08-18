package jp.manavista.developbase.model.dto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
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
import java.util.List;
import java.util.Locale;

import jp.manavista.developbase.fragment.MemberLessonFragment;
import jp.manavista.developbase.model.entity.MemberLesson;
import jp.manavista.developbase.util.DateTimeUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
public final class MemberLessonFragmentDto implements Serializable {

    /** Logger tag string */
    private static final String TAG = MemberLessonFragment.class.getSimpleName();

    /** Tag argument: Set Color */
    public static final String TAG_SET_COLOR = "TAG_SET_COLOR";

    private List<TimetableDto> timetableDtoList;

    private String dateFormat;

    /** entity id */
    private int id;
    /** memberId */
    private int memberId;

    private TextView memberName;

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
    private ImageButton timetableIcon;

    private Time startTime;
    private Time endTime;

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

    private ImageButton textColorImageButton;
    private ImageButton backgroundColorImageButton;


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
            startPeriod.setText(String.format(Locale.getDefault(), dateFormat, year, monthYear, dayOfMonth));
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
    public MemberLesson convert() {

        final MemberLesson memberLesson = new MemberLesson();

        memberLesson.id = id;
        memberLesson.memberId = memberId;
        memberLesson.name = name.getText().toString();
        memberLesson.abbr = abbr.getText().toString();
        memberLesson.type = type.getText().toString();
        memberLesson.location = location.getText().toString();
        memberLesson.presenter = presenter.getText().toString();
        memberLesson.textColor = textColor;
        memberLesson.backgroundColor = backgroundColor;
        memberLesson.startTime = DateTimeUtil.parseTime(DateTimeUtil.TIME_FORMAT_HHMM, startTimeText.getText().toString());
        memberLesson.endTime = DateTimeUtil.parseTime(DateTimeUtil.TIME_FORMAT_HHMM, endTimeText.getText().toString());
        memberLesson.dayOfWeeks = dayOfWeekValue;
        memberLesson.periodFrom = startPeriod.getText().toString();
        memberLesson.periodTo = endPeriod.getText().toString();

        return memberLesson;
    }
}
