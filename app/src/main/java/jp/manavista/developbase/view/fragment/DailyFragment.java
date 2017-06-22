package jp.manavista.developbase.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jp.manavista.developbase.R;

/**
 *
 * Daily Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 *
 * A simple {@link Fragment} subclass.
 */
public final class DailyFragment extends Fragment {

    public static final String CALENDAR_OFFSET_DAY_PROP = "calendarOffsetDay";

    /** Default Constructor */
    public DailyFragment() {
        // Every fragment must have an empty constructor.
        // https://developer.android.com/reference/android/app/Fragment.html#Fragment()
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param offset Calendar offset of offset.
     * @return A new instance of fragment DailyFragment.
     */
    public static DailyFragment newInstance(int offset) {
        DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putInt(CALENDAR_OFFSET_DAY_PROP, offset);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        Bundle args = getArguments();

        final SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, args.getInt(CALENDAR_OFFSET_DAY_PROP));

        TextView dayOfWeek = rootView.findViewById(R.id.dayOfWeek);
        dayOfWeek.setText(dayOfWeekFormat.format(calendar.getTime()));

        return rootView;
    }

}
