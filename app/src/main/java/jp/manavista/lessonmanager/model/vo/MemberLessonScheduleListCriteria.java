package jp.manavista.lessonmanager.model.vo;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import jp.manavista.lessonmanager.view.section.MemberLessonScheduleSection;
import jp.manavista.lessonmanager.view.section.MemberLessonSection;
import lombok.Builder;
import lombok.Value;
import lombok.val;

/**
 *
 * MemberLessonSchedule List Criteria
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
@Builder
@Value
public final class MemberLessonScheduleListCriteria implements Serializable {

    private long memberId;
    private Boolean containPastLesson;
    private Set<String> scheduleStatusSet;

    private MemberLessonSection lessonSection;
    private MemberLessonScheduleSection scheduleSection;

    private RecyclerView view;
    private ViewGroup emptyState;

    /** MemberLesson RecyclerView Adapter */
    private SectionedRecyclerViewAdapter sectionAdapter;


    public Set<Integer> getScheduleStatusIntegerSet() {

        final Set<Integer> set = new HashSet<>();
        for( val status : getScheduleStatusSet() ) {
            set.add(Integer.valueOf(status));
        }
        return set;
    }
}
