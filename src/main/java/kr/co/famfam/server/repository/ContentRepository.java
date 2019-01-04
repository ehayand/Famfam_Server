package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    Page<Content> findContentsByGroupIdx(int groupIdx, Pageable pageable);

    Page<Content> findContentsByUserIdx(int userIdx, Pageable pageable);

    long countByGroupIdxAndCreatedDateBetween(int groupIdx, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
