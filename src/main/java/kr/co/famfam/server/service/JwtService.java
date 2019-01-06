package kr.co.famfam.server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static com.auth0.jwt.JWT.require;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class JwtService {

    @Value("${JWT.ISSUER}")
    private String ISSUER;

    @Value("${JWT.SECRET}")
    private String SECRET;

    /**
     * 토큰 생성
     *
     * @param user_idx 토큰에 담길 로그인한 사용자의 회원 고유 IDX
     * @return 토큰
     */
    public String create(final int user_idx) {
        try {
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("user_idx", user_idx);
            b.withExpiresAt(Date.from(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC)));
            return b.sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException JwtCreationException) {
            log.info(JwtCreationException.getMessage());
        }
        return null;
    }

    /**
     * 토큰 해독
     *
     * @param token 토큰
     * @return 로그인한 사용자의 회원 고유 IDX
     */
    public Token decode(final String token) {
        try {
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            Date expiresAt = decodedJWT.getExpiresAt();
            if(LocalDateTime.now().isAfter(LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneOffset.UTC)))
                return new Token();

            return new Token(decodedJWT.getClaim("user_idx").asLong().intValue());
        } catch (JWTVerificationException jve) {
            log.error(jve.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Token();
    }

    public boolean checkAuth(final String header, final int userIdx) {
        return decode(header).getUser_idx() == userIdx;
    }

    public static class Token {
        //토큰에 담길 정보 필드
        //초기값을 -1로 설정함으로써 로그인 실패시 -1반환
        private int user_idx = -1;

        public Token() {
        }

        public Token(final int user_idx) {
            this.user_idx = user_idx;
        }

        public int getUser_idx() {
            return user_idx;
        }

    }

    //반환될 토큰Res
    public static class TokenRes {
        //실제 토큰
        private String token;

        public TokenRes() {
        }

        public TokenRes(final String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
