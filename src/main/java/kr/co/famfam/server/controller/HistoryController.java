package kr.co.famfam.server.controller;


import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.service.HistoryService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.HistoryType;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/history")
public class HistoryController {

    private HistoryService historyService;
    private JwtService jwtService;

    public HistoryController(HistoryService historyService, JwtService jwtService) {
        this.historyService = historyService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity<DefaultRes> getHistory(@RequestHeader(value = "Authorization") final String header,
                                                 @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC, size = 12) Pageable pageable) {
        try {
            int authIdx = jwtService.decode(header).getUser_idx();

            return new ResponseEntity<>(historyService.findAllHistoryByUserIdx(authIdx, pageable), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
