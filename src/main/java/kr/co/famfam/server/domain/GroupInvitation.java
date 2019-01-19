package kr.co.famfam.server.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-30
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Builder
@RedisHash("invitation")
public class GroupInvitation {

    @Id
    private String code;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    @Indexed
    private int groupIdx;
}
