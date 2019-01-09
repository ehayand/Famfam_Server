package kr.co.famfam.server.controller;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.*;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import kr.co.famfam.server.utils.auth.Auth;
import kr.co.famfam.server.utils.security.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by ehay@naver.com on 2018-12-27
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
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
     * @param header jwt token
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("")
    public ResponseEntity<DefaultRes> getUser(@RequestHeader(value = "Authorization") final String header) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(userService.findById(authIdx), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();

            //  TODO multivalue 수정
            return new ResponseEntity<>((MultiValueMap<String, String>) FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/groups")
    public ResponseEntity<DefaultRes> getGroup(@RequestHeader(value = "Authorization") final String header) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(userService.findUsersByGroupIdx(authIdx), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            //  TODO multivalue 수정
            return new ResponseEntity<>((MultiValueMap<String, String>) FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
    @PutMapping("")
    public ResponseEntity<DefaultRes> updateUser(@RequestHeader(value = "Authorization") final String header,
                                                 @RequestBody final UserinfoReq userinfoReq) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authIdx);

            return new ResponseEntity<>(userService.update(authIdx, userinfoReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/photo")
    public ResponseEntity<DefaultRes> updateUserPhoto(@RequestHeader(value = "Authorization") final String header,
                                                      final UserinfoPhotoReq userinfoPhotoReq,
                                                      @RequestPart(value = "profilePhoto", required = false) final MultipartFile profilePhoto,
                                                      @RequestPart(value = "backPhoto", required = false) final MultipartFile backPhoto) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authIdx);

            if (profilePhoto != null) userinfoPhotoReq.setProfilePhoto(profilePhoto);
            if (backPhoto != null) userinfoPhotoReq.setBackPhoto(backPhoto);
            return new ResponseEntity<>(userService.updatePhoto(authIdx, userinfoPhotoReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/id")
    public ResponseEntity<DefaultRes> checkDuplicationId(@RequestBody final DuplicationIdReq duplicationIdReq) {
        try {
            if (duplicationIdReq == null)
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(userService.checkDuplicationId(duplicationIdReq.getUserId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/password")
    public ResponseEntity<DefaultRes> checkPassword(@RequestHeader(value = "Authorization") final String header,
                                                    @RequestBody final PasswordReq passwordReq) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            PasswordUtil util = new PasswordUtil();
            passwordReq.setUserPw(util.encryptSHA256(passwordReq.getUserPw()));
            return new ResponseEntity<>(userService.checkPw(authIdx, passwordReq), HttpStatus.OK);
        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @Auth
    @PutMapping("/password")
    public ResponseEntity<DefaultRes> updatePassword(@RequestHeader(value = "Authorization") final String header,
                                                     @RequestBody final PasswordReq passwordReq) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            PasswordUtil util = new PasswordUtil();
            passwordReq.setUserPw(util.encryptSHA256(passwordReq.getUserPw()));
            return new ResponseEntity<>(userService.updatePw(authIdx, passwordReq), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestHeader(value = "Authorization") final String header) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();
            if (jwtService.checkAuth(header, authIdx))
                return new ResponseEntity<>(userService.deleteByUserIdx(authIdx), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id")
    public ResponseEntity findUserId(@RequestBody FindUserIdReq findUserIdReq) {
        try {
            return new ResponseEntity<>(userService.findUserId(findUserIdReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/password")
    public ResponseEntity findUserPassword(@RequestBody FindUserPasswordReq findUserPasswordReq) {
        try {
            return new ResponseEntity<>(userService.findUserPassword(findUserPasswordReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/password/{userIdx}")
    public ResponseEntity editUserPassword(@PathVariable("userIdx") final int userIdx,
                                           @RequestBody PasswordReq passwordReq) {
        try {
            return new ResponseEntity<>(userService.updatePw(userIdx, passwordReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}




