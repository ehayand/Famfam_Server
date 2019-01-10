package kr.co.famfam.server.model;

import lombok.Data;

@Data
public class FindUserIdReq {

    private String number;
    private String birthday;
}
