package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
}
