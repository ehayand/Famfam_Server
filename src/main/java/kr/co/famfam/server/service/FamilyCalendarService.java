package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.CalendarReq;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface FamilyCalendarService {

    List<FamilyCalendar> findByYearAndMonth(final String dateStr);
    List<FamilyCalendar> findByYearAndMonthAndDate(final String dateStr);
    void addSchedule(final CalendarReq calendarReq, final int authUserIdx);
    void updateSchedule(final int calendarIdx, final CalendarReq calendarReq);
    void deleteSchedule(final int calendarIdx);

}
