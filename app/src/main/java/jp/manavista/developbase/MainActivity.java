package jp.manavista.developbase;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(
                getSupportFragmentManager()
        ));
    }
}
