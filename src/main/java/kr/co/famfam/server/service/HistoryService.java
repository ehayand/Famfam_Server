package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.History;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface HistoryService {
    Boolean add(HistoryDto historyDto);
//    Boolean add(int userIdx, String type);

    Boolean batchHistory(final HistoryDto historyDto);

    DefaultRes findAllHistoryByUserIdx(int userIdx);
}
