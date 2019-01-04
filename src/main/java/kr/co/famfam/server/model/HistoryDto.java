package kr.co.famfam.server.model;


import kr.co.famfam.server.domain.History;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDto {

    private int userIdx;
    private int groupIdx;
    private String type;
    private String content;
    private LocalDateTime created;


    public HistoryDto(int userIdx, int groupIdx, String type, String content, LocalDateTime created) {
        this.userIdx = userIdx;
        this.groupIdx = groupIdx;
        this.type = type;
        this.content = content;
        this.created = LocalDateTime.now();
    }

    public History toEntity() {
        return History.builder()
                .userIdx(userIdx)
                .groupIdx(groupIdx)
                .type(type)
                .content(content)
                .created(created)
                .build();
    }
}
