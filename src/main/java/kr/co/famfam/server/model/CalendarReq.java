package kr.co.famfam.server.model;

import lombok.Data;


@Data
public class CalendarReq {

    private String startDate;
    private String endDate;
    private String content;

    private int returningTime = -1;
    private int dinner = -1;
}
