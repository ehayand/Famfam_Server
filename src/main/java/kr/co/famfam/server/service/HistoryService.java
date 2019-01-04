package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.History;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface HistoryService {
    Boolean addHistory(HistoryDto historyDto);

    List<History> getHistory(int userIdx);

}
