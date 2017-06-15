package jp.manavista.developbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by atsushi on 2017/06/13.
 */

public final class DummySectionFragment extends Fragment {

    public static final String  SECTION_NUMBER_PROP = "sectionNumber";

    @Nullable
    @Override
    public View onCreateView (
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
        Bundle args = getArguments();
        ((TextView)rootView.findViewById(R.id.textDummy)).setText(getString(R.string.dummy_section_text, args.getInt(SECTION_NUMBER_PROP)));
        return rootView;
    }
}
