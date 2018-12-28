package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
public class IndividualCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    private Date startDate;
    private Date endDate;
    private String content;
    private int returningTime;
    private int dinner;

    // 수정 필요
    private int userIdx;
}
