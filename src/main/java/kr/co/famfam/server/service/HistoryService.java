package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import org.springframework.data.domain.Pageable;


public interface HistoryService {
    Boolean add(HistoryDto historyDto);
//    Boolean add(int userIdx, String type);

    Boolean batchHistory(final HistoryDto historyDto, String content);

    DefaultRes findAllHistoryByUserIdx(int userIdx, Pageable pageable);

}
