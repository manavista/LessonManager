package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;
import lombok.val;

/**
 *
 * Setting Delete Fragment
 *
 * <p>
 * Overview:<br>
 * Define screen to delete internal categoriesList
 * </p>
 */
public class SettingDeleteFragment extends Fragment {

    private View rootView;
    private ListView listView;

    /** Activity Contents */
    private Activity contents;

    @Inject
    MemberLessonService memberLessonService;
    @Inject
    MemberService memberService;

    public SettingDeleteFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingDeleteFragment.
     */
    public static SettingDeleteFragment newInstance() {
        SettingDeleteFragment fragment = new SettingDeleteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting_delete, container, false);
        return inflater.inflate(R.layout.fragment_setting_delete, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contents = getActivity();

        DependencyInjector.appComponent().inject(this);

        List<String> categoriesList = Arrays.asList(getResources().getStringArray(R.array.entries_delete_category));

        final ImageButton deleteButton = contents.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(deleteButtonListener);

        val adapter = new ArrayAdapter<String>(contents, android.R.layout.simple_list_item_multiple_choice, categoriesList);
        listView = contents.findViewById(R.id.category_delete_list);
        listView.setAdapter(adapter);
    }

    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Set<String> deleteSet = new HashSet<>();
            for( int i = 0 ; i < listView.getChildCount() ; i++ ) {
                CheckedTextView textView = (CheckedTextView) listView.getChildAt(i);
                Log.d(textView.getText().toString(), textView.isChecked() ? "ON":"OFF");
                if( textView.isChecked() ) {
                    deleteSet.add(textView.getText().toString());
                }
            }

            // TODO: 2017/08/25 define service class

            if( deleteSet.contains("Schedule") ) {
                contents.finish();
            }

            if( deleteSet.contains("Lesson") ) {
                memberLessonService.deleteAll().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        contents.finish();
                    }
                });
            }

            if( deleteSet.contains("Member") ) {
                memberService.deleteAll().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        contents.finish();
                    }
                });
            }

        }
    };
}
