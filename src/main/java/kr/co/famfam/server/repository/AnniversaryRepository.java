package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Anniversary;
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
public interface AnniversaryRepository extends JpaRepository<Anniversary, Integer> {

    @Query("SELECT a FROM anniversary a WHERE a.date > (:dateStr - INTERVAL 2 MONTH)")
    List<Anniversary> findByYearAndMonth(@Param("dateStr") final String dateStr);

    @Query("SELECT a FROM anniversary a WHERE a.date LIKE :dateStr%")
    List<Anniversary> findByYearAndMonthAndDate(@Param("dateStr") final String dateStr);

    List<Anniversary> findAnniversariesByGroupIdx(final int groupIdx);
}
