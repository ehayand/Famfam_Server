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

    public static final String READ_CONTENT = "게시글 조회 성공";
    public static final String NOT_FOUND_CONTENT = "게시글을 찾을 수 없습니다.";
    public static final String CREATED_CONTENT = "게시글 업로드 성공";
    public static final String UPDATE_CONTENT = "게시글 수정 성공";
    public static final String DELETE_CONTENT = "게시글 삭제 성공";

    public static final String READ_PHOTO = "사진 조회 성공";
    public static final String NOT_FOUND_PHOTO = "사진을 찾을 수 없습니다.";

    public static final String UNAUTHORIZED = "권한이 없습니다.";

    public static final String NULL_POINTER = "NULL 에러";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String DB_ERROR = "데이터베이스 에러";
}
