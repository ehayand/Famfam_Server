package kr.co.famfam.server.controller;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.*;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import kr.co.famfam.server.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by ehay@naver.com on 2018-12-27
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */
@RestController

@RequestMapping("/users")
public class UserController {
    private static final DefaultRes<User> UNAUTHORIZED_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    private final UserService userService;

    private final JwtService jwtService;

    public UserController(final UserService userService, final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회
     *
     * @param header  jwt token
     *
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("")
    public ResponseEntity<DefaultRes> getUser( @RequestHeader(value = "Authorization") final String header) {
        try {
            System.out.println(header);

            int authIdx=jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(userService.findById(authIdx), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            //  TODO multivalue 수정
            return new ResponseEntity<>((MultiValueMap<String, String>) FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@Auth
    @RequestMapping("/groups")
    @GetMapping("/{groupIdx}")
    public ResponseEntity<DefaultRes> getGroup( @RequestHeader(value = "Authorization") final String header) {
        try {
            System.out.println(header);

            int authIdx=jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(userService.findById(authIdx), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            //  TODO multivalue 수정
            return new ResponseEntity<>((MultiValueMap<String, String>) FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("")
    public ResponseEntity<DefaultRes> signUp(@RequestBody final SignUpReq signUpReq) {
        try {
            return new ResponseEntity<>(userService.save(signUpReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Auth
    @PutMapping("/{userIdx}")
    public ResponseEntity<DefaultRes> updateUser(@RequestHeader(value = "Authorization") final String header,
                                                 @PathVariable("userIdx") final int userIdx,
                                                 @RequestBody final UserinfoReq userinfoReq) {

        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            if (authIdx == userIdx) {
            }
            return new ResponseEntity<>(userService.update(userIdx, userinfoReq), HttpStatus.OK);

        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @Auth
    @DeleteMapping("/{userIdx}")
    public ResponseEntity deleteUser( @RequestHeader(value = "Authorization") final String header,
                                      @PathVariable("userIdx") final int userIdx) {

        try {

            if (jwtService.checkAuth(header, userIdx))
                return new ResponseEntity<>(userService.deleteByUserIdx(userIdx), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);

        } catch (Exception e) {

            e.printStackTrace();

            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


}




