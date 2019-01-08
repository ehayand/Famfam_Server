package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.History;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface HistoryService {
    Boolean add(HistoryDto historyDto);
//    Boolean add(int userIdx, String type);

    DefaultRes findAllHistoryByUserIdx(int userIdx, Pageable pageable);

}
