package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findPhotosByContentIdx(int contentIdx);

    Page<Photo> findPhotosByUserIdxOrderByCreatedAtDesc(int userIdx, Pageable pageable);

    Page<Photo> findPhotosByGroupIdxOrderByCreatedAtDesc(int groupIdx, Pageable pageable);
}
