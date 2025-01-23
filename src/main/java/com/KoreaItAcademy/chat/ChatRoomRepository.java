package com.KoreaItAcademy.chat;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRooms = new LinkedHashMap<>();

    public List<ChatRoom> findAllRooms() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String id) {
        return chatRooms.get(id);
    }

    public ChatRoom createRoom(String name) {
        ChatRoom room = new ChatRoom(name);
        chatRooms.put(room.getId(), room);
        return room;
    }
}
