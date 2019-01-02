package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.model.SignUpReq;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;

public interface LoginService {
    DefaultRes login(final LoginReq loginReq);
    DefaultRes login(final int userIdx);
}
