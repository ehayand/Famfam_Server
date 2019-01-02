package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    Optional<Photo> findPhotoByContentIdx(int contentIdx);

    Page<Photo> findPhotosByUserIdx(int userIdx, Pageable pageable);
}
