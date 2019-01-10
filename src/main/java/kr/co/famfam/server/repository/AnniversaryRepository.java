package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Anniversary;
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
public interface AnniversaryRepository extends JpaRepository<Anniversary, Integer> {

    @Query(value = "SELECT * FROM anniversary WHERE (group_idx = :groupIdx) AND (date between :startDate and :endDate)", nativeQuery = true)
    List<Anniversary> findByYearAndMonth(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate, @Param("groupIdx") final int groupIdx);
//    List<Anniversary> findAnniversariesByDateBetween(@Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate);

    @Query(value = "SELECT * FROM anniversary WHERE (group_idx = :groupIdx) AND (date LIKE :dateStr)", nativeQuery = true)
    List<Anniversary> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr, @Param("groupIdx") final int groupIdx);
//    List<Anniversary> findAnniversariesByDateLike(@Param("dateStr") final LocalDateTime dateStr);

    @Query(value = "SELECT * FROM anniversary WHERE (group_idx = :groupIdx) AND (content LIKE :content)", nativeQuery = true)
    List<Anniversary> findByContent(@Param("content") final String content, @Param("groupIdx") final int groupIdx);
//    List<Anniversary> findAnniversariesByContentLike(@Param("content") final String content);

    List<Anniversary> findAnniversariesByGroupIdx(final int groupIdx);

    List<Anniversary> findAnniversariesByDate(final LocalDateTime Date);
}
