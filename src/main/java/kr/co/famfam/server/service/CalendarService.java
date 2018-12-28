package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;

public interface CalendarService {
    DefaultRes findAllSchedule(int year, int month);
}
