package kr.co.famfam.server.controller;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(final GroupService groupService){
        this.groupService = groupService;
    }

    @PostMapping("")
    public ResponseEntity getGroup(@RequestBody final Group group){
        try{
            return new ResponseEntity<>(groupService.save(group), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
