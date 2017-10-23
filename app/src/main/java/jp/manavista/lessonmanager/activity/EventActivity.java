/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.EventFragment;

public class EventActivity extends AppCompatActivity {

    /** activity put extra argument class */
    public class Extra {
        public static final String EVENT_ID = "EVENT_ID";
        public static final String EVENT_NAME = "EVENT_NAME";
    }

    public class RequestCode {
        public static final int CREATE = 500;
        public static final int EDIT = 501;
    }

    private EventFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = getIntent();
        final long id = intent.getLongExtra(Extra.EVENT_ID, 0);

        fragment = EventFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_event, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_save:
                fragment.save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
