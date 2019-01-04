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

    @Column
    private int userIdx;

    @Column
    private int groupIdx;

    @Column
    private String type;

    @Column
    private String content;

    @Column
    private LocalDateTime created;


    @Builder
    public History(int userIdx, int groupIdx, String type, String content, LocalDateTime created) {
        this.userIdx = userIdx;
        this.groupIdx = groupIdx;
        this.type = type;
        this.content = content;
        this.created = LocalDateTime.now();
    }
}
