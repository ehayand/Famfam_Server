package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.GroupJoinReq;
import kr.co.famfam.server.model.HomePhotoReq;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("groups")
public class GroupController {

    private final GroupService groupService;
    private final JwtService jwtService;
    private final UserService userService;

    public GroupController(final GroupService groupService, final JwtService jwtService, final UserService userService){
        this.groupService = groupService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Auth
    @PostMapping("")
    public ResponseEntity getGroup(@RequestHeader("Authorization") final String jwt){
        try{
            int userIdx = jwtService.decode(jwt).getUser_idx();
            return new ResponseEntity<>(userIdx, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/{groupIdx}")
    public ResponseEntity joinGroup(@RequestHeader("Authorization") final String jwt,
                                    @RequestBody final GroupJoinReq groupJoinReq){
        try{
            int userIdx = jwtService.decode(jwt).getUser_idx();
            return new ResponseEntity<>(groupService.joinGroup(userIdx, groupJoinReq.getCode()), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("")
    public ResponseEntity updateGroup(HomePhotoReq homePhotoReq,
                                      @RequestPart(value = "photo", required = false) final MultipartFile photo){
        try{
            if(photo != null) homePhotoReq.setPhoto(photo);
            return new ResponseEntity<>(groupService.photoUpdate(homePhotoReq), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 그룹 삭제 기능 일단 보류

/*    @Auth
    @DeleteMapping("")
    public ResponseEntity deleteGroup(@RequestHeader("Authorization") final String jwt){
        try{


            return new ResponseEntity<>( HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

}
