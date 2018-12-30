package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.AnniversaryReq;
import kr.co.famfam.server.service.AnniversaryService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("anniversary")
public class AnniversaryController {
    private final AnniversaryService anniversaryService;
    private final JwtService jwtService;

    public AnniversaryController(AnniversaryService anniversaryService, JwtService jwtService){
        this.anniversaryService = anniversaryService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getAllAnniversary(@RequestHeader("Authorization") final String header){
        try{
            int authUserIdx = jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(anniversaryService.findAll(authUserIdx), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/{anniversaryType}")
    public ResponseEntity addAnniversary(@PathVariable(value = "anniversaryType") final int anniversaryType,
                                         @RequestBody AnniversaryReq anniversaryReq){
        try{
            return new ResponseEntity<>(anniversaryService.addAnniversary(anniversaryType, anniversaryReq), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/{anniversaryIdx}")
    public ResponseEntity deleteAnniversary(@PathVariable(value = "anniversaryIdx") final int anniversaryIdx){
        try{
            return new ResponseEntity<>(anniversaryService.deleteAnniversary(anniversaryIdx), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
