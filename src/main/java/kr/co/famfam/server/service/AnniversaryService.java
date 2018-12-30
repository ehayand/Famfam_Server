package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.model.AnniversaryReq;
import kr.co.famfam.server.model.DefaultRes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface AnniversaryService {
    DefaultRes findAll(final int authUserIdx);
    DefaultRes addAnniversary(final int anniversaryType, final AnniversaryReq anniversaryReq);
    DefaultRes deleteAnniversary(final int anniversaryIdx);
    List<Anniversary> findByYearAndMonth(final String dateStr);
    List<Anniversary> findByYearAndMonthAndDate(final String dateStr);
}


