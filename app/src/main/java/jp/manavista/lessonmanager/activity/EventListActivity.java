/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.constants.analytics.ContentType;
import jp.manavista.lessonmanager.constants.analytics.Event;
import jp.manavista.lessonmanager.constants.analytics.Param;
import jp.manavista.lessonmanager.fragment.EventListFragment;
import jp.manavista.lessonmanager.view.section.FilterableSection;
import lombok.val;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.CONTENT_TYPE;
import static jp.manavista.lessonmanager.activity.SettingActivity.FragmentType.EVENT;

public class EventListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /** fragment */
    private EventListFragment fragment;

    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        analytics = FirebaseAnalytics.getInstance(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Activity activity = this;

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final Intent intent = new Intent(activity, EventActivity.class);
            startActivityForResult(intent, EventActivity.RequestCode.CREATE);
        });

        toolbar.setNavigationOnClickListener(view -> finish());

        fragment = EventListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_event_list, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_event_list, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_setting:
                final Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra(SettingActivity.EXTRA_FRAGMENT_TYPE, EVENT);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {

        final Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TYPE, ContentType.Event.label());

        val adapter = fragment.getAdapter();
        for( Section section : adapter.getSectionsMap().values() ) {
            if( section instanceof FilterableSection) {
                ((FilterableSection) section).filter(query);
                bundle.putString(Param.Value.label(), query);
                analytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
            }
        }
        adapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == EventActivity.RequestCode.CREATE && resultCode == RESULT_OK ) {
            final String name = data.getStringExtra(EventActivity.Extra.EVENT_NAME);
            final String message = getString(R.string.message_event_list_add_event, name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            final Bundle bundle = new Bundle();
            bundle.putString(CONTENT_TYPE, ContentType.Event.label());
            analytics.logEvent(Event.Add.label(), bundle);

        } else if( requestCode == EventActivity.RequestCode.EDIT && resultCode == RESULT_OK ) {
            final String name = data.getStringExtra(EventActivity.Extra.EVENT_NAME);
            final String message = getString(R.string.message_event_list_edit_event, name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            final Bundle bundle = new Bundle();
            bundle.putString(CONTENT_TYPE, ContentType.Event.label());
            analytics.logEvent(Event.Edit.label(), bundle);
        }
    }
}
