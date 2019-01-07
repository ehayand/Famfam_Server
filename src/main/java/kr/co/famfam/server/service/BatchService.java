package kr.co.famfam.server.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class BatchService {

    private final UserService userService;
    private final GroupService groupService;
    private final MissionService missionService;
    private final HistoryService historyService;

    public BatchService(UserService userService, GroupService groupService, MissionService missionService, HistoryService historyService) {
        this.userService = userService;
        this.groupService = groupService;
        this.missionService = missionService;
        this.historyService = historyService;
    }

    @Scheduled
    public void missionBatch() {
    }

    @Scheduled
    public void historyBatch() {

    }
}
