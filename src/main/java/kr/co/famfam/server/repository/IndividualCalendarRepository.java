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
//
//    @Query("SELECT i FROM individualCalendar i WHERE i.startDate > (:dateStr - INTERVAL 2 MONTH)")
//    List<IndividualCalendar> findByYearAndMonth(@Param("dateStr") final String dateStr);
//
//    @Query("SELECT i FROM individualCalendar i WHERE i.allDate LIKE %:dateStr%")
//    List<IndividualCalendar> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr);
}
