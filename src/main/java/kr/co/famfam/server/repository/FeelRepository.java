package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Feel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface FeelRepository extends JpaRepository<Feel, Integer> {

    Optional<Feel> findFeelByContentIdxAndUserIdx(int contentIdx, int userIdx);

    List<Feel> findFeelsByContentIdxOrderByCreatedDateAsc(int contentIdx);

    long countByUserIdxAndCreatedDateBetween(int userIdx, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
