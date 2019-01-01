package kr.co.famfam.server.utils;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public class ResponseMessage {
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    public static final String NOT_FOUND_INVITATION = "인증코드가 유효하지 않습니다.";
    public static final String JOIN_SUCCESS = "그룹 참여 성공";

    public static final String READ_CONTENT = "게시글 조회 성공";
    public static final String NOT_FOUND_CONTENT = "게시글을 찾을 수 없습니다.";
    public static final String CREATED_CONTENT = "게시글 업로드 성공";
    public static final String UPDATE_CONTENT = "게시글 수정 성공";
    public static final String DELETE_CONTENT = "게시글 삭제 성공";

    public static final String READ_COMMENT = "댓글 조회 성공";
    public static final String NOT_FOUND_COMMENT = "댓글을 찾을 수 없습니다.";
    public static final String CREATED_COMMENT = "댓글 작성 성공";
    public static final String UPDATE_COMMENT = "댓글 수정 성공";
    public static final String DELETE_COMMENT = "댓글 삭제 성공";

    public static final String READ_FEEL = "감정표현 조회 성공";
    public static final String NOT_FOUND_FEEL = "감정표현을 찾을 수 없습니다.";
    public static final String CREATED_FEEL = "감정표현 추가 성공";
    public static final String UPDATE_FEEL = "감정표현 수정 성공";
    public static final String DELETE_FEEL = "감정표현 삭제 성공";

    public static final String READ_ANNIVERSARY = "기념일 조회 성공";
    public static final String NOT_FOUND_ANNIVERSARYTYPE = "잘못된 기념일 타입입니다.";
    public static final String NOT_FOUND_ANNIVERSARY = "기념일을 찾을 수 없습니다.";
    public static final String CREATED_ANNIVERSARY = "기념일 추가 성공";
    public static final String DELETE_ANNIVERSARY = "기념일 삭제 성공";

    public static final String READ_CALENDAR = "일정 조회 성공";
    public static final String NOT_FOUND_CALENDARTYPE = "잘못된 일정 타입입니다.";
    public static final String NOT_FOUND_CALENDAR = "일정을 찾을 수 없습니다.";
    public static final String CREATED_CALENDAR = "일정 추가 성공";
    public static final String UPDATE_CALENDAR = "일정 수정 성공";
    public static final String DELETE_CALENDAR = "일정 삭제 성공";

    public static final String READ_PHOTO = "사진 조회 성공";
    public static final String NOT_FOUND_PHOTO = "사진을 찾을 수 없습니다.";

    public static final String UNAUTHORIZED = "권한이 없습니다.";

    public static final String NULL_POINTER = "NULL 에러";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String DB_ERROR = "데이터베이스 에러";
}
