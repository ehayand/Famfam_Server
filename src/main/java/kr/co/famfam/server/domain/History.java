package kr.co.famfam.server.domain;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "history_seq_generator", sequenceName = "history_seq", allocationSize = 1)
    @Column(name = "historyIdx")
    private int historyIdx;

    @Column(name = "historyType")
    private String historyType;
    @Column(name = "content")
    private String content;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "userIdx")
    private int userIdx;
    @Column(name = "groupIdx")
    private int groupIdx;

    @Builder
    public History(int userIdx, int groupIdx, String historyType, String content) {
        this.userIdx = userIdx;
        this.groupIdx = groupIdx;
        this.historyType = historyType;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
