package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findCommentsByContentIdxOrderByCreatedDateAsc(int contentIdx);
}
