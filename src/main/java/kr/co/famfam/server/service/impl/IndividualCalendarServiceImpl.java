package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.repository.IndividualCalendarRepository;
import kr.co.famfam.server.service.IndividualCalendarService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public List<IndividualCalendar> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate){
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        List<IndividualCalendar> individualCalendars = individualCalendarRepository.findByYearAndMonth(startDate, endDate);

        return individualCalendars;
    }

    public List<IndividualCalendar> findByYearAndMonthAndDate(final String dateStr){
         // 날짜에 맞는 일정 조회

        String per = "%";
        String tempStr = per.concat(dateStr);
        String result = tempStr.concat("%");

        List<IndividualCalendar> individualCalendars = individualCalendarRepository.findByYearAndMonthAndDate(result);

        return individualCalendars;
    }

    public void addSchedule(final CalendarReq calendarReq, final int authUserIdx, final String allDateStr){
        // 일정 추가

        IndividualCalendar schedule = new IndividualCalendar();
        schedule.setUserIdx(authUserIdx);
        schedule.setContent(calendarReq.getContent());
        schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
        schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
        schedule.setAllDate(allDateStr);
        schedule.setReturningTime(calendarReq.getReturningTime());
        schedule.setDinner(calendarReq.getDinner());

        individualCalendarRepository.save(schedule);
    }

    public void updateSchedule(final int calendarIdx, final CalendarReq calendarReq, final String allDateStr){
        // 일정 수정

        IndividualCalendar schedule = individualCalendarRepository.findById(calendarIdx).get();
        schedule.setContent(calendarReq.getContent());
        schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
        schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
        schedule.setAllDate(allDateStr);
        schedule.setReturningTime(calendarReq.getReturningTime());
        schedule.setDinner(calendarReq.getDinner());

        individualCalendarRepository.save(schedule);
    }

    public void deleteSchedule(final int calendarIdx){
        // 일정 삭제

        individualCalendarRepository.deleteById(calendarIdx);
    }

}
