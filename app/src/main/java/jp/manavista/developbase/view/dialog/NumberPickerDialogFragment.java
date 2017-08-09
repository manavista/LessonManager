package jp.manavista.developbase.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.io.Serializable;

/**
 *
 * NumberPicker Dialog Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 *
 */
public class NumberPickerDialogFragment extends DialogFragment {

    private static final String ARG_MIN = "arg_min";
    private static final String ARG_MAX = "arg_max";
    private static final String ARG_DEFAULT = "arg_default";
    private static final String ARG_LISTENER = "arg_listener";
    private static final String ARG_TITLE = "arg_title";

    /** Selectable minimum value */
    private int minValue;
    /** Selectable maximum value */
    private int maxValue;
    /** Selected default value */
    private int defaultValue;
    /** Wrap selector wheel */
    private static final boolean WRAP_SELECTOR_WHEEL = false;
    /** Title string */
    private String title;

    private NumberPicker numberPicker;
    /** Dialog fragment listener */
    private OnSetListener onSetListener;


    /** Constructor */
    public NumberPickerDialogFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param listener on NumberPicker set listener
     * @param min selectable minimum value
     * @param max selectable maximum value
     * @param selected default value
     * @param title dialog title string
     * @return A new instance of fragment NumberPickerDialogFragment.
     */
    public static NumberPickerDialogFragment newInstance(
            @NonNull OnSetListener listener, int min, int max, int selected, final String title) {

        NumberPickerDialogFragment fragment = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LISTENER, listener);
        args.putInt(ARG_MIN, min);
        args.putInt(ARG_MAX, max);
        args.putInt(ARG_DEFAULT, selected);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if ( getArguments() != null ) {

            this.onSetListener = (OnSetListener) getArguments().getSerializable(ARG_LISTENER);
            minValue = getArguments().getInt(ARG_MIN);
            maxValue = getArguments().getInt(ARG_MAX);
            defaultValue = getArguments().getInt(ARG_DEFAULT);
            title = getArguments().getString(ARG_TITLE);

        } else {

            this.onSetListener = new OnSetListener() {
                @Override
                public void onNumberSet(int value) {
                    /* no description */
                }
            };
            minValue = 1;
            maxValue = 20;
            defaultValue = minValue;
            title = "Select Number";
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(defaultValue);
        numberPicker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        numberPicker.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(numberPicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle(this.title)
                .setView(linearLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int value = numberPicker.getValue();
                        onSetListener.onNumberSet(value);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /* no description */
                    }
                })
                .create();
    }

    /**
     *
     * Set NumberPicker Listener interface
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     *
     */
    public interface OnSetListener extends Serializable {
        void onNumberSet(final int value);
    }
}
