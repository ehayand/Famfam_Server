package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.History;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.repository.HistoryRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.HistoryService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import kr.co.famfam.server.utils.HistoryType;
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

    public Boolean add(final HistoryDto historyDto) {
        try {

            int userIdx = historyDto.getUserIdx();


            User user = userRepository.findByUserIdx(userIdx);
            String userName = user.getUserName();
            int groupIdx = user.getGroupIdx();


            StringBuilder stb = new StringBuilder();
            stb.append(userName);


            switch (historyDto.getType()) {
                case HistoryType.ADD_SCHEDULE:
                    stb.append(" 님이 일정을 추가했습니다.");
                    break;
                case HistoryType.ADD_EMOTION:
                    stb.append(" 님이 감정을 표현했습니다.");
                   break;
                case HistoryType.ADD_CONTENT:
                    stb.append(" 님이 글을 추가했습니다.");
                    break;
                case HistoryType.ADD_COMMENT:
                    stb.append(" 님이 댓글을 추가했습니다.");
                    break;
                case HistoryType.ADD_ANNIVERSARY:
                    stb.append(" 님이 기념일을 추가했습니다.");
                    break;
            }

            historyDto.setGroupIdx(groupIdx);
            historyDto.setContent(stb.toString());

            History history = historyDto.toEntity();

            historyRepository.save(history);

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    public DefaultRes findAllHistoryByUserIdx(int userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        int groupIdx = user.getGroupIdx();

        List<History> history = historyRepository.findAllByGroupIdxAndUserIdxIsNotIn(groupIdx, userIdx);


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_HISTORY, history);

    }
}
