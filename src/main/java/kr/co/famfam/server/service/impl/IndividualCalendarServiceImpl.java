package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.repository.IndividualCalendarRepository;
import kr.co.famfam.server.service.IndividualCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class IndividualCalendarServiceImpl implements IndividualCalendarService {

    private final IndividualCalendarRepository individualCalendarRepository;

    public IndividualCalendarServiceImpl(IndividualCalendarRepository individualCalendarRepository){
        this.individualCalendarRepository = individualCalendarRepository;
    }

    public List<IndividualCalendar> findByYearAndMonth(final int year, final int month){

        List<IndividualCalendar> individualCalendars;

        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        return individualCalendars;
    }

    public List<IndividualCalendar> findByYearAndMonthAndDate(final int year, final int month, final int date){

        List<IndividualCalendar> individualCalendars;

        // 날짜에 맞는 일정 조회

        return individualCalendars;
    }

    public void addSchedule(final CalendarReq calendarReq){
        // 일정 추가
    }

    public void updateSchedule(final int calendarIdx, final CalendarReq calendarReq){
        // 일정 수정
    }

    public void deleteSchedule(final int calendarIdx, final CalendarReq calendarReq){
        // 일정 삭제
    }

}
