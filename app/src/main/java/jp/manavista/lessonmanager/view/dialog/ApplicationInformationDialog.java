/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import jp.manavista.lessonmanager.R;

/**
 *
 * Application Information Dialog
 *
 * <p>
 * Overview:<br>
 * Define a dialog to display application information.
 * A display name, version information, link to privacy policy, etc.
 * </p>
 */
public final class ApplicationInformationDialog extends DialogFragment {

    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_information, rootView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok,null);
        builder.setNeutralButton(R.string.label_application_info_privacy_policy,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            final Button policyButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_NEUTRAL);
            policyButton.setOnClickListener(buttonView -> {

                final String label = policyButton.getText().toString();
                final LinearLayout layout = view.findViewById(R.id.application_info);

                if( Objects.equals(getString(R.string.label_application_info_privacy_policy), label) ) {

                    layout.removeAllViews();
                    inflater.inflate(R.layout.dialog_privacy_policy, layout);

                    final String contents = getPrivacyPolicyContents();
                    if( StringUtils.isNotEmpty(contents) ){
                        TextView textView = view.findViewById(R.id.policy_contents);
                        textView.setText(contents);
                        policyButton.setText(R.string.label_application_info_back);
                    }

                } else if( Objects.equals(getString(R.string.label_application_info_back), label) ) {
                    layout.removeAllViews();
                    inflater.inflate(R.layout.dialog_information, layout);
                    policyButton.setText(R.string.label_application_info_privacy_policy);
                }
            });
        });

        return dialog;
    }

    /**
     *
     * Get Policy Contents
     *
     * <p>
     * Overview:<br>
     * Get the contents of the privacy policy from the text file to be placed in the raw resource.
     * See res/raw/privacy_policy.txt
     * </p>
     *
     * @return Policy Contents String
     */
    private String getPrivacyPolicyContents() {

        final InputStream stream = getResources().openRawResource(R.raw.plivacy_policy);
        final StringBuilder sb = new StringBuilder();
        String str;

        try( BufferedReader reader = new BufferedReader(new InputStreamReader(stream)) ) {
            while( (str = reader.readLine()) != null ){
                sb.append(str).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
