package kr.co.famfam.server.utils.auth;

import java.lang.annotation.*;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

//메소드에 적용
@Target(ElementType.METHOD)
//런타임시까지 참조 가능
@Retention(RetentionPolicy.RUNTIME)
//Java Doc에도 표시
@Documented
//상속 가능
@Inherited
public @interface Auth {
}