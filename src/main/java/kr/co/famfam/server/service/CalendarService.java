package kr.co.famfam.server.service;

import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.CalendarSearchReq;
import kr.co.famfam.server.model.DefaultRes;

public interface CalendarService {

    DefaultRes findAllSchedule(final String dateStr, final int authUserIdx);

    DefaultRes findDaySchedule(final String dateStr, final int authUserIdx);

    DefaultRes addSchedule(final int calendarType, final CalendarReq calendarReq, final int authUserIdx);

    DefaultRes updateSchedule(final int calendarType, final int calendarIdx, final CalendarReq calendarReq);

    DefaultRes deleteSchedule(final int calendarType, final int calendarIdx);

    DefaultRes searchSchedule(final CalendarSearchReq calendarSearchReq, final int authUserIdx);
}
