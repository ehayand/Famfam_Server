package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.IndividualCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface IndividualCalendarService {

    List<IndividualCalendar> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate);

    List<IndividualCalendar> findByYearAndMonthAndDate(final String dateStr);

    DefaultRes addSchedule(final CalendarReq calendarReq, final int authUserIdx, final String allDateStr);

    DefaultRes updateSchedule(final int calendarIdx, final CalendarReq calendarReq, final String allDateStr);

    DefaultRes deleteSchedule(final int calendarIdx);

    List<IndividualCalendar> searchSchedule(final String content);
}
