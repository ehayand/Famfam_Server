package kr.co.famfam.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class CalendarReq {
    private int startYear;
    private int startMonth;
    private int startDate;
    private int endYear;
    private int endMonth;
    private int endDate;
    private String content;

    private int returningTime;
    private int dinner;
}
