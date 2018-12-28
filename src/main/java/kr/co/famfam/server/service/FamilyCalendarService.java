package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.FamilyCalendar;
import kr.co.famfam.server.model.DefaultRes;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface FamilyCalendarService {

    List<FamilyCalendar> findByYearAndMonth(int year, int month);
}
