package kr.co.famfam.server.controller;

import kr.co.famfam.server.service.CalendarService;
import kr.co.famfam.server.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.famfam.server.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService){
        this.calendarService = calendarService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getMonthSchedule(@RequestParam(value = "year", defaultValue = "-1") final int year,
                                           @RequestParam(value = "month", defaultValue = "-1") final int month){
        try{
            // defaultValue값이 string으로 들어가는지 알아서 int형으로 변환 되는지 확인 해야함
            if(year == -1 || month == -1){
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.BAD_REQUEST);  // status 값 임의로 해뒀음
            }
            return new ResponseEntity<>(calendarService.findAllSchedule(year, month), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getDaySchedule(@RequestParam(value = "year", defaultValue = "-1") final int year,
                                         @RequestParam(value = "month", defaultValue = "-1") final int month,
                                         @RequestParam(value = "date", defaultValue = "-1") final int date){
        try{
            // defaultValue값이 string으로 들어가는지 알아서 int형으로 변환 되는지 확인 해야함
            if(year == -1 || month == -1){
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.BAD_REQUEST);  // status 값 임의로 해뒀음
            }
            return new ResponseEntity<>(calendarService.findDaySchedule(year, month, date), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
