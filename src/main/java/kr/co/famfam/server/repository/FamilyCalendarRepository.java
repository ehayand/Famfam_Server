package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.FamilyCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface FamilyCalendarRepository extends JpaRepository<FamilyCalendar, Integer> {

    List<FamilyCalendar> findFamilyCalendarsByStartYearAndStartMonth(final int startYear, final int startMonth);
    List<FamilyCalendar> findFamilyCalendarsByStartYearAndStartMonthAndStartDate(final int startYear, final int startMonth, final int startDate);
}
