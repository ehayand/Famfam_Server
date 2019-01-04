package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.AnniversaryDeleteReq;
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

    public AnniversaryServiceImpl(AnniversaryRepository anniversaryRepository, UserRepository userRepository) {
        this.anniversaryRepository = anniversaryRepository;
        this.userRepository = userRepository;
    }

    public DefaultRes findAll(final int authUserIdx) {
        // groupIdx로 기념일 조회
        try {
            Optional<User> user = userRepository.findById(authUserIdx);
            if (!user.isPresent()) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            }
            int groupIdx = user.get().getGroupIdx();

            List<Anniversary> anniversaries = anniversaryRepository.findAnniversariesByGroupIdx(groupIdx);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANNIVERSARY, anniversaries);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes addAnniversary(final int anniversaryType, final AnniversaryReq anniversaryReq, final int authUserIdx) {
        // 타입값에 따라 기념일 추가
        try {
            Optional<User> user = userRepository.findById(authUserIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Anniversary anniversary = new Anniversary();
            if (anniversaryType == 1 || anniversaryType == 3) {
                anniversary.setAnniversaryType(anniversaryType);
                anniversary.setContent(anniversaryReq.getContent());
                anniversary.setDate(anniversaryReq.getDate());
                anniversary.setGroupIdx(user.get().getGroupIdx());

                anniversaryRepository.save(anniversary);
            } else if (anniversaryType == 2) {
                anniversary.setAnniversaryType(anniversaryType);
                anniversary.setContent("결혼기념일");
                anniversary.setDate(anniversaryReq.getDate());
                anniversary.setGroupIdx(user.get().getGroupIdx());

                anniversaryRepository.save(anniversary);
            } else {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARY_TYPE);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_ANNIVERSARY);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes updateAnniversary(final int anniversaryIdx, final AnniversaryReq anniversaryReq) {
        // 기념일 수정
        try {
            Optional<Anniversary> anniversary = anniversaryRepository.findById(anniversaryIdx);
            if (!anniversary.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARY);
            int anniversaryType = anniversary.get().getAnniversaryType();

            if (anniversaryType == 1 || anniversaryType == 3) {
                anniversary.get().setDate(anniversaryReq.getDate());

                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_ANNIVERSARY);
            } else {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARY_TYPE);
            }
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes deleteAnniversary(final AnniversaryDeleteReq anniversaryDeleteReq) {
        // 기념일 삭제
        try {
            if (anniversaryDeleteReq.getCount() != anniversaryDeleteReq.getAnniversaryIdx().length)
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_MATCH_ANNIVERSARY_COUNT);

            int[] indexTemp = new int[anniversaryDeleteReq.getCount()];
            for (int i = 0; i < anniversaryDeleteReq.getCount(); i++) {
                // 기념일 타입/인덱스 확인
                int anniversaryIdx = anniversaryDeleteReq.getAnniversaryIdx()[i];
                Optional<Anniversary> anniversary = anniversaryRepository.findById(anniversaryIdx);
                if (!anniversary.isPresent())
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARY);

                int anniversaryType = anniversary.get().getAnniversaryType();
                System.out.println(anniversaryType);
                if (anniversaryType != 1 && anniversaryType != 3)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARY_TYPE);

                indexTemp[i] = anniversaryIdx;
            }

            for (int idx : indexTemp)
                anniversaryRepository.deleteById(idx);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_ANNIVERSARY);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public List<Anniversary> findByYearAndMonth(final LocalDateTime startDate, final LocalDateTime endDate) {
        // 년, 월에 맞는 (앞달, 뒷달 포함)세달치 기념일 조회
        try {
            List<Anniversary> anniversaries = anniversaryRepository.findByYearAndMonth(startDate, endDate);

            return anniversaries;
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    public List<Anniversary> findByYearAndMonthAndDate(final String dateStr) {
        // 날짜에 맞는 기념일 조회
        try {
            String tempStr = dateStr.concat("%");
            List<Anniversary> anniversaries = anniversaryRepository.findByYearAndMonthAndDate(tempStr);

            return anniversaries;
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<Anniversary> searchSchedule(final String content) {
        // 일정 검색
        try {
            List<Anniversary> anniversaries = anniversaryRepository.findByContetnt(content);

            return anniversaries;
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }
}