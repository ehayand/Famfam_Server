package kr.co.famfam.server.controller;

import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.MissionService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@RestController
@RequestMapping("/mission")
public class MissionController {

    private final MissionService missionService;
    private final JwtService jwtService;

    public MissionController(MissionService missionService, JwtService jwtService) {
        this.missionService = missionService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getMission(@RequestHeader(value = "Authorization") final String header) {
        try{
            int authIdx = jwtService.decode(header).getUser_idx();
            return new ResponseEntity(missionService.findById(authIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
