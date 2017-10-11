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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.MemberListFragment;
import jp.manavista.lessonmanager.view.section.FilterableSection;
import lombok.val;

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
public class MemberListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /** Logger tag string */
    private static final String TAG = MemberListActivity.class.getSimpleName();

    /** MemberList fragment */
    private MemberListFragment memberListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Activity activity = this;

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(activity, MemberActivity.class);
                startActivityForResult(intent, MemberActivity.RequestCode.CREATE);
            }
        });

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

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == MemberActivity.RequestCode.CREATE && resultCode == RESULT_OK ) {

            final String name = data.getStringExtra(MemberActivity.EXTRA_MEMBER_NAME_DISPLAY);
            final String message = getString(R.string.message_member_list_add_member, name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        } else if( requestCode == MemberActivity.RequestCode.EDIT && resultCode == RESULT_OK ) {

            final String name = data.getStringExtra(MemberActivity.EXTRA_MEMBER_NAME_DISPLAY);
            final String message = getString(R.string.message_member_list_edit_member, name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            default:
                Log.d(TAG, "Selected undefine item Id.");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {

        val adapter = memberListFragment.getSectionAdapter();
        for( Section section : adapter.getSectionsMap().values() ) {
            if( section instanceof FilterableSection ) {
                ((FilterableSection) section).filter(query);
            }
        }
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
