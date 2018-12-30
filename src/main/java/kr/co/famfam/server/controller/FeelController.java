package kr.co.famfam.server.controller;

import kr.co.famfam.server.service.FeelService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/feel")
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
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(feelService.findFeelsByContentIdx(contentIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/{type}/contents/{contentIdx}")
    public ResponseEntity saveFeel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("type") final int type,
            @PathVariable("contentIdx") final int contentIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(feelService.save(contentIdx, authUserIdx, type), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/{feelIdx}")
    public ResponseEntity saveFeel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("feelIdx") final int feelIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(feelService.delete(feelIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
