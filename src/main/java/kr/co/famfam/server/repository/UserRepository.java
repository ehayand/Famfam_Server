package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUserIdAndUserPw(String userId, String userPw);

    List<User> findUsersByGroupIdx(int groupIdx);

    Optional<User> findUserByUserId(String userId);
}
