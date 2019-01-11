package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;
import org.springframework.data.domain.Pageable;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface PhotoService {

    DefaultRes findPhotosByUserIdx(int userIdx, Pageable pageable);

    DefaultRes findPhotosByGroupIdx(int userIdx, Pageable pageable);
}
