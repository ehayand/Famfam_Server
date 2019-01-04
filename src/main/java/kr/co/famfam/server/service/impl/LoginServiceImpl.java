package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.model.UserRes;
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
    private final JwtService jwtService;

    public LoginServiceImpl(UserRepository userRepository, JwtService jwtService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /***
     *
     * @param loginReq
     * @return
     */
    public DefaultRes login(LoginReq loginReq) {
        if (loginReq.isLogin()) {
            User loginUser = new User(loginReq);

            final Optional<User> user = userRepository.findUserByUserIdAndUserPw(loginUser.getUserId(), loginUser.getUserPw());

            if (user.isPresent()) {
                final JwtService.TokenRes tokenRes = new JwtService.TokenRes(jwtService.create(user.get().getUserIdx()));

                Map<String, Object> result = new HashMap<>();
                result.put("token", tokenRes.getToken());
                result.put("user", new UserRes(user.get()));

                return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, result);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }

    public DefaultRes login(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);

        if (user.isPresent()) {
            final JwtService.TokenRes tokenRes = new JwtService.TokenRes(jwtService.create(user.get().getUserIdx()));
            Map<String, Object> result = new HashMap<>();
            result.put("token", tokenRes.getToken());
            result.put("user", user);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenRes);
        }


        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }
}

