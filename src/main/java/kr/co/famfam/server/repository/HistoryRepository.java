package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    Page<History> findAllByGroupIdxAndUserIdxIsNotIn(int groupIdx, int userIdx, Pageable pageable);
}
