package jp.manavista.developbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import jp.manavista.developbase.R;
import jp.manavista.developbase.fragment.MemberListFragment;

/**
 *
 * Member List Fragment
 *
 * <p>
 * Overview:<br>
 * Display a list of members. <br>
 * Provide interface for editing and creating new.
 * </p>
 */
public class MemberListActivity extends AppCompatActivity {

    /** Logger tag string */
    public static final String TAG = MemberListActivity.class.getSimpleName();

    /** MemberList fragment */
    private MemberListFragment memberListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        memberListFragment = MemberListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member_list, memberListFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_member_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_add:
                Intent intent = new Intent(this, MemberActivity.class);
                this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
