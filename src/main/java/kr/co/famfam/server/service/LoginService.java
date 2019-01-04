package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;

public interface LoginService {

    DefaultRes login(final LoginReq loginReq);
    DefaultRes login(final int userIdx);
}
