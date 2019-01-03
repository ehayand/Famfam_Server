package kr.co.famfam.server.model;

import lombok.Data;

/**
 * Created by ehay@naver.com on 2019-01-03
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class FeelReq {

    private int type;

    private int userIdx;
    private int contentIdx;
}
