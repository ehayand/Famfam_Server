package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.FamilyCalendarRepository;
import kr.co.famfam.server.repository.IndividualCalendarRepository;
import kr.co.famfam.server.service.FamilyCalendarService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class FamilyCalendarServiceImpl implements FamilyCalendarService {

    private final FamilyCalendarRepository familyCalendarRepository;

    public FamilyCalendarServiceImpl(FamilyCalendarRepository familyCalendarRepository){
        this.familyCalendarRepository = familyCalendarRepository;
    }

    public List<FamilyCalendar> findByYearAndMonth(final int year, final int month){

        List<FamilyCalendar> familyCalendars;

        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        return familyCalendars;
    }

    public List<FamilyCalendar> findByYearAndMonthAndDate(final int year, final int month, final int date){

        List<FamilyCalendar> familyCalendars;

        // 날짜에 맞는 일정 조회

        return familyCalendars;
    }
}
