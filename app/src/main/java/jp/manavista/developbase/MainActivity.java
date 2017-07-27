package jp.manavista.developbase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.manavista.developbase.view.fragment.LessonViewFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LessonViewFragment fragment = new LessonViewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_lesson_view, fragment)
                .commit();
    }

}
