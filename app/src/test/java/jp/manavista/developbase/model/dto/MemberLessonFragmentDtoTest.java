package jp.manavista.developbase.model.dto;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class MemberLessonFragmentDtoTest {

    @Before
    public void setUp() {

    }

    @Test
    public void rangeTimetableListener() {

        MemberLessonFragmentDto dto = MemberLessonFragmentDto.builder().build();
        List<TimetableDto> list = new ArrayList<>();

        list.add(new TimetableDto());
        list.add(new TimetableDto());
        list.add(new TimetableDto());

        final int expected = list.size();

        assertThat(expected, is(3));

//        dto.setTimetableDtoList(list);
//        dto.timetableSetLister.onClick(null, 2);
    }


}
