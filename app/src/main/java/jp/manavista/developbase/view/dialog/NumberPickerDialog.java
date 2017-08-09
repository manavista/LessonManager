package jp.manavista.developbase.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class NumberPickerDialog extends Dialog {

    /** Selectable minimum value */
    private static final int MIN_VALUE = 0;
    /** Selectable maximum value */
    private static final int MAX_VALUE = 20;
    /** Wrap selector wheel */
    private static final boolean WRAP_SELECTOR_WHEEL = false;

    private NumberPicker numberPicker;

    public NumberPickerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void create() {

        super.create();

        numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(MIN_VALUE);
        numberPicker.setMaxValue(MAX_VALUE);
        numberPicker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        numberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(numberPicker);

        setContentView(linearLayout);

    }
}
