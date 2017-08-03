package jp.manavista.developbase.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import jp.manavista.developbase.R;
import jp.manavista.developbase.fragment.MemberFragment;

public class MemberActivity extends AppCompatActivity {

    /** MemberFragment */
    private MemberFragment memberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        memberFragment = MemberFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_subscribe, memberFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.option_save:
                memberFragment.saveMember();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
