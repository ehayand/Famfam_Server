package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class BatchService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MissionService missionService;
    private final HistoryService historyService;

    public BatchService(GroupRepository groupRepository, UserRepository userRepository, MissionService missionService, HistoryService historyService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.missionService = missionService;
        this.historyService = historyService;
    }

    @Scheduled(cron = "0 0/3 * * * *")
    public void missionBatch() {
        List<Group> allGroups = groupRepository.findAll();
        if(allGroups.isEmpty())
            log.error("Groups Empty");

        try {
            for (Group g : allGroups) {
                if (2 > userRepository.countByGroupIdx(g.getGroupIdx())) continue;

                List<User> users = userRepository.findUsersByGroupIdx(g.getGroupIdx());
                for (User u : users) missionService.updateUser(u);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled
    public void historyBatch() {

    }
}
