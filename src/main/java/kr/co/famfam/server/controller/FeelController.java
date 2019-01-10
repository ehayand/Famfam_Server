package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.FeelReq;
import kr.co.famfam.server.service.FeelService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/feels")
public class FeelController {

    private final FeelService feelService;
    private final JwtService jwtService;

    public FeelController(FeelService feelService, JwtService jwtService) {
        this.feelService = feelService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("/contents/{contentIdx}")
    public ResponseEntity getFeels(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx) {
        try {
            return new ResponseEntity<>(feelService.findFeelsByContentIdx(contentIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/count/week")
    public ResponseEntity<DefaultRes> getCountThisWeek(
            @RequestHeader("Authorization") final String header) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();

            return new ResponseEntity<>(feelService.countThisWeek(authUserIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/contents/{contentIdx}")
    public ResponseEntity saveFeel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx,
            @RequestBody final Optional<FeelReq> feelReq) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();

            if (!feelReq.isPresent())
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.BAD_REQUEST);

            feelReq.get().setContentIdx(contentIdx);
            feelReq.get().setUserIdx(authUserIdx);

            return new ResponseEntity<>(feelService.save(feelReq.get()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/contents/{contentIdx}")
    public ResponseEntity deleteFeel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();

            return new ResponseEntity<>(feelService.delete(contentIdx, authUserIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
