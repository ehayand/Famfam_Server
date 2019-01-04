package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.History;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.repository.HistoryRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;



@Slf4j
@Service
public class HistoryServiceImpl implements HistoryService {

    private HistoryRepository historyRepository;
    private UserRepository userRepository;


    public HistoryServiceImpl(HistoryRepository historyRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    public Boolean addHistory(final HistoryDto historyDto) {
        try {

            History history = historyDto.toEntity();

            historyRepository.save(history);

            return true;
        } catch (Exception e) {


            return false;
        }
    }


    public List<History> getHistory(int userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        int groupIdx = user.getGroupIdx();

        List<History> history = historyRepository.findAllByGroupIdxAndUserIdxIsNotIn(groupIdx, userIdx);



        return history;

    }
}
