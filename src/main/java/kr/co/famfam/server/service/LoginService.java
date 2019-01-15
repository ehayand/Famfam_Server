package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;

import java.util.Optional;

public interface LoginService {

    DefaultRes login(final LoginReq loginReq, final Optional<String> fcmToken);

    DefaultRes login(final int userIdx, final Optional<String> fcmToken);
}
