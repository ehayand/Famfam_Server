package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.*;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.repository.AnniversaryRepository;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.utils.DefaultMission;
import kr.co.famfam.server.utils.HistoryType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
    private final AnniversaryRepository anniversaryRepository;
    private final FamilyCalendarRepository familyCalendarRepository;
    private final MissionService missionService;
    private final HistoryService historyService;

    public BatchService(GroupRepository groupRepository, UserRepository userRepository, AnniversaryRepository anniversaryRepository, FamilyCalendarRepository familyCalendarRepository, MissionService missionService, HistoryService historyService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.anniversaryRepository = anniversaryRepository;
        this.familyCalendarRepository = familyCalendarRepository;
        this.missionService = missionService;
        this.historyService = historyService;
    }

    /**
     * cron = "초 분 시 일 월 주(년)"
     * 초 0-59 , - * /
     * 분 0-59 , - * /
     * 시 0-23 , - * /
     * 일 1-31 , - * ? / L W
     * 월 1-12 or JAN-DEC , - * /
     * 요일 1-7 or SUN-SAT , - * ? / L #
     * 년(옵션) 1970-2099 , - * /
     * : 모든 값
     * ? : 특정 값 없음
     * - : 범위 지정에 사용
     * , : 여러 값 지정 구분에 사용
     * / : 초기값과 증가치 설정에 사용
     * L : 지정할 수 있는 범위의 마지막 값
     * W : 월~금요일 또는 가장 가까운 월/금요일
     * # : 몇 번째 무슨 요일 2#1 => 첫 번째 월요일
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void missionBatch() {
        log.info("############ Mission Batch Start : " + LocalTime.now());
        List<Group> allGroups = groupRepository.findAll();
        if (allGroups.isEmpty())
            log.error("Groups Empty");
        else {
            try {
                for (Group g : allGroups) {
                    if (2 > userRepository.countByGroupIdx(g.getGroupIdx())) continue;

                    List<User> users = userRepository.findUsersByGroupIdx(g.getGroupIdx());
                    for (User u : users) missionService.updateUser(u);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            log.info("############ Mission Batch End : " + LocalTime.now());
        }
    }

    @Scheduled(cron = "0 5 1 * * *")
    public void historyBatch() {
        log.info("############ History Batch Start : " + LocalTime.now());

        List<FamilyCalendar> allFamilyCalendars = familyCalendarRepository.findFamilyCalendarsByStartDate(LocalDateTime.of(LocalDate.now().plusDays(6), LocalTime.of(0, 0, 0)));
        if (allFamilyCalendars.isEmpty())
            log.error("FamilyCalendars Empty");
        else {
            try {
                for (FamilyCalendar f : allFamilyCalendars) {
                    Optional<User> user = userRepository.findById(f.getUserIdx());
                    if (!user.isPresent()) continue;

                    List<User> users = userRepository.findUsersByGroupIdxAndAndCalendarConsent(user.get().getGroupIdx(), 1);
                    for (User u : users)
                        historyService.batchHistory(new HistoryDto(u.getUserIdx(), u.getGroupIdx(), HistoryType.ADD_FAMILYCALENDAR_PUSH), f.getContent());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        List<Anniversary> allAnniversaries = anniversaryRepository.findAnniversariesByDate(LocalDateTime.of(LocalDate.now().plusDays(6), LocalTime.of(0, 0, 0)));
        if (allAnniversaries.isEmpty())
            log.error("Anniversaries Empty");
        else {
            try {
                for (Anniversary a : allAnniversaries) {
                    List<User> users = userRepository.findUsersByGroupIdxAndContentConsent(a.getGroupIdx(), 1);
                    if (users.isEmpty())
                        log.error("users empty");

                    for (User u : users)
                        historyService.batchHistory(new HistoryDto(u.getUserIdx(), u.getGroupIdx(), HistoryType.ADD_ANNIVERSARY_PUSH), a.getContent());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        log.info("############ History Batch End : " + LocalTime.now());
    }

    @PostConstruct
    private void missionTask() {
        DefaultMission defaultMission = new DefaultMission();

        try {
            Mission defaultZeroMission = Mission.builder()
                    .missionIdx(1)
                    .missionType(0)
                    .content("가족을 초대해보세요!")
                    .build();

            missionService.save(defaultZeroMission);

            for (Mission mission : defaultMission.getMissionList())
                missionService.save(mission);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
