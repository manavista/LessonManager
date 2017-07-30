package jp.manavista.developbase.view.week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import java.text.DateFormat;
import java.util.List;

import jp.manavista.developbase.dto.TimetableDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    private static final String TAG = LessonView.class.getSimpleName();

    /** Lesson time paint */
    private Paint lessonTimePaint;
    /** Lesson no paint */
    private Paint lessonNoPaint;
    /** Lesson no color */
    private int lessonNoColor;
    /** Lesson background paint */
    private Paint lessonNoBackgroundPaint;
    /** Lesson background color */
    @Getter @Setter
    private int lessonNoBackgroundColor = Color.LTGRAY;
    /** Lesson no margin width */
    @Getter @Setter
    private int lessonNoPaintMarginWidth = 4;

    /** Lesson timetable data transfer object list */
    private List<TimetableDto> lessonTableList;


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
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        drawLessonColumn(canvas);
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

        // TODO: set lesson no paint and background color in xml

        lessonNoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lessonNoPaint.setTextAlign(Paint.Align.CENTER);
        lessonNoPaint.setTextSize(super.getTextSize());
        lessonNoPaint.setTypeface(Typeface.DEFAULT_BOLD);
        lessonNoPaint.setColor(super.getHeaderColumnTextColor());

        lessonTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lessonTimePaint.setTextAlign(Paint.Align.RIGHT);
        lessonTimePaint.setTextSize(super.getTextSize());
        lessonTimePaint.setColor(super.getHeaderColumnTextColor());

        lessonNoBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lessonNoBackgroundPaint.setColor(lessonNoBackgroundColor);
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

}
