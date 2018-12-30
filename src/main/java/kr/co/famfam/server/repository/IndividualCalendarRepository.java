package kr.co.famfam.server.repository;


import kr.co.famfam.server.domain.IndividualCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface IndividualCalendarRepository extends JpaRepository<IndividualCalendar, Integer> {

    List<IndividualCalendar> findIndividualCalendarsByStartYearAndStartMonth(int startYear, int startMonth);
    List<IndividualCalendar> findIndividualCalendarsByStartYearAndStartMonthAndStartDate(final int startYear, final int startMonth, final int startDate);
}
