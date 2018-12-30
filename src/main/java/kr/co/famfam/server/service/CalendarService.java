package kr.co.famfam.server.service;

import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;

public interface CalendarService {
    DefaultRes findAllSchedule(final String dateStr);
    DefaultRes findDaySchedule(final String dateStr);
    DefaultRes addSchedule(final int calendarType, final CalendarReq calendarReq, final int authUserIdx);
    DefaultRes updateSchedule(final int calendarType, final int calendarIdx, final CalendarReq calendarReq);
    DefaultRes deleteSchedule(final int calendarType, final int calendarIdx);
}
