package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Mission;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.MissionRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.MissionService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class MissionServiceImpl implements MissionService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;

    public MissionServiceImpl(UserRepository userRepository, MissionRepository missionRepository) {
        this.userRepository = userRepository;
        this.missionRepository = missionRepository;
    }

    public DefaultRes findById(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if(!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Optional<Mission> mission = missionRepository.findById(user.get().getMissionIdx());
        if(!mission.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_MISSION);

        Optional<User> target = userRepository.findById(user.get().getMissionTargetUserIdx());
        if(!target.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Map<String, Object> result = new HashMap<>();
        result.put("mission", mission);
        result.put("targetUser", target);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MISSION, result);
    }

    @Transactional
    public Boolean updateUser(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return false;

        List<Mission> missions = missionRepository.findAll();
        if (missions.isEmpty())
            return false;

        List<User> users = userRepository.findUsersByGroupIdxAndUserIdxIsNotIn(user.get().getGroupIdx(), user.get().getUserIdx());
        if (users.isEmpty())
            return false;

        Mission mission = missions.get(new Random().nextInt(missions.size()));
        User target = users.get(new Random().nextInt(users.size()));

        user.get().setMissionIdx(mission.getMissionIdx());
        user.get().setMissionTargetUserIdx(target.getUserIdx());

        try {
            userRepository.save(user.get());
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
        }

        return false;
    }

    @Transactional
    public Boolean save(String text) {
        int missionType = 0;
        int suffixType = 0;
        String content;

        String[] temp = text.split(" ");
        if(temp == null || temp.length != 3) return false;

        if ("하트".equals(temp[0])) missionType = 1;
        else if ("요리".equals(temp[0])) missionType = 2;
        else if ("선물상자".equals(temp[0])) missionType = 3;
        else if ("음악".equals(temp[0])) missionType = 4;
        else if ("책".equals(temp[0])) missionType = 5;
        else if ("카메라".equals(temp[0])) missionType = 6;
        else if ("편지".equals(temp[0])) missionType = 7;

        if ("님에게".equals(temp[1])) suffixType = 1;
        else if ("님과".equals(temp[1])) suffixType = 2;

        content = temp[2];

        try {
            missionRepository.save(new Mission(missionType, suffixType, content));
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
        }

        return false;
    }
}
