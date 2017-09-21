package jp.manavista.lessonmanager.model.vo;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import lombok.Builder;
import lombok.Value;
import lombok.val;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Builder
@Value
public final class MemberLessonScheduleListCriteria implements Serializable {

    private long memberId;
    private Boolean containPastLesson;
    private Set<String> scheduleStatusSet;

    private List<MemberLessonVo> lessonVoList;
    private List<MemberLessonScheduleVo> scheduleVoList;

    private RecyclerView view;
    private FrameLayout emptyState;

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
