package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.AnniversaryReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.AnniversaryRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.AnniversaryService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class AnniversaryServiceImpl implements AnniversaryService {

    private final AnniversaryRepository anniversaryRepository;
    private final UserRepository userRepository;

    public AnniversaryServiceImpl(AnniversaryRepository anniversaryRepository, UserRepository userRepository){
        this.anniversaryRepository = anniversaryRepository;
        this.userRepository = userRepository;
    }

    public DefaultRes findAll(final int authUserIdx){
        // groupIdx로 기념일 조회

        Optional<User> user = userRepository.findById(authUserIdx);
        int groupIdx = user.get().getGroupIdx();

        List<Anniversary> anniversaries = anniversaryRepository.findAnniversariesByGroupIdx(groupIdx);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, anniversaries);
    }

    public DefaultRes addAnniversary(final int anniversaryType, final AnniversaryReq anniversaryReq){
        // 타입값에 따라 기념일 추가

        Anniversary anniversary = new Anniversary();

        if(anniversaryType == 1 || anniversaryType == 2 || anniversaryType == 3){
            anniversary.setAnniversaryType(anniversaryReq.getAnniversaryType());
            anniversary.setContent(anniversaryReq.getContent());
            anniversary.setDate(anniversaryReq.getDate());

            anniversaryRepository.save(anniversary);
        }else {
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_CONTENT);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    public DefaultRes deleteAnniversary(final int anniversaryIdx){
        // 기념일 삭제

        anniversaryRepository.deleteById(anniversaryIdx);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    //    public String allDate(final CalendarReq calendarReq){
//
//        String startDateStr = calendarReq.getStartDate();
//        String endDateStr = calendarReq.getEndDate();
//
//        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
//        LocalDateTime endDate = LocalDateTime.parse(endDateStr);
//
//        ArrayList<String> allDate = new ArrayList<>();
//
//        LocalDateTime tempDate = startDate;
//        while (tempDate.compareTo(endDate) <= 0) {
//           allDate.add(tempDate.toString());
//           tempDate = tempDate.plusDays(1);
//        }
//
//        return allDate.toString();
//    }
    public List<Anniversary> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate){
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 기념일 조회

        List<Anniversary> anniversaries = anniversaryRepository.findByYearAndMonth(startDate, endDate);

        return anniversaries;
    }

    public List<Anniversary> findByYearAndMonthAndDate(final String dateStr){
        // 날짜에 맞는 기념일 조회

        String tempStr = dateStr.concat("%");
        List<Anniversary> anniversaries = anniversaryRepository.findByYearAndMonthAndDate(tempStr);

        return anniversaries;
    }

}

