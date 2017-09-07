package jp.manavista.lessonmanager.view.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import java.text.DateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.TimetableDto;

/**
 *
 * LessonView
 *
 * <p>
 * Overview:<br>
 * Definition of view to display lesson schedule week by vertical date.
 * </p>
 *
 * @see WeekView WeekView
 */
public final class LessonView extends WeekView {

    /** Logger tag string */
    private static final String TAG = LessonView.class.getSimpleName();

    /** Lesson time paint */
    private Paint lessonTimePaint;
    /** Lesson no paint */
    private Paint lessonNoPaint;
    /** Lesson background paint */
    private Paint lessonNoBackgroundPaint;
    /** Lesson no color */
    private int lessonNoColor = Color.RED;
    /** Lesson background color */
    private int lessonNoBackgroundColor = Color.LTGRAY;
    /** Lesson no margin width */
    private int lessonNoPaintMarginWidth = 4;

    /** Lesson timetable data transfer object list */
    private List<TimetableDto> lessonTableList;
    /** Except draw hour set */
    private Set<Integer> exceptDrawHourSet;

    /** Constructor */
    public LessonView(Context context) {
        super(context);
    }

    /** Constructor */
    public LessonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /** Constructor */
    public LessonView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeekView, 0, 0);

        try {

            lessonNoColor = a.getColor(R.styleable.WeekView_lessonNoColor, lessonNoColor);
            lessonNoBackgroundColor = a.getColor(R.styleable.WeekView_lessonNoBackgroundColor, lessonNoBackgroundColor);

        } finally {
            a.recycle();
        }

        init();
    }

    /**
     *
     * Set lesson timetable list
     *
     * <p>
     * Overview:<br>
     * set lesson timetable dto list.
     * </p>
     *
     * @param lessonTableList lesson timetable dto list
     */
    public void setLessonTableList(List<TimetableDto> lessonTableList) {
        this.lessonTableList = lessonTableList;
        calcExceptDrawHourSet();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        drawLessonColumn(canvas);
    }

    @Override
    protected void drawTimeColumnAndAxes(Canvas canvas) {

        float headerHeight = super.getHeaderHeight();
        int headerRowPadding = super.getHeaderRowPadding();
        PointF currentOrigin = super.getCurrentOrigin();
        int hourHeight = super.getHourHeight();
        int startHour = super.getStartTime();
        int endHour = super.getEndTime();
        float headerMarginBottom = super.getHeaderMarginBottom();

        float timeTextWidth = super.getTimeTextWidth();
        float timeTextHeight = super.getTimeTextHeight();
        int headerColumnPadding = super.getHeaderColumnPadding();
        float headerColumnWidth = super.getHeaderColumnWidth();

        canvas.drawRect(0, headerHeight + headerColumnPadding * 2, headerColumnWidth, getHeight(), super.getHeaderColumnBackgroundPaint());
        canvas.clipRect(0, headerHeight + headerColumnPadding * 2 + headerMarginBottom + timeTextHeight/2, headerColumnWidth, getHeight(), Region.Op.REPLACE);
//        canvas.clipOutRect(0, headerHeight + headerColumnPadding * 2, headerColumnWidth, getHeight());


        for ( int i = startHour ; i < endHour ; i++ ) {

            if ( exceptDrawHourSet.contains(i) ) {
                continue;
            }

            float top = headerHeight + headerRowPadding * 2 + currentOrigin.y
                    + hourHeight * (i - startHour) + headerMarginBottom;
            String time = getDateTimeInterpreter().interpretTime(i);

            if ( top < getHeight() ) {
                float x = timeTextWidth + headerColumnPadding;
                float y = top + timeTextHeight;
                canvas.drawText(time, x, y, super.getTimeTextPaint());
            }
        }
    }

    /**
     *
     * Initial setting
     *
     * <p>
     * Overview:<br>
     * Define processing at initialization of this view.<br>
     * Mainly set an instance for property of this class.
     * </p>
     */
    private void init() {

        lessonNoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lessonNoPaint.setTextAlign(Paint.Align.CENTER);
        lessonNoPaint.setTextSize(super.getTextSize());
        lessonNoPaint.setTypeface(Typeface.DEFAULT_BOLD);
        lessonNoPaint.setColor(lessonNoColor);

        lessonTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lessonTimePaint.setTextAlign(Paint.Align.RIGHT);
        lessonTimePaint.setTextSize(super.getTextSize());
        lessonTimePaint.setColor(super.getHeaderColumnTextColor());

        lessonNoBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lessonNoBackgroundPaint.setColor(lessonNoBackgroundColor);

        exceptDrawHourSet = new HashSet<>();
    }

    /**
     *
     * Draw LessonNo Column
     *
     * <p>
     * Overview:<br>
     * Display the defined timetable on the time axis.
     * </p>
     *
     * @param canvas Canvas Object
     */
    private void drawLessonColumn(Canvas canvas) {

        if( lessonTableList == null || lessonTableList.isEmpty() ) {
            return;
        }

        float headerHeight = super.getHeaderHeight();
        int headerRowPadding = super.getHeaderRowPadding();
        float headerColumnWidth = super.getHeaderColumnWidth();
        PointF currentOrigin = super.getCurrentOrigin();
        float headerMarginBottom = super.getHeaderMarginBottom();
        int hourHeight = super.getHourHeight();
        int startHour = super.getStartTime();
        float timeTextHeight = super.getTimeTextHeight();
        float timeTextWidth = super.getTimeTextWidth();
        int eventMarginVertical = super.getEventMarginVertical();
        int headerColumnPadding = super.getHeaderColumnPadding();

        int marginTop = hourHeight * startHour;

        for( TimetableDto dto : lessonTableList ) {

            // LessonNo rect

            float startTimeHeight = dto.getStartTime(DateFormat.HOUR_OF_DAY0_FIELD) * 60 + dto.getStartTime(DateFormat.MINUTE_FIELD);
            float top = hourHeight * 24 * startTimeHeight / 1440 + currentOrigin.y + headerHeight + headerRowPadding * 2
                    + headerMarginBottom + timeTextHeight / 2 + eventMarginVertical - marginTop;

            float endTimeHeight = dto.getEndTime(DateFormat.HOUR_OF_DAY0_FIELD) * 60 + dto.getEndTime(DateFormat.MINUTE_FIELD);
            float bottom = hourHeight * 24 * endTimeHeight / 1440 + currentOrigin.y + headerHeight + headerRowPadding * 2
                    + headerMarginBottom + timeTextHeight / 2 + eventMarginVertical - marginTop;

            float left = lessonNoPaintMarginWidth;
            float right = headerColumnWidth - lessonNoPaintMarginWidth;
            canvas.drawRect(left, top, right, bottom, lessonNoBackgroundPaint);

            // LessonNo start time

            float startTimeX = timeTextWidth + headerColumnPadding;
            float startTimeY = top + timeTextHeight + (timeTextHeight / 2);
            canvas.drawText(dto.getStartTimeFormatted(), startTimeX, startTimeY, lessonTimePaint);

            // LessonNo end time

            float endTimeX = timeTextWidth + headerColumnPadding;
            float endTimeY = bottom - (timeTextHeight / 2);
            canvas.drawText(dto.getEndTimeFormatted(), endTimeX, endTimeY, lessonTimePaint);

            // LessonNo
            float lessonNoX = headerColumnWidth / 2;
            float lessonNoY = top + ((bottom - top) / 2);
            canvas.drawText(String.valueOf(dto.getLessonNo()), lessonNoX, lessonNoY, lessonNoPaint);
        }

    }

    /**
     *
     * Calculate Except draw hour Set
     *
     * <p>
     * Overview:<br>
     * Calculate a time set not displayed from timetable.<br>
     * Delete the time display of the calendar when the calendar time display and
     * the start and end time of the lesson overlap or near.
     * </p>
     */
    private void calcExceptDrawHourSet() {

        if( lessonTableList == null || lessonTableList.isEmpty() ) {
            return;
        }

        exceptDrawHourSet.clear();

        for ( TimetableDto dto : lessonTableList ) {

            final int startHour = dto.getStartTime(DateFormat.HOUR_OF_DAY0_FIELD);
            final int startMinute = dto.getStartTime(DateFormat.MINUTE_FIELD);

            if( startMinute < 30 ) {
                exceptDrawHourSet.add(startHour);
            } else if ( startMinute > 30 ) {
                exceptDrawHourSet.add(startHour + 1);
            }

            final int endHour = dto.getEndTime(DateFormat.HOUR_OF_DAY0_FIELD);
            final int endMinute = dto.getEndTime(DateFormat.MINUTE_FIELD);

            if( endMinute < 30 ) {
                exceptDrawHourSet.add(endHour);
            } else if ( endMinute > 30 ) {
                exceptDrawHourSet.add(endHour + 1);
            }
        }

        Log.d(TAG, "Except draw hour: " + exceptDrawHourSet.toString());
    }

}
