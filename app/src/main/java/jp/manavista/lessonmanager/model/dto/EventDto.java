package jp.manavista.lessonmanager.model.dto;

import android.app.DatePickerDialog;
import android.support.annotation.ColorInt;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.io.Serializable;
import java.util.Locale;

import jp.manavista.lessonmanager.model.entity.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Event Data Transfer Object
 *
 * <p>
 * Overview:<br>
 * </p>
 */
@Data
@Builder
public final class EventDto implements Serializable {

    /** Logger tag string */
    public static final String TAG = EventDto.class.getSimpleName();

    /** entity id */
    private long id;
    /** DateFormat */
    private String dateFormat;

    @NotEmpty
    @Length(max = 20)
    private EditText name;
    @NotEmpty
    private EditText date;
    @Optional
    @Length(max = 50)
    private EditText memo;

    private TextView previewText;
    @ColorInt
    private int textColor;
    @ColorInt
    private int backgroundColor;

    private ImageButton eventDateImageButton;
    private ImageButton textColorImageButton;
    private ImageButton backgroundColorImageButton;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthYear, int dayOfMonth) {
            monthYear += 1; /* monthYear is 0-11 */
            date.setText(String.format(Locale.getDefault(), dateFormat, year, monthYear, dayOfMonth));
        }
    };

    /**
     *
     * To Entity
     *
     * <p>
     * Overview:<br>
     * Convert DTO to entity object.
     * </p>
     *
     * @return {@link Event} Object
     */
    public Event toEntity() {

        Event entity = new Event();

        entity.id = id;
        entity.name = name.getText().toString();
        entity.date = date.getText().toString();
        entity.memo = memo.getText().toString();
        entity.textColor = textColor;
        entity.backgroundColor = backgroundColor;

        return entity;
    }
}

