package jp.manavista.developbase.view.week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public final class LessonView extends WeekView {

    private Paint lessonTimePaint;
    private Paint lessonNoPaint;
    private Paint lessonNoBackgroundPaint;
    private int lessonNoBackgroundColor = Color.LTGRAY;

    private int lessonNoPaintMarginWidth = 4;

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

//        int startHour = 9;
//        int startMinute = 0;
//        int endHour = 10;
//        int endMinute = 30;

        String lessonNo = "1";

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 9);
        startTime.set(Calendar.MINUTE, 30);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 11);
        endTime.set(Calendar.MINUTE, 45);

        // TODO: lesson start 9:00 - end 10:30 rect, lessonNo and time.

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

        // eventRect.top = eventRect.event.getStartTime().get(Calendar.HOUR_OF_DAY) * 60 +
        // eventRect.event.getStartTime().get(Calendar.MINUTE);

        // float top = mHourHeight * 24 * mEventRects.get(i).top / 1440 + mCurrentOrigin.y + mHeaderHeight +
        // mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight/2 + mEventMarginVertical - marginTop;

        // eventRect.bottom = eventRect.event.getEndTime().get(Calendar.HOUR_OF_DAY) * 60 +
        // eventRect.event.getEndTime().get(Calendar.MINUTE);

        // bottom = mHourHeight * 24 * bottom / 1440 + mCurrentOrigin.y + mHeaderHeight +
        // mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight/2 - mEventMarginVertical - marginTop;

        // LessonNo rect

        float startTimeHeight = startTime.get(Calendar.HOUR_OF_DAY) * 60 + startTime.get(Calendar.MINUTE);
        float top = hourHeight * 24 * startTimeHeight / 1440 + currentOrigin.y + headerHeight + headerRowPadding * 2
                + headerMarginBottom + timeTextHeight / 2 + eventMarginVertical - marginTop;

        float endTimeHeight = endTime.get(Calendar.HOUR_OF_DAY) * 60 + endTime.get(Calendar.MINUTE);
        float bottom = hourHeight * 24 * endTimeHeight / 1440 + currentOrigin.y + headerHeight + headerRowPadding * 2
                + headerMarginBottom + timeTextHeight / 2 + eventMarginVertical - marginTop;

        float left = lessonNoPaintMarginWidth;
//        float top = headerHeight + headerRowPadding * 2 + currentOrigin.y + hourHeight * 9 + headerMarginBottom;
        float right = headerColumnWidth - lessonNoPaintMarginWidth;
        canvas.drawRect(left, top, right, bottom, lessonNoBackgroundPaint);


        // LessonNo start time

        SimpleDateFormat format = new SimpleDateFormat("hh a", Locale.getDefault());
        String startTimeLabel = format.format(startTime.getTime());
        float startTimeX = timeTextWidth + headerColumnPadding;
        float startTimeY = top + timeTextHeight + (timeTextHeight / 2);
        canvas.drawText(startTimeLabel, startTimeX, startTimeY, lessonTimePaint);

        // LessonNo end time

        String endTimeLabel = format.format(endTime.getTime());
        float endTimeX = timeTextWidth + headerColumnPadding;
        float endTimeY = bottom - (timeTextHeight / 2);
        canvas.drawText(endTimeLabel, endTimeX, endTimeY, lessonTimePaint);

        // LessonNo
        float lessonNoX = headerColumnWidth / 2;
        float lessonNoY = top + ((bottom - top) / 2);
        canvas.drawText(lessonNo, lessonNoX, lessonNoY, lessonNoPaint);

    }
}
