package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-30
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, String> {
    Optional<GroupInvitation> findGroupInvitationByGroupIdx(int groupIdx);
}
