package kr.co.famfam.server.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnniversaryReq {

    private String content;
    private LocalDateTime date;

    private int groupIdx;
}
