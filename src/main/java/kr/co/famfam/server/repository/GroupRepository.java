package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
