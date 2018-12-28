package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.AnniversaryRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.AnniversaryService;
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


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    public DefaultRes addAnniversary(final int anniversaryType){
        // 타입값에 따라 기념일 추가

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    public DefaultRes deleteAnniversary(final int anniversaryIdx){
        // 기념일 삭제

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER);
    }

    public List<Anniversary> findByYearAndMonth(final int year, final int month){

        List<Anniversary> anniversaries;

        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 일정 조회

        return anniversaries;
    }

    public List<Anniversary> findByYearAndMonthAndDate(final int year, final int month, final int date){

        List<Anniversary> anniversaries;

        // 날짜에 맞는 일정 조회

        return anniversaries;
    }

}

