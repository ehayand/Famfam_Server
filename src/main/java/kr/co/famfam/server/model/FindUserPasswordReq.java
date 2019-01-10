package kr.co.famfam.server.model;

import lombok.Data;

@Data
public class FindUserPasswordReq {

    private String userId;
    private String number;
}
