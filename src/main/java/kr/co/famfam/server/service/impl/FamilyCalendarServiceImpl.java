package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.FamilyCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kr.co.famfam.server.utils.HistoryType.ADD_SCHEDULE;
import static kr.co.famfam.server.utils.PushType.PUSH_ADD_SCHEDULE;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class FamilyCalendarServiceImpl implements FamilyCalendarService {

    private final FamilyCalendarRepository familyCalendarRepository;
    private final HistoryServiceImpl historyService;
    private final UserRepository userRepository;
    private final PushServiceImpl pushService;

    public FamilyCalendarServiceImpl(FamilyCalendarRepository familyCalendarRepository, HistoryServiceImpl historyService, UserRepository userRepository, PushServiceImpl pushService) {
        this.familyCalendarRepository = familyCalendarRepository;
        this.historyService = historyService;
        this.userRepository = userRepository;
        this.pushService = pushService;
    }

    @Override
    public List<FamilyCalendar> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate, final int groupIdx) {
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회
        try {
            List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByYearAndMonth(startDate, endDate, groupIdx);

            return familyCalendars;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<FamilyCalendar> findByYearAndMonthAndDate(final String dateStr, final int groupIdx) {
        // 날짜에 맞는 일정 조회
        try {
            String per = "%";
            String tempStr = per.concat(dateStr);
            String result = tempStr.concat("%");

            List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByYearAndMonthAndDate(result, groupIdx);

            return familyCalendars;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public DefaultRes addSchedule(final CalendarReq calendarReq, final int authUserIdx, final String allDateStr) {
        // 일정 추가
        try {
            Optional<User> user = userRepository.findById(authUserIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            FamilyCalendar schedule = new FamilyCalendar();
            schedule.setUserIdx(authUserIdx);
            schedule.setContent(calendarReq.getContent());
            schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
            schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
            schedule.setAllDate(allDateStr);

            familyCalendarRepository.save(schedule);

            HistoryDto historyDto = new HistoryDto(authUserIdx, user.get().getGroupIdx(), ADD_SCHEDULE);
            historyService.add(historyDto);

            List<User> users = userRepository.findUsersByGroupIdxAndUserIdxIsNotIn(user.get().getGroupIdx(), user.get().getUserIdx());
            for(User userTemp : users){
                System.out.println(userTemp.getUserId());
                pushService.sendToDevice(userTemp.getFcmToken(), PUSH_ADD_SCHEDULE, user.get().getUserName());
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CALENDAR);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes updateSchedule(final int calendarIdx, final CalendarReq calendarReq, final String allDateStr) {
        // 일정 수정
        try {
            if (!familyCalendarRepository.findById(calendarIdx).isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR);

            FamilyCalendar schedule = familyCalendarRepository.findById(calendarIdx).get();
            if (!calendarReq.getContent().isEmpty()) schedule.setContent(calendarReq.getContent());
            if (!calendarReq.getStartDate().isEmpty())
                schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
            if (!calendarReq.getEndDate().isEmpty()) schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
            if (allDateStr != "") schedule.setAllDate(allDateStr);

            familyCalendarRepository.save(schedule);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CALENDAR);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes deleteSchedule(final int calendarIdx) {
        // 일정 삭제
        try {
            if (!familyCalendarRepository.findById(calendarIdx).isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR);

            familyCalendarRepository.deleteById(calendarIdx);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CALENDAR);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public List<FamilyCalendar> searchSchedule(final String content, final int groupIdx) {
        // 일정 검색
        try {
            List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByContetnt(content, groupIdx);

            return familyCalendars;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }
}