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
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "photo_seq_generator", sequenceName = "photo_seq", allocationSize = 1)
    @Column(name = "photoIdx")
    private int photoIdx;

    @Column(name = "photoName")
    private String photoName;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "contentIdx")
    private int contentIdx;
    @Column(name = "groupIdx")
    private int groupIdx;
    @Column(name = "userIdx")
    private int userIdx;

    public Photo(int contentIdx, int userIdx, int groupIdx) {
        this.createdAt = LocalDateTime.now();
        this.contentIdx = contentIdx;
        this.groupIdx = groupIdx;
        this.userIdx = userIdx;
    }
}
