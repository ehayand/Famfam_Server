package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.repository.IndividualCalendarRepository;
import kr.co.famfam.server.service.FamilyCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class FamilyCalendarServiceImpl implements FamilyCalendarService {

    private final FamilyCalendarRepository familyCalendarRepository;

    public FamilyCalendarServiceImpl(FamilyCalendarRepository familyCalendarRepository){
        this.familyCalendarRepository = familyCalendarRepository;
    }

    public List<FamilyCalendar> findByYearAndMonth(final int year, final int month){

        List<FamilyCalendar> familyCalendars = new LinkedList<>();;

        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        return familyCalendars;
    }

    public List<FamilyCalendar> findByYearAndMonthAndDate(final int year, final int month, final int date){

        List<FamilyCalendar> familyCalendars = new LinkedList<>();;

        // 날짜에 맞는 일정 조회

        return familyCalendars;
    }

    public void addSchedule(final CalendarReq calendarReq, final int authUserIdx){
        // 일정 추가

        FamilyCalendar schedule = new FamilyCalendar();
        schedule.setUserIdx(authUserIdx);
        schedule.setContent(calendarReq.getContent());
        schedule.setStartDate(calendarReq.getStartDate());
        schedule.setEndDate(calendarReq.getEndDate());

        familyCalendarRepository.save(schedule);
    }

    public void updateSchedule(final int calendarIdx, final CalendarReq calendarReq){
        // 일정 수정

        FamilyCalendar schedule = familyCalendarRepository.findById(calendarIdx).get();
        schedule.setContent(calendarReq.getContent());
        schedule.setStartDate(calendarReq.getStartDate());
        schedule.setEndDate(calendarReq.getEndDate());

        familyCalendarRepository.save(schedule);
    }

    public void deleteSchedule(final int calendarIdx){
        // 일정 삭제

        familyCalendarRepository.deleteById(calendarIdx);
    }

}
