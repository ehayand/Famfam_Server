package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.ContentReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.service.ContentService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;
    private final JwtService jwtService;

    public ContentController(ContentService contentService, JwtService jwtService) {
        this.contentService = contentService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity<DefaultRes> getContents(
            @RequestHeader("Authorization") final String header,
            @RequestParam("userIdx") final Optional<Integer> userIdx,
            @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            if (userIdx.isPresent())
                return new ResponseEntity<>(contentService.findContentsByUserIdx(userIdx.get(), pageable), HttpStatus.OK);

            return new ResponseEntity<>(contentService.findContentsByGroupIdx(authUserIdx, pageable), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/{contentIdx}")
    public ResponseEntity<DefaultRes> getContent(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(contentService.findContentById(contentIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/photos/{photoIdx}")
    public ResponseEntity<DefaultRes> getContentByPhoto(
            @RequestHeader("Authorization") final String header,
            @PathVariable("photoIdx") final int photoIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(contentService.findContentByPhotoId(photoIdx), HttpStatus.OK);
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

            return new ResponseEntity<>(contentService.countThisWeek(authUserIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("")
    public ResponseEntity<DefaultRes> save(
            @RequestHeader("Authorization") final String header,
            ContentReq contentReq,
            @RequestPart(value = "photos", required = false) final MultipartFile[] files) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            contentReq.setUserIdx(authUserIdx);

            if (files != null) {
                log.info("files is not null");
                log.info("" + files.length);
                contentReq.setPhotos(files);
            }
            return new ResponseEntity<>(contentService.save(contentReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/{contentIdx}")
    public ResponseEntity<DefaultRes> update(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx,
            @RequestBody ContentReq contentReq) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(contentService.update(contentIdx, contentReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/{contentIdx}")
    public ResponseEntity<DefaultRes> delete(
            @RequestHeader("Authorization") final String header,
            @PathVariable("contentIdx") final int contentIdx) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(contentService.deleteByContentId(authUserIdx, contentIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
