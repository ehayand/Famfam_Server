package kr.co.famfam.server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "individual_calendar")
public class IndividualCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "calendar_seq_generator", sequenceName = "calendar_seq", allocationSize = 1)
    @Column(name = "calendarIdx")
    private int calendarIdx;

    @Column(name = "startDate")
    private LocalDateTime startDate;
    @Column(name = "endDate")
    private LocalDateTime endDate;
    @Column(name = "allDate")
    private String allDate;
    @Column(name = "content")
    private String content;
    @Column(name = "returningTime")
    private int returningTime;
    @Column(name = "dinner")
    private int dinner;

    @Column(name = "userIdx")
    private int userIdx;
}
