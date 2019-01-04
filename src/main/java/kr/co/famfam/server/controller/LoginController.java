package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.model.SignUpReq;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.LoginService;
import kr.co.famfam.server.utils.auth.Auth;
import kr.co.famfam.server.utils.security.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("")
public class LoginController {
    private final LoginService loginService;
    private final JwtService jwtService;

    public LoginController(final LoginService loginService ,final JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService= jwtService;
    }
    /**
     * 로그인
     *
     * @param loginReq 로그인 폼
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody final LoginReq loginReq) {
        try {
            PasswordUtil util=new PasswordUtil();
            loginReq.setUserPw(util.encryptSHA256(loginReq.getUserPw()));
            return new ResponseEntity<>(loginService.login(loginReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/login")
    public ResponseEntity<DefaultRes> login(@RequestHeader(value = "Authorization") final String header) {
        try {
            int authIdx=jwtService.decode(header).getUser_idx();

            return new ResponseEntity<>(loginService.login(authIdx), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
