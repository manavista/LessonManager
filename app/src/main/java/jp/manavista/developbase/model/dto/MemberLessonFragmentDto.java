package jp.manavista.developbase.model.dto;

import android.support.annotation.ColorInt;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import jp.manavista.developbase.fragment.MemberLessonFragment;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Data
@Builder
public final class MemberLessonFragmentDto implements Serializable {

    /** Logger tag string */
    public static final String TAG = MemberLessonFragment.class.getSimpleName();

    /** entity id */
    private int id;

    private TextView memberName;

    private EditText name;
    private EditText abbr;
    private EditText type;
    private EditText location;
    private EditText presenter;

    private EditText startTime;
    private EditText endTime;
    private ImageView timetableIcon;

    private EditText dayOfWeek;

    private EditText startPeriod;
    private EditText endPeriod;

    @ColorInt
    private int textColor;
    @ColorInt
    private int backgroundColor;

    // TODO: change image view to image button
    private ImageView textColorIcon;
    private ImageView backgroundColorIcon;
}
