package kr.co.famfam.server.mapper;

import org.apache.ibatis.annotations.*;
import kr.co.famfam.server.dto.User;
import kr.co.famfam.server.model.SignUpReq;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Mapper
public interface UserMapper {

    // 모든 회원 리스트 조회
    @Select("SELECT * FROM user")
    List<User> findAll();

    // 회원 이름으로 조회
    @Select("SELECT * FROM user WHERE userId = #{userId}")
    User findById(@Param("userId") final String userId);

    // 회원 고유 번호로 조회
    @Select("SELECT * FROM user WHERE userIdx = #{userIdx}")
    User findByUserIdx(@Param("userIdx") final int userIdx);

    // 회원 이름과 비밀번호로 조회
    @Select("SELECT * FROM user WHERE userId = #{userId} AND userPw = #{userPw}")
    User findByIdAndPassword(@Param("userId") final String userId, @Param("userPw") final String userPw);

    // 회원 등록, Auto Increment는 회원 고유 번호
    // Auto Increment 값을 받아오고 싶으면 리턴 타입을 int(Auto Increment 컬럼 타입)으로 하면 된다.
    @Insert("INSERT INTO user(userId, userPw, userName, userNickName, userPhone, birthday, sexType) " +
            "VALUES(#{signUpReq.userId}, #{signUpReq.userPw}, #{signUpReq.userName}, " +
            "#{signUpReq.userNickName}, #{signUpReq.userPhone}, #{signUpReq.birthday}, #{signUpReq.sexType})")
    @Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    int save(@Param("signUpReq") final SignUpReq signUpReq);

    // 회원 정보 수정
    @Update("UPDATE user SET userNickName = #{user.userNickName}, birthday = #{user.birthday}, sexType = #{user.sexType}, " +
            "profilePhoto = #{user.profilePhoto}, backPhoto = #{user.backPhoto}, statusMessage = #{user.statusMessage}, userColor = #{user.userColor} " +
            "WHERE userIdx = #{userIdx}")
    void update(@Param("userIdx") final int userIdx, @Param("user") final User user);

    // 회원 삭제
    @Delete("DELETE FROM user WHERE userIdx = #{userIdx}")
    void deleteByUserIdx(@Param("userIdx") final int userIdx);
}
