package kr.co.famfam.server.model;


import kr.co.famfam.server.domain.History;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {

    private int userIdx;
    private int groupIdx;

    // TODO edit String to enum
    private String type;
    private String content;
    private LocalDateTime created;



    public HistoryDto(int userIdx, int groupIdx, String type) {
        this.userIdx = userIdx;
        this.groupIdx = groupIdx;
        this.type = type;
        this.created = LocalDateTime.now();
    }

    public History toEntity() {
        return History.builder()
                .userIdx(userIdx)
                .groupIdx(groupIdx)
                .type(type)
                .content(content)
                .build();
    }
}
