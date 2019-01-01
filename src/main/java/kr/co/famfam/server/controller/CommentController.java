package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.CommentDto;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.service.CommentService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    public CommentController(CommentService commentService, JwtService jwtService) {
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("/contents/{contentIdx}")
    public ResponseEntity<DefaultRes> getComments(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(commentService.findCommentsByContentIdx(contentIdx), HttpStatus.OK);
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
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(commentService.countThisWeek(authUserIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/contents/{contentIdx}")
    public ResponseEntity<DefaultRes> saveComment(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx,
            @RequestBody final CommentDto commentDto) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            commentDto.setUserIdx(authUserIdx);
            commentDto.setContentIdx(contentIdx);


            return new ResponseEntity<>(commentService.save(commentDto), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/{commentIdx}")
    public ResponseEntity<DefaultRes> updateComment(
            @RequestHeader("Authorization") final String header,
            @PathVariable("commentIdx") final int commentIdx,
            @RequestBody final CommentDto commentDto) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(commentService.update(commentIdx, commentDto), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/{commentIdx}")
    public ResponseEntity<DefaultRes> updateComment(
            @RequestHeader("Authorization") final String header,
            @PathVariable("commentIdx") final int commentIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(commentService.delete(commentIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
