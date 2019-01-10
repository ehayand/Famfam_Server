package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ehay@naver.com on 2019-01-10
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes {

    int groupIdx;
    String groupId;

    public GroupRes(Group group) {
        this.groupIdx = group.getGroupIdx();
        this.groupId = group.getGroupId();
    }
}
