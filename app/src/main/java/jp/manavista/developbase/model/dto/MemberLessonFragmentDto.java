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

import java.io.Serializable;
import java.sql.Time;
import java.util.List;
import java.util.Locale;

import jp.manavista.developbase.fragment.MemberLessonFragment;
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

    private TextView memberName;

    private EditText name;
    private EditText abbr;
    private EditText type;
    private EditText location;
    private EditText presenter;

    private EditText startTimeText;
    private EditText endTimeText;
    private ImageButton timetableIcon;

    private Time startTime;
    private Time endTime;

    private EditText dayOfWeek;
    private String dayOfWeekValue;

    private EditText startPeriod;
    private EditText endPeriod;

    private TextView previewText;
    @ColorInt
    private int textColor;
    @ColorInt
    private int backgroundColor;

    private ImageButton textColorIcon;
    private ImageButton backgroundColorIcon;


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

}
