package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.repository.IndividualCalendarRepository;
import kr.co.famfam.server.service.IndividualCalendarService;
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
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        int tempMonth = month, tempYear = year;
        if(month-1 == 0) {
            tempMonth = 12;
            --tempYear;
        }
        else if(month+1 == 13) {
            tempMonth = 1;
            ++tempYear;
        }

        List<IndividualCalendar> current = individualCalendarRepository.findIndividualCalendarsByStartYearAndStartMonth(year, month);
        List<IndividualCalendar> before = individualCalendarRepository.findIndividualCalendarsByStartYearAndStartMonth(tempYear, tempMonth);
        List<IndividualCalendar> after = individualCalendarRepository.findIndividualCalendarsByStartYearAndStartMonth(tempYear, tempMonth);

        current.addAll(before);
        current.addAll(after);

        return current;
    }

    public List<IndividualCalendar> findByYearAndMonthAndDate(final int year, final int month, final int date){
         // 날짜에 맞는 일정 조회

        List<IndividualCalendar> oneday = individualCalendarRepository.findIndividualCalendarsByStartYearAndStartMonthAndStartDate(year, month, date);

        return oneday;
    }

    public void addSchedule(final CalendarReq calendarReq, final int authUserIdx){
        // 일정 추가

        IndividualCalendar schedule = new IndividualCalendar();
        schedule.setUserIdx(authUserIdx);
        schedule.setContent(calendarReq.getContent());
        schedule.setStartYear(calendarReq.getStartYear());
        schedule.setStartMonth(calendarReq.getStartMonth());
        schedule.setStartDate(calendarReq.getStartDate());
        schedule.setEndYear(calendarReq.getEndYear());
        schedule.setEndMonth(calendarReq.getEndMonth());
        schedule.setEndDate(calendarReq.getEndDate());
        schedule.setReturningTime(calendarReq.getReturningTime());
        schedule.setDinner(calendarReq.getDinner());

        individualCalendarRepository.save(schedule);
    }

    public void updateSchedule(final int calendarIdx, final CalendarReq calendarReq){
        // 일정 수정

        IndividualCalendar schedule = individualCalendarRepository.findById(calendarIdx).get();
        schedule.setContent(calendarReq.getContent());
        schedule.setStartYear(calendarReq.getStartYear());
        schedule.setStartMonth(calendarReq.getStartMonth());
        schedule.setStartDate(calendarReq.getStartDate());
        schedule.setEndYear(calendarReq.getEndYear());
        schedule.setEndMonth(calendarReq.getEndMonth());
        schedule.setEndDate(calendarReq.getEndDate());
        schedule.setReturningTime(calendarReq.getReturningTime());
        schedule.setDinner(calendarReq.getDinner());

        individualCalendarRepository.save(schedule);
    }

    public void deleteSchedule(final int calendarIdx){
        // 일정 삭제

        individualCalendarRepository.deleteById(calendarIdx);
    }

}
