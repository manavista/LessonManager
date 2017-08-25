package jp.manavista.lessonmanager.view.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import jp.manavista.lessonmanager.R;

/**
 *
 * NumberPickerPreference
 *
 * <p>
 * Overview:<br>
 * Define a dialog to pick up numbers in Preference.
 * </p>
 */
public class NumberPickerPreference extends DialogPreference {

    /** Selectable minimum value */
    private static final int MIN_VALUE = 0;
    /** Selectable maximum value */
    private static final int MAX_VALUE = 100;
    private static final boolean WRAP_SELECTOR_WHEEL = false;

    private int selectedValue;
    private final int minValue;
    private final int maxValue;
    private final boolean wrapSelectorWheel;
    private NumberPicker numberPicker;


    /** Constructor */
    public NumberPickerPreference(Context context, AttributeSet attrs) {

        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);

        try {

            this.minValue = a.getInt(R.styleable.NumberPickerPreference_minValue, MIN_VALUE);
            this.maxValue = a.getInt(R.styleable.NumberPickerPreference_maxValue, MAX_VALUE);
            this.wrapSelectorWheel = a.getBoolean(R.styleable.NumberPickerPreference_wrapWheel, WRAP_SELECTOR_WHEEL);

        } finally {
            a.recycle();
        }
    }

    /**
     *
     * Get Selected Value
     *
     * <p>
     * Overview:<br>
     * Get selected in spinner value.
     * </p>
     *
     * @return selected value
     */
    public int getSelectedValue() {
        return this.selectedValue;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        final int intDefaultValue = defaultValue instanceof Integer ? (int) defaultValue : minValue;
        selectedValue = restorePersistedValue ? this.getPersistedInt(intDefaultValue) : intDefaultValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {

        super.onPrepareDialogBuilder(builder);

        numberPicker = new NumberPicker(this.getContext());
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(selectedValue);
        numberPicker.setWrapSelectorWheel(wrapSelectorWheel);
        numberPicker.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(numberPicker);

        builder.setView(linearLayout);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        super.onDialogClosed(positiveResult);

        if ( positiveResult && numberPicker != null ) {

            final int newValue = numberPicker.getValue();

            if ( this.callChangeListener(newValue) ) {
                this.selectedValue = newValue;
                this.persistInt(this.selectedValue);
            }
        }
    }
}
