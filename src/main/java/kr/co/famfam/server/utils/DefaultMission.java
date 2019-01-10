package kr.co.famfam.server.utils;

import kr.co.famfam.server.domain.Mission;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ehay@naver.com on 2019-01-08
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public class DefaultMission {

    private List<Mission> missions;

    public List<Mission> getMissionList() {
        missions = new LinkedList<>();

        int index = 2;

        for (String s : getDefaultMission())
            missions.add(create(s, index++));

        return missions;
    }

    private Mission create(String text, int index) {
        int missionType = 1;
        int suffixType = 0;
        String content;

        String[] temp = text.split("-");

        if ("하트".equals(temp[0])) missionType = 1;
        else if ("요리".equals(temp[0])) missionType = 2;
        else if ("선물상자".equals(temp[0])) missionType = 3;
        else if ("음악".equals(temp[0])) missionType = 4;
        else if ("책".equals(temp[0])) missionType = 5;
        else if ("카메라".equals(temp[0])) missionType = 6;
        else if ("편지".equals(temp[0])) missionType = 7;

        if ("님에게".equals(temp[1])) suffixType = 1;
        else if ("님과".equals(temp[1])) suffixType = 2;

        content = temp[2];

        return Mission.builder()
                .missionIdx(index)
                .missionType(missionType)
                .suffixType(suffixType)
                .content(content)
                .build();
    }

    private String[] getDefaultMission() {
        String[] defaultMission = {
                "하트-님에게-5분동안 어깨마사지 하기",
                "요리-님에게-간단한 요리해주기",
                "선물상자-님에게-꽃 한송이 선물하기",
                "선물상자-님에게-소소한 선물주기",
                "선물상자-님에게-좋아하는 음료 선물하기",
                "음악-님에게-좋아하는 음악 물어보기",
                "음악-님에게-좋아하는 음악 추천하기",
                "책-님에게-책 선물하기",
                "카메라-님에게-인생샷 찍어 주기",
                "카메라-님에게-본인의 현재 사진 보내기",
                "편지-님에게-“고마워요”라는 한마디",
                "편지-님에게-“사랑해요”라는 한마디",
                "편지-님에게-“덕분에 행복해요”",
                "편지-님에게-짧은 편지 써서 전달하기",
                "편지-님에게-깜짝 연락하기",
                "편지-님에게-좋아하는 음식 물어보기",
                "하트-님과-간단한 보드게임하기",
                "하트-님과-하루 일상 공유하기",
                "하트-님과-만나면 포옹하기",
                "하트-님과-산책하기",
                "하트-님과-함께 영화 한편보기",
                "하트-님과-1시간 추억 만들기",
                "편지-님과-좋은 글귀 공유하기",
                "요리-님과-같이 저녁식사하기",
                "카메라-님과-함께 사진찍기"
        };

        return defaultMission;
    }
}
