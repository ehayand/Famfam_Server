package kr.co.famfam.server.controller;

import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.service.CalendarService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final JwtService jwtService;

    public CalendarController(CalendarService calendarService, JwtService jwtService) {
        this.calendarService = calendarService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("/month/{dateStr}")
    public ResponseEntity<DefaultRes> getMonthSchedule(@PathVariable(value = "dateStr") final String dateStr) {
        try {
            return new ResponseEntity<>(calendarService.findAllSchedule(dateStr), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/oneday/{dateStr}")
    public ResponseEntity getDaySchedule(@PathVariable(value = "dateStr") final String dateStr) {
        try {
            return new ResponseEntity<>(calendarService.findDaySchedule(dateStr), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/{calendarType}")
    public ResponseEntity addSchedule(@PathVariable(value = "calendarType") final int calendarType,
                                      @RequestBody CalendarReq calendarReq,
                                      @RequestHeader("Authorization") final String header) {
        try {
            int authUserIdx = jwtService.decode(header).getUser_idx();
            log.info("ID : " + authUserIdx);

            return new ResponseEntity<>(calendarService.addSchedule(calendarType, calendarReq, authUserIdx), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/{calendarType}/{calendarIdx}")
    public ResponseEntity updateSchedule(@PathVariable(value = "calendarType") final int calendarType,
                                         @PathVariable(value = "calendarIdx") final int calendarIdx,
                                         @RequestBody CalendarReq calendarReq) {
        try {
            return new ResponseEntity<>(calendarService.updateSchedule(calendarType, calendarIdx, calendarReq), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/{calendarType}/{calendarIdx}")
    public ResponseEntity deleteSchedule(@PathVariable(value = "calendarType") final int calendarType,
                                         @PathVariable(value = "calendarIdx") final int calendarIdx) {
        try {
            return new ResponseEntity<>(calendarService.deleteSchedule(calendarType, calendarIdx), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
