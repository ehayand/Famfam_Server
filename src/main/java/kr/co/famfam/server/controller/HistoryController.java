package kr.co.famfam.server.controller;



import kr.co.famfam.server.service.HistoryService;
import kr.co.famfam.server.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/history")
public class HistoryController {

    private HistoryService historyService;
    private JwtService jwtService;

    public HistoryController(HistoryService historyService, JwtService jwtService) {
        this.historyService = historyService;
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public ResponseEntity getHistory(@RequestHeader(value = "Authorization") final String header) {
        int authIdx = jwtService.decode(header).getUser_idx();



        return new ResponseEntity<>(historyService.getHistory(authIdx), HttpStatus.OK);
    }
}
