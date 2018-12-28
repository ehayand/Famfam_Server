package kr.co.famfam.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class CalendarReq {
    private Date startDate;
    private Date endDate;
    private String content;

    // 가족이랑 개인 같이 써도 되나?
    private int returningTime;
    private int dinner;
}
