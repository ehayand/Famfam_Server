package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.CalendarReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.service.FamilyCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class FamilyCalendarServiceImpl implements FamilyCalendarService {

    private final FamilyCalendarRepository familyCalendarRepository;

    public FamilyCalendarServiceImpl(FamilyCalendarRepository familyCalendarRepository) {
        this.familyCalendarRepository = familyCalendarRepository;
    }

    public List<FamilyCalendar> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate) {
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회
        try {
            List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByYearAndMonth(startDate, endDate);

            return familyCalendars;
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    public List<FamilyCalendar> findByYearAndMonthAndDate(final String dateStr) {
        // 날짜에 맞는 일정 조회
        try {
            String per = "%";
            String tempStr = per.concat(dateStr);
            String result = tempStr.concat("%");

            List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByYearAndMonthAndDate(result);

            return familyCalendars;
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public DefaultRes addSchedule(final CalendarReq calendarReq, final int authUserIdx, final String allDateStr) {
        // 일정 추가
        try {
            FamilyCalendar schedule = new FamilyCalendar();
            schedule.setUserIdx(authUserIdx);
            schedule.setContent(calendarReq.getContent());
            schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
            schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
            schedule.setAllDate(allDateStr);

            familyCalendarRepository.save(schedule);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CALENDAR);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes updateSchedule(final int calendarIdx, final CalendarReq calendarReq, final String allDateStr) {
        // 일정 수정
        try {
            if (!familyCalendarRepository.findById(calendarIdx).isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR);

            FamilyCalendar schedule = familyCalendarRepository.findById(calendarIdx).get();
            schedule.setContent(calendarReq.getContent());
            schedule.setStartDate(LocalDateTime.parse(calendarReq.getStartDate()));
            schedule.setEndDate(LocalDateTime.parse(calendarReq.getEndDate()));
            schedule.setAllDate(allDateStr);

            familyCalendarRepository.save(schedule);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CALENDAR);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes deleteSchedule(final int calendarIdx) {
        // 일정 삭제
        try {
            if (!familyCalendarRepository.findById(calendarIdx).isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CALENDAR);

            familyCalendarRepository.deleteById(calendarIdx);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CALENDAR);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public List<FamilyCalendar> searchSchedule(final String content) {
        // 일정 검색
        try {
            List<FamilyCalendar> familyCalendars = familyCalendarRepository.findByContetnt(content);

            return familyCalendars;
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }
}