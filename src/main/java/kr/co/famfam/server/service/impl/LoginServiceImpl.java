package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.model.UserRes;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.LoginService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final JwtService jwtService;

    public LoginServiceImpl(UserRepository userRepository, GroupRepository groupRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.jwtService = jwtService;
    }

    public DefaultRes login(LoginReq loginReq) {
        if (!loginReq.isLogin())
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);

        User loginUser = new User(loginReq);

        final Optional<User> user = userRepository.findUserByUserIdAndUserPw(loginUser.getUserId(), loginUser.getUserPw());
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);

        Map<String, Object> result = new HashMap<>();

        if (user.get().getGroupIdx() != -1) {
            Optional<Group> group = groupRepository.findById(user.get().getGroupIdx());
            result.put("groupId", group.get().getGroupId());
        }
      
        final JwtService.TokenRes tokenRes = new JwtService.TokenRes(jwtService.create(user.get().getUserIdx()));

        result.put("token", tokenRes.getToken());
        result.put("user", new UserRes(user.get()));

        return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, result);
    }

    public DefaultRes login(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);

        Map<String, Object> result = new HashMap<>();

        if (user.get().getGroupIdx() != -1) {
            Optional<Group> group = groupRepository.findById(user.get().getGroupIdx());
            result.put("groupId", group.get().getGroupId());
        }
      
        final JwtService.TokenRes tokenRes = new JwtService.TokenRes(jwtService.create(user.get().getUserIdx()));

        result.put("token", tokenRes.getToken());
        result.put("user", new UserRes(user.get()));

        return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, result);
    }
}

