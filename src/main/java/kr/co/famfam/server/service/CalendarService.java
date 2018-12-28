package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;

public interface CalendarService {
    DefaultRes findAllSchedule(int year, int month);
    DefaultRes findDaySchedule(final int year, final int month, final int date);
    DefaultRes addSchedule(final int type);
}
