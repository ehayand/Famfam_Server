package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.service.FamilyCalendarService;
import org.springframework.stereotype.Service;

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

    public List<FamilyCalendar> findByYearAndMonth(final int year, final int month){
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

        List<FamilyCalendar> current = familyCalendarRepository.findFamilyCalendarsByStartYearAndStartMonth(year, month);
        List<FamilyCalendar> before = familyCalendarRepository.findFamilyCalendarsByStartYearAndStartMonth(tempYear, tempMonth);
        List<FamilyCalendar> after = familyCalendarRepository.findFamilyCalendarsByStartYearAndStartMonth(tempYear, tempMonth);

        current.addAll(before);
        current.addAll(after);

        return current;
    }

    public List<FamilyCalendar> findByYearAndMonthAndDate(final int year, final int month, final int date){
        // 날짜에 맞는 일정 조회

        List<FamilyCalendar> oneday = familyCalendarRepository.findFamilyCalendarsByStartYearAndStartMonthAndStartDate(year, month, date);

        return oneday;
    }

    public void addSchedule(final CalendarReq calendarReq, final int authUserIdx){
        // 일정 추가

        FamilyCalendar schedule = new FamilyCalendar();
        schedule.setUserIdx(authUserIdx);
        schedule.setContent(calendarReq.getContent());
        schedule.setStartYear(calendarReq.getStartYear());
        schedule.setStartMonth(calendarReq.getStartMonth());
        schedule.setStartDate(calendarReq.getStartDate());
        schedule.setEndYear(calendarReq.getEndYear());
        schedule.setEndMonth(calendarReq.getEndMonth());
        schedule.setEndDate(calendarReq.getEndDate());

        familyCalendarRepository.save(schedule);
    }

    public void updateSchedule(final int calendarIdx, final CalendarReq calendarReq){
        // 일정 수정

        FamilyCalendar schedule = familyCalendarRepository.findById(calendarIdx).get();
        schedule.setContent(calendarReq.getContent());
        schedule.setStartYear(calendarReq.getStartYear());
        schedule.setStartMonth(calendarReq.getStartMonth());
        schedule.setStartDate(calendarReq.getStartDate());
        schedule.setEndYear(calendarReq.getEndYear());
        schedule.setEndMonth(calendarReq.getEndMonth());
        schedule.setEndDate(calendarReq.getEndDate());

        familyCalendarRepository.save(schedule);
    }

    public void deleteSchedule(final int calendarIdx){
        // 일정 삭제

        familyCalendarRepository.deleteById(calendarIdx);
    }

}
