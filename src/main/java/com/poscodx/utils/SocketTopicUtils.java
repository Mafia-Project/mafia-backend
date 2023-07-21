package com.poscodx.utils;

public class SocketTopicUtils {

    public static final String SYSTEM_NAME = "[시스템]";
    public static final String TIME_REDUCTION_MASSAGE = "%s님이 시간을 단축시켰습니다.";
    public static final String VOTE_RESULT_NONE_MASSAGE = "투표결과 아무일도 일어나지 않았습니다.";
    public static final String VOTE_RESULT_KILL_MASSAGE = "%s님이 투표결과 죽었습니다.";
    public static final String CITIZEN_WIN = "시민이 승리했습니다.";
    public static final String MAFIA_WIN = "마피아가 승리했습니다.";
    public static String getRoomTopic(String roomId){
        return String.format("/sub/rooms/%s", roomId);
    }

    public static String getChatTopic(String roomId){
        return String.format("/sub/chat/rooms/%s", roomId);
    }
}
