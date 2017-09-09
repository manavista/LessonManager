package jp.manavista.lessonmanager.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.MemberFragment;
import jp.manavista.lessonmanager.util.DateTimeUtil;

public class MemberActivity extends AppCompatActivity {

    /** activity put extra argument: member id */
    public static final String EXTRA_MEMBER_ID = "MEMBER_ID";
    private static final int PICK_CONTACT_REQUEST = 1;

    /** Identifier for the permission request */
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    /** MemberFragment */
    private MemberFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = getIntent();
        final long id = intent.getLongExtra(EXTRA_MEMBER_ID, 0);

        fragment = MemberFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.option_save:
                fragment.saveMember();
                break;
            case R.id.option_import_contact:
                // In an actual app, you'd want to request a permission when the user performs an action
                // that requires that permission.
                getPermissionToReadUserContacts();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == PICK_CONTACT_REQUEST ) {
            if( resultCode == RESULT_OK ) {
                retrieveContact(data.getData());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if ( requestCode == READ_CONTACTS_PERMISSIONS_REQUEST ) {

            if ( grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                startContactActivity();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void retrieveContact(@NonNull final Uri uri) {

        Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if ( cursor != null && cursor.moveToFirst() ) {

            final String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            cursor.close();

            /* Structure Name */
            retrieveStructureName(contactId);
            /* Nick Name */
            retrieveNickName(contactId);
            /* Birthday */
            retrieveEvent(contactId);
            /* Phone Number */
            retrievePhoneNumber(contactId);
            /* Email Address */
            retrieveEmailAddress(contactId);
        }
    }

    private void retrieveStructureName(final String contactId) {

        final String where = ContactsContract.Data.MIMETYPE + " = ? AND " + StructuredName.CONTACT_ID + " = ?";
        final String[] params = new String[] { StructuredName.CONTENT_ITEM_TYPE, contactId };

        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        if ( cursor != null && cursor.moveToFirst() ) {
            String given = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME));
            String additional = cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME));
            String display = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));

            Log.d("contact", "given:" + given + " family:" + family + " display:" + display);

            fragment.getDto().getGivenName().setText(given);
            fragment.getDto().getAdditionalName().setText(additional);
            fragment.getDto().getFamilyName().setText(family);

            cursor.close();
        }
    }

    private void retrieveNickName(final String contactId) {

        final String where = ContactsContract.Data.MIMETYPE + " = ? AND " + Nickname.CONTACT_ID + " = ?";
        final String[] params = new String[] {Nickname.CONTENT_ITEM_TYPE, contactId };
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        if ( cursor != null && cursor.moveToFirst() ) {
            String name = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
            fragment.getDto().getNickName().setText(name);
            cursor.close();
        }
    }

    private void retrieveEvent(final String contactId) {

        final String where = ContactsContract.Data.MIMETYPE + " = ? AND "
                + Event.TYPE + " = " + Event.TYPE_BIRTHDAY + " AND "
                + Event.CONTACT_ID + " = ?";
        final String[] params = new String[] {Event.CONTENT_ITEM_TYPE, contactId };
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        if ( cursor != null && cursor.moveToFirst() ) {
            String birthday = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
            try {
                /* Convert format YYYY-MM-DD to YYYY/MM/DD */
                Date birthdayDate = DateTimeUtil.DATE_FORMAT_YYYYMMDD_DIGITS.parse(StringUtils.getDigits(birthday));
                String birthdayText = DateTimeUtil.DATE_FORMAT_YYYYMMDD.format(birthdayDate);
                fragment.getDto().getBirthday().setText(birthdayText);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
    }

    private void retrievePhoneNumber(final String contactId) {

        final String where = ContactsContract.Data.MIMETYPE + " = ? AND " + Phone.CONTACT_ID + " = ?";
        final String[] params = new String[] {Phone.CONTENT_ITEM_TYPE, contactId };
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        if( cursor != null ) {

            final List<String> numberList = new ArrayList<>();

            while ( cursor.moveToNext() ) {
                String number = cursor.getString(cursor.getColumnIndex(Phone.DATA));
                if( StringUtils.isNotEmpty(number) ) {
                    numberList.add(number);
                }
            }
            cursor.close();

            if( numberList.size() == 1 ) {
                fragment.getDto().getPhoneNumber().setText(numberList.get(0));
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.label_member_phone_dialog))
                        .setItems(numberList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                fragment.getDto().getPhoneNumber().setText(numberList.get(which));
                            }
                        })
                        .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fragment.getDto().getPhoneNumber().setText(StringUtils.EMPTY);
                            }
                        })
                        .show();
            }
        }
    }

    private void retrieveEmailAddress(final String contactId) {

        final String where = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        final String[] params = new String[] {ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, contactId };
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        if( cursor != null ) {

            final List<String> addressList = new ArrayList<>();

            while ( cursor.moveToNext() ) {
                String address = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                if( StringUtils.isNotEmpty(address) ) {
                    addressList.add(address);
                }
            }
            cursor.close();

            if( addressList.size() == 1 ) {
                fragment.getDto().getEmail().setText(addressList.get(0));
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.label_member_email_dialog))
                        .setItems(addressList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                fragment.getDto().getEmail().setText(addressList.get(which));
                            }
                        })
                        .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fragment.getDto().getEmail().setText(StringUtils.EMPTY);
                            }
                        })
                        .show();
            }
        }
    }

    private void startContactActivity() {
        final Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if( intent.resolveActivity(getPackageManager()) != null ) {
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        }
    }

    /**
     *
     * Get Permission
     *
     * <p>
     * Overview:<br>
     * Called when the user is performing an action which requires the app to read the
     * user's contacts
     * </p>
     */
    private void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        } else {
            startContactActivity();
        }
    }
}
