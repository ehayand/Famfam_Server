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

    @Override
    public DefaultRes findById(int userIdx) {
        try {
            Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Optional<Mission> mission = missionRepository.findById(user.get().getMissionIdx());
            if (!mission.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_MISSION);

            Optional<User> target = userRepository.findById(user.get().getMissionTargetUserIdx());
            if (!target.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Map<String, Object> result = new HashMap<>();
            result.put("mission", mission);
            result.put("target", target.get().getUserName());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MISSION, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public Boolean updateUser(final User user) {
        try {
            List<Mission> missions = missionRepository.findAll();
            if (missions.isEmpty())
                return false;

            List<User> users = userRepository.findUsersByGroupIdxAndUserIdxIsNotIn(user.getGroupIdx(), user.getUserIdx());
            if (users.isEmpty())
                return false;

            int randomMission = 0;
            while(randomMission == 0)
                randomMission = new Random().nextInt(missions.size());

            Mission mission = missions.get(randomMission);
            User target = users.get(new Random().nextInt(users.size()));

            user.setMissionIdx(mission.getMissionIdx());
            user.setMissionTargetUserIdx(target.getUserIdx());

            userRepository.save(user);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean save(Mission mission) {
        try {
            missionRepository.save(mission);

            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
        }

        return false;
    }
}
