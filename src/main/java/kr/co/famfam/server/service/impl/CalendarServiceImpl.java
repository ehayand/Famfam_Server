package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.CalendarSearchReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.service.AnniversaryService;
import kr.co.famfam.server.service.CalendarService;
import kr.co.famfam.server.service.FamilyCalendarService;
import kr.co.famfam.server.service.IndividualCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CalendarServiceImpl implements CalendarService {

    private final IndividualCalendarService individualCalendarService;
    private final FamilyCalendarService familyCalendarService;
    private final AnniversaryService anniversaryService;

    public CalendarServiceImpl(IndividualCalendarService individualCalendarService, FamilyCalendarService familyCalendarService, AnniversaryService anniversaryService) {
        this.individualCalendarService = individualCalendarService;
        this.familyCalendarService = familyCalendarService;
        this.anniversaryService = anniversaryService;
    }

    public DefaultRes findAllSchedule(final String dateStr) {
        // 가족 일정, 개인 일정 합치기

        LocalDateTime date = LocalDateTime.parse(dateStr);
        LocalDateTime startDate = date.minusMonths(1);
        LocalDateTime endDate = date.plusMonths(2);

        List<IndividualCalendar> individualCalendars = individualCalendarService.findByYearAndMonth(startDate, endDate);
        List<FamilyCalendar> familyCalendars = familyCalendarService.findByYearAndMonth(startDate, endDate);
        List<Anniversary> anniversaries = anniversaryService.findByYearAndMonth(startDate, endDate);

        if (anniversaries == null || individualCalendars == null || familyCalendars == null)
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

        Map<String, Object> map = new HashMap<>();

        map.put("individual", individualCalendars);
        map.put("family", familyCalendars);
        map.put("anniversary", anniversaries);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CALENDAR, map);
    }

    public DefaultRes findDaySchedule(final String dateStrTemp) {
        // 가족 일정, 개인 일정 합치기

        String dateStr = dateStrTemp.substring(0, 10);

        List<IndividualCalendar> individualCalendars = individualCalendarService.findByYearAndMonthAndDate(dateStr);
        List<FamilyCalendar> familyCalendars = familyCalendarService.findByYearAndMonthAndDate(dateStr);
        List<Anniversary> anniversaries = anniversaryService.findByYearAndMonthAndDate(dateStr);

        if (anniversaries == null || individualCalendars == null || familyCalendars == null)
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

        Map<String, Object> map = new HashMap<>();

        map.put("individual", individualCalendars);
        map.put("family", familyCalendars);
        map.put("anniversary", anniversaries);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CALENDAR, map);
    }

    @Transactional
    public DefaultRes addSchedule(final int calendarType, final CalendarReq calendarReq, final int authUserIdx) {
        // 타입값에 따라서 가족/개인 캘린더서비스 불러서 일정 추가하기

        String allDateStr = allDate(calendarReq);

        if (calendarType == 1) {
            return individualCalendarService.addSchedule(calendarReq, authUserIdx, allDateStr);
        } else if (calendarType == 2) {
            return familyCalendarService.addSchedule(calendarReq, authUserIdx, allDateStr);
        } else {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR_TYPE);
        }
    }

    @Transactional
    public DefaultRes updateSchedule(final int calendarType, final int calendarIdx, final CalendarReq calendarReq) {
        // 타입값에 따라서 가족/개인 캘린더서비스 불러서 일정 수정하기

        String allDateStr = "";
        if(!calendarReq.getStartDate().isEmpty() || !calendarReq.getEndDate().isEmpty())
            allDateStr = allDate(calendarReq);

        if (calendarType == 1) {
            return individualCalendarService.updateSchedule(calendarIdx, calendarReq, allDateStr);
        } else if (calendarType == 2) {
            return familyCalendarService.updateSchedule(calendarIdx, calendarReq, allDateStr);
        } else {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR_TYPE);
        }
    }

    @Transactional
    public DefaultRes deleteSchedule(final int calendarType, final int calendarIdx) {
        // 타입값에 따라서 가족/개인 캘린더서비스 불러서 일정 삭제하기
        if (calendarType == 1) {
            return individualCalendarService.deleteSchedule(calendarIdx);
        } else if (calendarType == 2) {
            return familyCalendarService.deleteSchedule(calendarIdx);
        } else {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR_TYPE);
        }
    }

    @Transactional
    public DefaultRes searchSchedule(final CalendarSearchReq calendarSearchReq) {
        // 일정 검색
        String content = calendarSearchReq.getContent();

        String per = "%";
        String contentTemp = per.concat(content);
        String result = contentTemp.concat("%");

        List<IndividualCalendar> individualCalendars = individualCalendarService.searchSchedule(result);
        List<FamilyCalendar> familyCalendars = familyCalendarService.searchSchedule(result);
        List<Anniversary> anniversaries = anniversaryService.searchSchedule(result);

        if (anniversaries == null || individualCalendars == null || familyCalendars == null)
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

        Map<String, Object> map = new HashMap<>();

        map.put("individual", individualCalendars);
        map.put("family", familyCalendars);
        map.put("anniversary", anniversaries);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_CALENDAR, map);
    }

    public String allDate(final CalendarReq calendarReq) {

        String startDateStr = calendarReq.getStartDate();
        String endDateStr = calendarReq.getEndDate();

        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        ArrayList<String> allDate = new ArrayList<>();

        LocalDateTime tempDate = startDate;
        while (tempDate.compareTo(endDate) <= 0) {
            allDate.add(tempDate.toString());
            tempDate = tempDate.plusDays(1);
        }

        return allDate.toString();
    }
}