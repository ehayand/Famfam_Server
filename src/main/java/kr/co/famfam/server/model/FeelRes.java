package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.Feel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-30
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Builder
public class FeelRes {

    private List<Feel> feelTypes;
    private String firstUserName;
    private int feelCount;
}
