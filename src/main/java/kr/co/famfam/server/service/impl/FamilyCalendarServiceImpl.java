package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.service.FamilyCalendarService;
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
public class FamilyCalendarServiceImpl implements FamilyCalendarService {

    private final FamilyCalendarRepository familyCalendarRepository;

    public FamilyCalendarServiceImpl(FamilyCalendarRepository familyCalendarRepository){
        this.familyCalendarRepository = familyCalendarRepository;
    }

    public List<FamilyCalendar> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate){
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByYearAndMonth(startDate, endDate);

        return familyCalendars;
    }

    public List<FamilyCalendar> findByYearAndMonthAndDate(final String dateStr){
        // 날짜에 맞는 일정 조회

        String per = "%";
        String tempStr = per.concat(dateStr);
        String result = tempStr.concat("%");

        List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByYearAndMonthAndDate(result);

        return familyCalendars;
    }

    public void addSchedule(final CalendarReq calendarReq, final int authUserIdx, final String allDateStr){
        // 일정 추가

        FamilyCalendar schedule = new FamilyCalendar();
        schedule.setUserIdx(authUserIdx);
        schedule.setContent(calendarReq.getContent());
        schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
        schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
        schedule.setAllDate(allDateStr);

        familyCalendarRepository.save(schedule);
    }

    public void updateSchedule(final int calendarIdx, final CalendarReq calendarReq, final String allDateStr){
        // 일정 수정

        FamilyCalendar schedule = familyCalendarRepository.findById(calendarIdx).get();
        schedule.setContent(calendarReq.getContent());
        schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
        schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
        schedule.setAllDate(allDateStr);

        familyCalendarRepository.save(schedule);
    }

    public void deleteSchedule(final int calendarIdx){
        // 일정 삭제

        familyCalendarRepository.deleteById(calendarIdx);
    }

}
