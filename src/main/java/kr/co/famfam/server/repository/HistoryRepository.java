package kr.co.famfam.server.repository;

import kr.co.famfam.server.domain.History;
import kr.co.famfam.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface HistoryRepository extends JpaRepository<History, Integer> {

    List<History> findAllByGroupIdxAndUserIdxIsNotIn(int groupIdx, int userIdx);

}
