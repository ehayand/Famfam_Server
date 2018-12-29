package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "family_calendar")
public class FamilyCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "calendar_seq_generator", sequenceName = "calendar_seq", allocationSize = 1)
    private int calendarIdx;

    private Timestamp startDate;
    private Timestamp endDate;
    private String content;

    private int userIdx;
}
