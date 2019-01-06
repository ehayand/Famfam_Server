package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.FamilyCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface FamilyCalendarRepository extends JpaRepository<FamilyCalendar, Integer> {

    @Query(value = "SELECT f.* FROM family_calendar f JOIN user u ON u.group_idx = :groupIdx WHERE (u.user_idx = f.user_idx) AND (f.start_date between :startDate and :endDate)", nativeQuery = true)
    List<FamilyCalendar> findByYearAndMonth(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate, @Param("groupIdx") final int groupIdx);
//    List<FamilyCalendar> findFamilyCalendarsByStartDateBetween(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate);

    @Query(value = "SELECT f.* FROM family_calendar f JOIN user u ON u.group_idx = :groupIdx WHERE (u.user_idx = f.user_idx) AND (f.all_date LIKE :dateStr)", nativeQuery = true)
    List<FamilyCalendar> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr, @Param("groupIdx") final int groupIdx);
//    List<FamilyCalendar> findFamilyCalendarsByAllDateLike(@Param("dateStr") final String dateStr);

    @Query(value = "SELECT f.* FROM family_calendar f JOIN user u ON u.group_idx = :groupIdx WHERE (u.user_idx = f.user_idx) AND (f.content LIKE :content)", nativeQuery = true)
    List<FamilyCalendar> findByContetnt(@Param("content") final String content, @Param("groupIdx") final int groupIdx);
//    List<FamilyCalendar> findFamilyCalendarsByContentLike(@Param("content") final String content);

}
