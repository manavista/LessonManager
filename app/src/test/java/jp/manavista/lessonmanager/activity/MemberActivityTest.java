/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.activity;

import android.os.Build;
import android.widget.EditText;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import jp.manavista.lessonmanager.BuildConfig;
import jp.manavista.lessonmanager.R;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

/**
 *
 * MemberActivity Test
 *
 * <p>
 * Overview:<br>
 * Unit Test of {@link MemberActivity}.<br>
 * Input item validation.
 * </p>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O)
public class MemberActivityTest {

    @Test
    public void saveEmpty() throws Exception {

        MemberActivity activity  = Robolectric.setupActivity(MemberActivity.class);
        activity.findViewById(R.id.option_save).performClick();
        EditText givenName = activity.findViewById(R.id.givenNameEditText);
        EditText familyName = activity.findViewById(R.id.familyNameEditText);

        Assert.assertThat(givenName.getError().toString(), is(not(isEmptyOrNullString())));
        Assert.assertThat(familyName.getError().toString(), is(not(isEmptyOrNullString())));
    }
}
