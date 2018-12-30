package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.FamilyCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public interface FamilyCalendarRepository extends JpaRepository<FamilyCalendar, Integer> {
//
//    @Query("SELECT f FROM familyCalendar f WHERE f.startDate > (:dateStr - INTERVAL 2 MONTH)")
//    List<FamilyCalendar> findByYearAndMonth(@Param("dateStr") final String dateStr);
//
//    @Query("SELECT f FROM familyCalendar f WHERE f.allDate LIKE %:dateStr%")
//    List<FamilyCalendar> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr);
}
