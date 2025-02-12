package com.KoreaItAcdemy.OurNotion.Chat.service;

import com.KoreaItAcdemy.OurNotion.Chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository; // JPA Repository 주입

    public String getRoomNameById(String roomId) {
        return chatRoomRepository.findRoomNameByRoomId(roomId);
    }
}
