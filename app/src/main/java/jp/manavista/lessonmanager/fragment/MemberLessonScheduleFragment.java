package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.MemberLessonScheduleDto;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.util.DateTimeUtil;

/**
 *
 * Member Lesson Schedule Fragment
 *
 * <p>
 * Overview:<br>
 * In {@code MemberLessonSchedule}, define processing such as display of details,
 * editing and saving.
 * </p>
 */
public final class MemberLessonScheduleFragment extends Fragment implements Validator.ValidationListener {

    private static final String KEY_LESSON_ID = "LESSON_ID";

    private long scheduleId;

    /** Data transfer object */
    private MemberLessonScheduleDto dto;
    /** Activity Contents */
    private Activity contents;
    /** input validator */
    private Validator validator;
    /** MemberLessonSchedule disposable */
    private Disposable disposable;

    @Inject
    MemberLessonScheduleService memberLessonScheduleService;
    @Inject
    TimetableService timetableService;

    public MemberLessonScheduleFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param scheduleId MemberLessonSchedule Id
     * @return A new instance of fragment MemberLessonScheduleFragment.
     */
    public static MemberLessonScheduleFragment newInstance(final long scheduleId) {
        final MemberLessonScheduleFragment fragment = new MemberLessonScheduleFragment();
        final Bundle args = new Bundle();
        args.putLong(KEY_LESSON_ID, scheduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            scheduleId = bundle.getLong(KEY_LESSON_ID);
        }
        disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_member_lesson_schedule, container, false);

        dto = MemberLessonScheduleDto.builder()
                .id(scheduleId)
                .statusRadioGroup((RadioGroup) rootView.findViewById(R.id.statusRadioGroup))
                .statusScheduled((RadioButton) rootView.findViewById(R.id.scheduledRadioButton))
                .statusDone((RadioButton) rootView.findViewById(R.id.doneRadioButton))
                .statusAbsent((RadioButton) rootView.findViewById(R.id.absentRadioButton))
                .statusSuspend((RadioButton) rootView.findViewById(R.id.suspendedRadioButton))
                .name((EditText) rootView.findViewById(R.id.name))
                .abbr((EditText) rootView.findViewById(R.id.abbr))
                .type((EditText) rootView.findViewById(R.id.type))
                .location((EditText) rootView.findViewById(R.id.location))
                .presenter((EditText) rootView.findViewById(R.id.presenter))
                .scheduleDate((EditText) rootView.findViewById(R.id.schedule_date))
                .startTimeText((EditText) rootView.findViewById(R.id.start_time))
                .endTimeText((EditText) rootView.findViewById(R.id.end_time))
                .timetableIcon((ImageButton) rootView.findViewById(R.id.timetable_image_button))
                .previewText((TextView) rootView.findViewById(R.id.preview_text))
                .textColorImageButton((ImageButton) rootView.findViewById(R.id.text_color_image_button))
                .backgroundColorImageButton((ImageButton) rootView.findViewById(R.id.background_color_image_button))

                .dateFormat("%04d/%02d/%02d")

                .build();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contents = getActivity();
        DependencyInjector.appComponent().inject(this);

        validator = new Validator(dto);
        validator.setValidationListener(this);

        prepareButtonListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        disposable = memberLessonScheduleService.getById(scheduleId).subscribe(new Consumer<MemberLessonSchedule>() {
            @Override
            public void accept(MemberLessonSchedule entity) throws Exception {
               dto.store(entity);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    @Override
    public void onValidationSucceeded() {
        disposable = memberLessonScheduleService.save(dto.convert()).subscribe(new Consumer<MemberLessonSchedule>() {
            @Override
            public void accept(MemberLessonSchedule entity) throws Exception {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for( ValidationError error : errors ) {

            final String message = error.getCollatedErrorMessage(contents);
            final View view = error.getView();

            if( view instanceof EditText ) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(contents, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * Save Member Lesson Schedule
     *
     * <p>
     * Overview:<br>
     * Save the information on the {@code MemberLessonSchedule} you entered on the screen.<br>
     * First, validate the input value. Perform processing with another
     * function according to the result.
     * </p>
     */
    public void save() {
        validator.validate();
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

        dto.getScheduleDate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText scheduleDate = (EditText) view;
                final String[] dates = String.valueOf(scheduleDate.getText()).split(DateTimeUtil.SLASH);
                final int month = Integer.valueOf(dates[1]) - 1;
                new DatePickerDialog(contents, dto.dateSetListener,
                        Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
            }
        });

        dto.getStartTimeText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText startTime = (EditText) view;
                final String[] times = String.valueOf(startTime.getText()).split(DateTimeUtil.COLON);
                new TimePickerDialog(contents, dto.startTimeSetListener,
                        Integer.valueOf(times[0]), Integer.valueOf(times[1]), false).show();
            }
        });

        dto.getEndTimeText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText endTime = (EditText) view;
                final String[] times = String.valueOf(endTime.getText()).split(DateTimeUtil.COLON);
                new TimePickerDialog(contents, dto.endTimeSetListener,
                        Integer.valueOf(times[0]), Integer.valueOf(times[1]), false).show();
            }
        });

        dto.getTimetableIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final List<String> labelList = new ArrayList<>();
                final List<TimetableDto> timetableList = new ArrayList<>();
                final StringBuilder sb = new StringBuilder();

                // TODO: 2017/09/22 If Timetable is empty, create Timetable icon and screen.

                disposable = timetableService.getListAll().subscribe(new Consumer<Timetable>() {
                    @Override
                    public void accept(Timetable timetable) throws Exception {
                        TimetableDto dto = TimetableDto.copy(timetable);
                        timetableList.add(dto);
                        sb.setLength(0);
                        sb.append(dto.getStartTimeFormatted())
                                .append(" - ")
                                .append(dto.getEndTimeFormatted());
                        labelList.add(sb.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throw new RuntimeException("can not get timetable", throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        dto.setTimetableDtoList(timetableList);
                        AlertDialog.Builder builder = new AlertDialog.Builder(contents);
                        builder.setTitle("Select a timetable")
                                .setIcon(R.drawable.ic_event_black)
                                .setItems(labelList.toArray(new CharSequence[0]), dto.timetableSetLister)
                                .show();
                    }
                });
            }
        });

        dto.getTextColorImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final ImageButton textColorButton = (ImageButton) view;
                final int textColor = textColorButton.getTag(R.string.tag_member_lesson_preview_color) == null
                        ? ContextCompat.getColor(contents, R.color.black)
                        : (int) textColorButton.getTag(R.string.tag_member_lesson_preview_color);

                new SpectrumDialog.Builder(contents)
                        .setColors(R.array.color_picker_target)
                        .setSelectedColor(textColor)
                        .setOutlineWidth(1)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                dto.setTextColor(color);
                                dto.getPreviewText().setTextColor(color);
                                textColorButton.setTag(R.string.tag_member_lesson_preview_color, color);
                            }
                        }).build().show(getFragmentManager(), "text_color_select_dialog");
            }
        });

        dto.getBackgroundColorImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ImageButton backgroundColorButton = (ImageButton) view;
                final int backgroundColor = backgroundColorButton.getTag(R.string.tag_member_lesson_preview_color) == null
                        ? ContextCompat.getColor(contents, R.color.amber_500)
                        : (int) backgroundColorButton.getTag(R.string.tag_member_lesson_preview_color);

                new SpectrumDialog.Builder(contents)
                        .setColors(R.array.color_picker_target)
                        .setSelectedColor(backgroundColor)
                        .setOutlineWidth(1)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                dto.setBackgroundColor(color);
                                dto.getPreviewText().setBackgroundColor(color);
                                backgroundColorButton.setTag(R.string.tag_member_lesson_preview_color, color);
                            }
                        }).build().show(getFragmentManager(), "background_color_select_dialog");
            }
        });

        dto.getStatusRadioGroup().setOnCheckedChangeListener(dto.statusRadioGroupListener);
    }
}
