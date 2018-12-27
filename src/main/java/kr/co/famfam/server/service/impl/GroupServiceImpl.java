package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public class GroupServiceImpl {

    private static GroupRepository groupRepository;
    private static UserRepository userRepository;

    public GroupServiceImpl(final GroupRepository groupRepository, final UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public DefaultRes joinGroup(int userIdx, String code) {
        Optional<User> user = userRepository.findById(userIdx);

        int groupIdx = auth(code);

        user.get().setGroupIdx(groupIdx);
        userRepository.save(user.get());

        return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_USER);
    }

    private int auth(String code) {
        int groupIdx = -1;

        return groupIdx;
    }
}
