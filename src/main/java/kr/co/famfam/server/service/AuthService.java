package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public DefaultRes<JwtService.TokenRes> login(final LoginReq loginReq) {
        final Optional<User> user = userRepository.findUserByUserIdAndUserPw(loginReq.getUserId(), loginReq.getUserPw());
        if (user.isPresent()) {
            //토큰 생성
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.get().getUserIdx()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }

        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }
}
