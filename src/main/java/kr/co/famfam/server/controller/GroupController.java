package kr.co.famfam.server.controller;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.GroupJoinReq;
import kr.co.famfam.server.model.HomePhotoReq;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final JwtService jwtService;

    public GroupController(final GroupService groupService, final JwtService jwtService){
        this.groupService = groupService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getGroup(@RequestHeader("Authorization") final String header) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(authUserIdx, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/{groupIdx}")
    public ResponseEntity joinGroup(@RequestHeader("Authorization") final String header,
                                    @RequestBody final GroupJoinReq groupJoinReq) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(groupService.joinGroup(authUserIdx, groupJoinReq.getCode()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("")
    public ResponseEntity saveGroup(@RequestHeader("Authorization") final String header) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(groupService.save(authUserIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("")
    public ResponseEntity updateGroup(@RequestHeader("Authorization") final String header,
                                      HomePhotoReq homePhotoReq,
                                      @RequestPart(value = "photo", required = false) final MultipartFile photo){
        try{
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            if(photo != null) homePhotoReq.setPhoto(photo);
            return new ResponseEntity<>(groupService.photoUpdate(homePhotoReq), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("")
    public ResponseEntity deleteGroup(@RequestHeader("Authorization") final String header,
                                      @PathVariable(value = "groupIdx") final int groupIdx) {
        try {
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
