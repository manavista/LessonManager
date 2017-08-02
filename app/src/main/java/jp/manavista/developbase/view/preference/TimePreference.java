package jp.manavista.developbase.view.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import lombok.Getter;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class TimePreference extends DialogPreference {

    @Getter
    private int lastHour;
    @Getter
    private int lastMinute;
    /** TimePicker Object */
    private TimePicker picker;

    /** Constructor */
    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    public static int getHour(String time) {

        String[] pieces = time.split(":");
        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {

        String[] pieces = time.split(":");
        return(Integer.parseInt(pieces[1]));
    }

    @Override
    protected View onCreateDialogView() {
        this.picker = new TimePicker(getContext());
        return picker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        super.onDialogClosed(positiveResult);

        if ( positiveResult ) {

            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);

            if ( callChangeListener(time) ) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        String time;

        if (restorePersistedValue) {
            if (defaultValue==null) {
                time = getPersistedString("00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }
}
