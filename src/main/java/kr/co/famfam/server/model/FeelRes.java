package kr.co.famfam.server.model;

import lombok.Data;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-30
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class FeelRes {
    private List<Integer> types;
    private String firstUserName;
    private int feelCount;
}
