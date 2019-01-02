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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
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
        try{
            Optional<User> user = userRepository.findById(authUserIdx);
            if(!user.isPresent()){
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            }
            int groupIdx = user.get().getGroupIdx();

            List<Anniversary> anniversaries = anniversaryRepository.findAnniversariesByGroupIdx(groupIdx);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANNIVERSARY, anniversaries);
        }catch(Exception e){
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes addAnniversary(final int anniversaryType, final AnniversaryReq anniversaryReq){
        // 타입값에 따라 기념일 추가
        try{
            Anniversary anniversary = new Anniversary();
            if(anniversaryType == 1 || anniversaryType == 2 || anniversaryType == 3){
                anniversary.setAnniversaryType(anniversaryType);
                anniversary.setContent(anniversaryReq.getContent());
                anniversary.setDate(anniversaryReq.getDate());

                anniversaryRepository.save(anniversary);
            }else {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARYTYPE);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_ANNIVERSARY);
        }catch(Exception e){
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes deleteAnniversary(final int anniversaryIdx){
        // 기념일 삭제
        try{
            anniversaryRepository.deleteById(anniversaryIdx);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_ANNIVERSARY);
        }catch(Exception e){
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public List<Anniversary> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate){
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 기념일 조회
        try{
            List<Anniversary> anniversaries = anniversaryRepository.findByYearAndMonth(startDate, endDate);

            return anniversaries;
        }catch(Exception e){
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    public List<Anniversary> findByYearAndMonthAndDate(final String dateStr){
        // 날짜에 맞는 기념일 조회
        try {
            String tempStr = dateStr.concat("%");
            List<Anniversary> anniversaries = anniversaryRepository.findByYearAndMonthAndDate(tempStr);

            return anniversaries;
        }catch(Exception e){
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }
}

