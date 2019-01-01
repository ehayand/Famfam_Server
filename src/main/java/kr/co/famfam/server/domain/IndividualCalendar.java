package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "individual_calendar")
public class IndividualCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "calendar_seq_generator", sequenceName = "calendar_seq", allocationSize = 1)
    @Column(name = "calendar_idx")
    private int calendarIdx;

    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "all_date")
    private String allDate;
    private String content;

    @Column(name = "returning_time")
    private int returningTime;
    private int dinner;

    @Column(name = "user_idx")
    private int userIdx;
}
