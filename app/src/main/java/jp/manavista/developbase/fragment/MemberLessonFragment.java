package jp.manavista.developbase.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.developbase.R;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.model.dto.MemberDto;
import jp.manavista.developbase.model.dto.MemberLessonFragmentDto;
import jp.manavista.developbase.model.dto.TimetableDto;
import jp.manavista.developbase.model.entity.Member;
import jp.manavista.developbase.model.entity.MemberLesson;
import jp.manavista.developbase.model.entity.Timetable;
import jp.manavista.developbase.service.MemberLessonService;
import jp.manavista.developbase.service.MemberService;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.util.ArrayUtil;
import jp.manavista.developbase.util.DateTimeUtil;

import static jp.manavista.developbase.util.DateTimeUtil.DATE_PATTERN_YYYYMMDD;

/**
 *
 * MemberLesson Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public final class MemberLessonFragment extends Fragment implements Validator.ValidationListener {

    /** Logger tag string */
    public static final String TAG = MemberLessonFragment.class.getSimpleName();
    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";
    /** bundle key: member lesson id */
    public static final String KEY_MEMBER_LESSON_ID = "MEMBER_LESSON_ID";

    /** member id */
    private int memberId;
    /** member lesson id */
    private int id;
    /** DTO */
    private MemberLessonFragmentDto dto;
    /** Activity Contents */
    private Activity contents;

    @Inject
    MemberService memberService;
    @Inject
    MemberLessonService memberLessonService;
    @Inject
    TimetableService timetableService;

    /** input validator */
    private Validator validator;
    /** Member disposable */
    private Disposable disposable;

    /** Constructor */
    public MemberLessonFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param memberId display member id
     * @return A new instance of fragment MemberLessonFragment.
     */
    public static MemberLessonFragment newInstance(final int memberId, final int memberLessonId) {
        MemberLessonFragment fragment = new MemberLessonFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_MEMBER_ID, memberId);
        args.putInt(KEY_MEMBER_LESSON_ID, memberLessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            memberId = args.getInt(KEY_MEMBER_ID);
            id = args.getInt(KEY_MEMBER_LESSON_ID);
        }

        this.disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_member_lesson, container, false);

        dto = MemberLessonFragmentDto.builder()
                .id(id)
                .memberId(memberId)
                .memberName((TextView) rootView.findViewById(R.id.member_name))
                .name((EditText) rootView.findViewById(R.id.name))
                .abbr((EditText) rootView.findViewById(R.id.abbr))
                .type((EditText) rootView.findViewById(R.id.type))
                .location((EditText) rootView.findViewById(R.id.location))
                .presenter((EditText) rootView.findViewById(R.id.presenter))
                .startTimeText((EditText) rootView.findViewById(R.id.start_time))
                .endTimeText((EditText) rootView.findViewById(R.id.end_time))
                .timetableIcon((ImageButton) rootView.findViewById(R.id.timetable_image_button))
                .dayOfWeek((EditText) rootView.findViewById(R.id.day_of_week))
                .startPeriod((EditText) rootView.findViewById(R.id.start_period))
                .endPeriod((EditText) rootView.findViewById(R.id.end_period))
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
        this.contents = getActivity();

        DependencyInjector.appComponent().inject(this);

        validator = new Validator(dto);
        validator.setValidationListener(this);

        if( id > 0 ) {
            storeEntityToDto(id);
        } else {
            storeInitValueToDto();
        }

        displayMemberName();
        prepareButtonListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.disposable.dispose();
    }

    @Override
    public void onValidationSucceeded() {

        disposable = memberLessonService.save(dto.convert()).subscribe(new Consumer<MemberLesson>() {
            @Override
            public void accept(MemberLesson memberLesson) throws Exception {
                Log.d(TAG, memberLesson.toString());
                contents.finish();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException("can not save MemberLesson entity", throwable);
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
     * Save Member Lesson
     *
     * <p>
     * Overview:<br>
     * Save the information on the member lesson you entered on the screen.<br>
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

        dto.getDayOfWeek().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* full name: Sunday, Monday, Tuesday... */
                final String[] daysFull = getResources().getStringArray(R.array.entries_day_of_week_full_name);
                /* short name: Sun, Mon, Tue, Wed... */
                final String[] days = getResources().getStringArray(R.array.entries_day_of_week);
                /* day decimal string value: 1, 2, 3... */
                final String[] dayValues = getResources().getStringArray(R.array.entry_values_day_of_week);

                final String dayOfWeek = dto.getDayOfWeekValue();
                final boolean[] index = ArrayUtil.convertIndexFromArray(dayOfWeek, dayValues, ",");

                AlertDialog.Builder builder = new AlertDialog.Builder(contents);
                builder.setTitle(R.string.label_member_lesson_day_of_week_dialog_title)
                        .setIcon(R.drawable.ic_event_black)
                        .setMultiChoiceItems(daysFull, index, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                index[which] = isChecked;
                                ((AlertDialog) dialogInterface)
                                        .getButton(AlertDialog.BUTTON_POSITIVE)
                                        .setEnabled(ArrayUtils.contains(index, true));
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String label = ArrayUtil.concatIndexOfArray(days, index, ", ");
                                final String dayOfWeekValue = ArrayUtil.concatIndexOfArray(dayValues, index, ",");
                                dto.getDayOfWeek().setText(label);
                                dto.setDayOfWeekValue(dayOfWeekValue);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        dto.getStartPeriod().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText startPeriod = (EditText) view;
                final String[] dates = String.valueOf(startPeriod.getText()).split(DateTimeUtil.SLASH);
                final int month = Integer.valueOf(dates[1]) - 1;
                new DatePickerDialog(contents, dto.startPeriodSetListener,
                        Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
            }
        });

        dto.getEndPeriod().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText endPeriod = (EditText) view;
                final String[] dates = String.valueOf(endPeriod.getText()).split(DateTimeUtil.SLASH);
                final int month = Integer.valueOf(dates[1]) - 1;
                new DatePickerDialog(contents, dto.endPeriodSetListener,
                        Integer.valueOf(dates[0]), month, Integer.valueOf(dates[2])).show();
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
    }

    /**
     *
     * Display Member name
     *
     * <p>
     * Overview:<br>
     * Obtain the display name of the member associated with the id specified in the argument
     * and display it on the screen.
     * </p>
     */
    private void displayMemberName() {

        disposable = memberService.getById(memberId).subscribe(new Consumer<Member>() {
            @Override
            public void accept(Member member) throws Exception {
                final String displayName = MemberDto.copy(member).getDisplayName();
                dto.getMemberName().setText(displayName);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException("can not get member entity", throwable);
            }
        });
    }

    /**
     *
     * Store Initial Value to DTO
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     *
     */
    private void storeInitValueToDto() {

        /* dayOfWeek (default: Monday) */
        final String dayOfWeek = "2";
        final String[] dayOfWeeks = getResources().getStringArray(R.array.entries_day_of_week);

        dto.setDayOfWeekValue(dayOfWeek);
        dto.getDayOfWeek().setText(dayOfWeeks[1]);

        /* Period */
        final Calendar calendar = DateTimeUtil.today();
        dto.getStartPeriod().setText(DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, calendar));
        calendar.add(Calendar.MONTH, 3);
        dto.getEndPeriod().setText(DateTimeUtil.format(DATE_PATTERN_YYYYMMDD, calendar));

        /* Color (default text: @color/black, background: @color/amber_500) */
        final int textColor = dto.getPreviewText().getCurrentTextColor();
        final int backgroundColor = ((ColorDrawable) dto.getPreviewText().getBackground()).getColor();
        final int tag = R.string.tag_member_lesson_preview_color;

        dto.setTextColor(textColor);
        dto.getTextColorImageButton().setTag(tag, textColor);

        dto.setBackgroundColor(backgroundColor);
        dto.getBackgroundColorImageButton().setTag(tag, backgroundColor);
    }

    /**
     *
     * Store Entity to DTO
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     * 
     * @param memberLessonId member lesson id
     */
    private void storeEntityToDto(final int memberLessonId) {
        // TODO: 2017/08/17 Get MemberLesson entity and copy dto
    }
}
