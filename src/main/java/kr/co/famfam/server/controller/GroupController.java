package kr.co.famfam.server.controller;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("groups")
public class GroupController {

    private final GroupService groupService;
    private final JwtService jwtService;

    public GroupController(final GroupService groupService, final JwtService jwtService){
        this.groupService = groupService;
        this.jwtService = jwtService;
    }

    @Auth
    @PostMapping("")
    public ResponseEntity getGroup(@RequestHeader("Authorization") final String jwt){
        try{
            return new ResponseEntity<>(groupService.save(jwtService.decode(jwt).getUser_idx()), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
