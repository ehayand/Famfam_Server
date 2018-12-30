package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.service.AnniversaryService;
import kr.co.famfam.server.service.CalendarService;
import kr.co.famfam.server.service.FamilyCalendarService;
import kr.co.famfam.server.service.IndividualCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final IndividualCalendarService individualCalendarService;
    private final FamilyCalendarService familyCalendarService;
    private final AnniversaryService anniversaryService;

    public CalendarServiceImpl(IndividualCalendarService individualCalendarService, FamilyCalendarService familyCalendarService, AnniversaryService anniversaryService){
        this.individualCalendarService = individualCalendarService;
        this.familyCalendarService = familyCalendarService;
        this.anniversaryService = anniversaryService;
    }

    public DefaultRes findAllSchedule(final int year, final int month){
        // 가족 일정, 개인 일정 합치기

        List<IndividualCalendar> individualCalendars = individualCalendarService.findByYearAndMonth(year, month);
        List<FamilyCalendar> familyCalendars = familyCalendarService.findByYearAndMonth(year, month);
        List<Anniversary> anniversaries = anniversaryService.findByYearAndMonth(year, month);

        Map<String, Object> map = new HashMap<>();

        map.put("individual", individualCalendars);
        map.put("family", familyCalendars);
        map.put("anniversary", anniversaries);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, map);
    }

    public DefaultRes findDaySchedule(final int year, final int month, final int date){
        // 가족 일정, 개인 일정 합치기

        List<IndividualCalendar> individualCalendars = individualCalendarService.findByYearAndMonthAndDate(year, month, date);
        List<FamilyCalendar> familyCalendars = familyCalendarService.findByYearAndMonthAndDate(year, month, date);
        List<Anniversary> anniversaries = anniversaryService.findByYearAndMonthAndDate(year, month, date);

        Map<String, Object> map = new HashMap<>();

        map.put("individual", individualCalendars);
        map.put("family", familyCalendars);
        map.put("anniversary", anniversaries);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, map);
    }
    public DefaultRes addSchedule(final int calendarType, final CalendarReq calendarReq, final int authUserIdx){
        // 타입값에 따라서 가족/개인 캘린더서비스 불러서 일정 추가하기

        if(calendarType == 1){
            individualCalendarService.addSchedule(calendarReq, authUserIdx);
        }else if(calendarType == 2){
            familyCalendarService.addSchedule(calendarReq, authUserIdx);
        }else{
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    public DefaultRes updateSchedule(final int calendarType, final int calendarIdx, final CalendarReq calendarReq){
        // 타입값에 따라서 가족/개인 캘린더서비스 불러서 일정 수정하기

        if(calendarType == 1){
            individualCalendarService.updateSchedule(calendarIdx, calendarReq);
        }else if(calendarType == 2){
            familyCalendarService.updateSchedule(calendarIdx, calendarReq);
        }else{
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    public DefaultRes deleteSchedule(final int calendarType, final int calendarIdx){
        // 타입값에 따라서 가족/개인 캘린더서비스 불러서 일정 삭제하기

        if(calendarType == 1){
            individualCalendarService.deleteSchedule(calendarIdx);
        }else if(calendarType == 2){
            familyCalendarService.deleteSchedule(calendarIdx);
        }else{
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }
}
