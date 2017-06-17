package jp.manavista.developbase;

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

import jp.manavista.developbase.util.DateUtil;

/**
 * Created by atsushi on 2017/06/13.
 */

public final class DummySectionFragment extends Fragment {

    public static final String  CALENDAR_OFFSET_DAY_PROP = "calendarOffsetDay";

    @Nullable
    @Override
    public View onCreateView (
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
        Bundle args = getArguments();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, args.getInt(CALENDAR_OFFSET_DAY_PROP));

        Calendar[] calendars = DateUtil.getWeekRangeOfMonth(calendar.getTime(), Calendar.MONDAY, Calendar.SATURDAY);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        final String date = sdf.format(calendars[0].getTime()) + " - " + sdf.format(calendars[1].getTime());

        ((TextView)rootView.findViewById(R.id.textDummy)).setText(date);
        return rootView;
    }
}
