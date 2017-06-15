package jp.manavista.developbase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final Context context = this;

        cal = Calendar.getInstance();
        // cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Log.d("date: ", sdf.format(cal.getTime()));

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(
                getSupportFragmentManager(),cal
        ));
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast toast = Toast.makeText(context, "page "+ position +" change", Toast.LENGTH_SHORT);
//                toast.show();
//                viewPager.setCurrentItem(1);
//                cal.add(Calendar.DAY_OF_MONTH, 1);
//                viewPager.setCurrentItem(1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }
}
