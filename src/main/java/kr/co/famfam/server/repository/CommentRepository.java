package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentsByContentIdxOrderByCreatedAtAsc(int contentIdx);

    long countByUserIdxAndCreatedAtBetween(int userIdx, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
