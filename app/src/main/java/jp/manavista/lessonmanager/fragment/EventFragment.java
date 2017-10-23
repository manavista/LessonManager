/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.EventActivity;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.EventDto;
import jp.manavista.lessonmanager.model.entity.Event;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.util.DateTimeUtil;

import static jp.manavista.lessonmanager.util.DateTimeUtil.DATE_PATTERN_YYYYMMDD;

/**
 *
 * Event Fragment
 *
 *
 */
public final class EventFragment extends Fragment implements Validator.ValidationListener {

    /** bundle key: member id */
    public static final String KEY_EVENT_ID = "EVENT_ID";
    /** Preview Tag string */
    private static final int TAG_PREVIEW_TEXT = R.string.tag_event_preview_color;

    /** member id */
    private long eventId;
    /** Activity Contents */
    private Activity contents;
    /** DTO */
    private EventDto dto;

    /** input validator */
    private Validator validator;
    /** Member disposable */
    private Disposable disposable;

    @Inject
    EventService service;


    public EventFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventFragment.
     */
    public static EventFragment newInstance(final long eventId) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getLong(KEY_EVENT_ID);
        }
        disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        dto = EventDto.builder()
                .name((EditText) rootView.findViewById(R.id.event_name_edit_text))
                .date((EditText) rootView.findViewById(R.id.event_date_edit_text))
                .memo((EditText) rootView.findViewById(R.id.event_memo_edit_text))
                .previewText((TextView) rootView.findViewById(R.id.preview_text))
                .eventDateImageButton((ImageButton) rootView.findViewById(R.id.event_date_image_button))
                .textColorImageButton((ImageButton) rootView.findViewById(R.id.text_color_image_button))
                .backgroundColorImageButton((ImageButton) rootView.findViewById(R.id.background_color_image_button))

                .dateFormat("%04d/%02d/%02d")

                .build();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.contents = getActivity();

        DependencyInjector.appComponent().inject(this);

        validator = new Validator(dto);
        validator.setValidationListener(this);

        prepareButtonListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        if( eventId > 0 ) {
            storeEntity(eventId);
        } else {
            storeInitValue();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    @Override
    public void onValidationSucceeded() {
        disposable = service.save(dto.toEntity()).subscribe(new Consumer<Event>() {
            @Override
            public void accept(Event event) throws Exception {

                final Intent intent = new Intent();
                intent.putExtra(EventActivity.Extra.EVENT_NAME, event.name);
                contents.setResult(Activity.RESULT_OK, intent);
                contents.finish();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for( ValidationError error : errors ) {

            final String message = error.getCollatedErrorMessage(contents);
            final View view = error.getView();

            if( view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(contents, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void save() {
        validator.validate();
    }

    /**
     *
     * Store Initial Value to DTO
     *
     * <p>
     * Overview:<br>
     * Initial value is set to the screen element to be edited
     * when new event is created.
     * </p>
     */
    private void storeInitValue() {
        final Calendar today = DateTimeUtil.today();
        dto.getDate().setText(DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, today));

        /* Color (default text: @color/black, background: @color/amber_500) */
        final int textColor = dto.getPreviewText().getCurrentTextColor();
        final int backgroundColor = ((ColorDrawable) dto.getPreviewText().getBackground()).getColor();

        dto.setTextColor(textColor);
        dto.getTextColorImageButton().setTag(TAG_PREVIEW_TEXT, textColor);

        dto.setBackgroundColor(backgroundColor);
        dto.getBackgroundColorImageButton().setTag(TAG_PREVIEW_TEXT, backgroundColor);
    }

    private void storeEntity(final long id) {
        disposable = service.getById(id).subscribe(new Consumer<Event>() {
            @Override
            public void accept(Event entity) throws Exception {
                dto.store(entity);
            }
        });
    }

    /**
     *
     * Prepare ImageButton Listener
     *
     * <p>
     * Overview:<br>
     * Define and set the event when ImageButton is clicked.
     * </p>
     */
    private void prepareButtonListener() {

        dto.getDate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText date = (EditText) view;
                String[] dates;
                if( StringUtils.isEmpty(date.getText().toString()) ) {
                    final Calendar today = DateTimeUtil.today();
                    dates = DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, today).split(DateTimeUtil.SLASH);
                } else {
                    dates = String.valueOf(date.getText()).split(DateTimeUtil.SLASH);
                }
                final int month = Integer.valueOf(dates[1]) - 1;
                new DatePickerDialog(contents, dto.dateSetListener,
                        Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
            }
        });

        dto.getEventDateImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText date = dto.getDate();
                String[] dates;
                if( StringUtils.isEmpty(date.getText().toString()) ) {
                    final Calendar today = DateTimeUtil.today();
                    dates = DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, today).split(DateTimeUtil.SLASH);
                } else {
                    dates = String.valueOf(date.getText()).split(DateTimeUtil.SLASH);
                }

                final int month = Integer.valueOf(dates[1]) - 1;
                new DatePickerDialog(contents, dto.dateSetListener,
                        Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
            }
        });

        dto.getTextColorImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final ImageButton textColorButton = (ImageButton) view;
                final int textColor = textColorButton.getTag(TAG_PREVIEW_TEXT) == null
                        ? ContextCompat.getColor(contents, R.color.black)
                        : (int) textColorButton.getTag(TAG_PREVIEW_TEXT);

                new SpectrumDialog.Builder(contents)
                        .setColors(R.array.color_picker_target)
                        .setSelectedColor(textColor)
                        .setOutlineWidth(1)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                dto.setTextColor(color);
                                dto.getPreviewText().setTextColor(color);
                                textColorButton.setTag(TAG_PREVIEW_TEXT, color);
                            }
                        }).build().show(getFragmentManager(), "text_color_select_dialog");
            }
        });

        dto.getBackgroundColorImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ImageButton backgroundColorButton = (ImageButton) view;
                final int backgroundColor = backgroundColorButton.getTag(TAG_PREVIEW_TEXT) == null
                        ? ContextCompat.getColor(contents, R.color.amber_500)
                        : (int) backgroundColorButton.getTag(TAG_PREVIEW_TEXT);

                new SpectrumDialog.Builder(contents)
                        .setColors(R.array.color_picker_target)
                        .setSelectedColor(backgroundColor)
                        .setOutlineWidth(1)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                dto.setBackgroundColor(color);
                                dto.getPreviewText().setBackgroundColor(color);
                                backgroundColorButton.setTag(TAG_PREVIEW_TEXT, color);
                            }
                        }).build().show(getFragmentManager(), "background_color_select_dialog");
            }
        });

    }
}
