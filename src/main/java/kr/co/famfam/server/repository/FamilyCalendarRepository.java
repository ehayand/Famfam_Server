package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.FamilyCalendar;
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
public interface FamilyCalendarRepository extends JpaRepository<FamilyCalendar, Integer> {

    @Query("SELECT f FROM FamilyCalendar AS f WHERE f.startDate between :startDate and :endDate")
    List<FamilyCalendar> findByYearAndMonth(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate);

    @Query(value = "SELECT * FROM family_calendar WHERE all_date LIKE :dateStr", nativeQuery = true)
    List<FamilyCalendar> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr);
}
