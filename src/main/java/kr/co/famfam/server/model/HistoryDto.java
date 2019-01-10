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

    // TODO edit String to enum
    private String historyType;
    private String content;
    private LocalDateTime createdAt;

    private int userIdx;
    private int groupIdx;

    public HistoryDto(int userIdx, int groupIdx, String historyType) {
        this.userIdx = userIdx;
        this.groupIdx = groupIdx;
        this.historyType = historyType;
        this.createdAt = LocalDateTime.now();
    }

    public History toEntity() {
        return History.builder()
                .userIdx(userIdx)
                .groupIdx(groupIdx)
                .historyType(historyType)
                .content(content)
                .build();
    }
}
