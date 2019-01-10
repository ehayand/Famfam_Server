package kr.co.famfam.server.repository;


import kr.co.famfam.server.domain.IndividualCalendar;
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
public interface
IndividualCalendarRepository extends JpaRepository<IndividualCalendar, Integer> {

    @Query(value = "SELECT i.* FROM individual_calendar i JOIN user u ON u.group_idx = :groupIdx WHERE (u.user_idx = i.user_idx) AND (i.start_date between :startDate and :endDate)", nativeQuery = true)
    List<IndividualCalendar> findByYearAndMonth(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate, @Param("groupIdx") final int groupIdx);
//    List<IndividualCalendar> findIndividualCalendarsByStartDateBetween(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate);

    @Query(value = "SELECT i.* FROM individual_calendar i JOIN user u ON u.group_idx = :groupIdx WHERE (u.user_idx = i.user_idx) AND (i.all_date LIKE :dateStr)", nativeQuery = true)
    List<IndividualCalendar> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr, @Param("groupIdx") final int groupIdx);
//    List<IndividualCalendar> findIndividualCalendarsByAllDateLike(@Param("dateStr") final String dateStr);

    @Query(value = "SELECT i.* FROM individual_calendar i JOIN user u ON u.group_idx = :groupIdx WHERE (u.user_idx = i.user_idx) AND (i.content LIKE :content)", nativeQuery = true)
    List<IndividualCalendar> findByContetnt(@Param("content") final String content, @Param("groupIdx") final int groupIdx);
//    List<IndividualCalendar> findIndividualCalendarsByContentLike(@Param("content") final String content);
}
