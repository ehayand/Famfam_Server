package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "individualcalendar")
public class IndividualCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "calendar_seq_generator", sequenceName = "calendar_seq", allocationSize = 1)
    private int calendarIdx;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String allDate;
    private String content;
    private int returningTime;
    private int dinner;

    private int userIdx;
}
