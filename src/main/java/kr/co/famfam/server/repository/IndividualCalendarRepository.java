package kr.co.famfam.server.repository;


import kr.co.famfam.server.domain.IndividualCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public interface IndividualCalendarRepository extends JpaRepository<IndividualCalendar, Integer> {

    @Query("SELECT i FROM IndividualCalendar AS i WHERE i.startDate between :startDate and :endDate")
    List<IndividualCalendar> findByYearAndMonth(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate);

    @Query(value = "SELECT * FROM Individual_calendar WHERE all_date LIKE :dateStr", nativeQuery = true)
    List<IndividualCalendar> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr);

    @Query(value = "SELECT * FROM Individual_calendar WHERE content LIKE :content", nativeQuery = true)
    List<IndividualCalendar> findByContetnt(@Param("content") final String content);

}
