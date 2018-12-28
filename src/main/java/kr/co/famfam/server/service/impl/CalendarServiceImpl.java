package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.DefaultRes;
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

    public CalendarServiceImpl(IndividualCalendarService individualCalendarService, FamilyCalendarService familyCalendarService){
        this.individualCalendarService = individualCalendarService;
        this.familyCalendarService = familyCalendarService;
    }

    public DefaultRes findAllSchedule(final int year, final int month){

        // 가족 일정, 개인 일정 합치기

        List<IndividualCalendar> individualCalendars = individualCalendarService.findByYearAndMonth(year, month);
        List<FamilyCalendar> familyCalendars = familyCalendarService.findByYearAndMonth(year, month);

        Map<String, Object> map = new HashMap<>();

        map.put("individual", individualCalendars);
        map.put("family", familyCalendars);


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, map);
    }
}
