package kr.co.famfam.server.controller;

import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.PhotoService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final JwtService jwtService;

    public PhotoController(PhotoService photoService, JwtService jwtService) {
        this.photoService = photoService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getPhotos(
            @RequestHeader("Authorization") final String header,
            @RequestParam("userIdx") final Optional<Integer> userIdx,
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC, size = 20) Pageable pageable) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();

            if (userIdx.isPresent())
                return new ResponseEntity<>(photoService.findPhotosByUserIdx(userIdx.get(), pageable), HttpStatus.OK);

            return new ResponseEntity<>(photoService.findPhotosByUserIdx(authUserIdx, pageable), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
