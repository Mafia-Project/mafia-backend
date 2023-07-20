package com.poscodx.utils;

public class SocketTopicUtils {

    public static String getRoomTopic(String roomId){
        return String.format("/sub/rooms/%s", roomId);
    }

    public static String getChatTopic(String roomId){
        return String.format("/sub/chat/rooms/%s", roomId);
    }
}
