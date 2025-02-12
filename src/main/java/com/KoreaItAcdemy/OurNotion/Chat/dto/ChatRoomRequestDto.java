package com.KoreaItAcdemy.OurNotion.Chat.dto; // 📌 DTO 패키지

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDto {
    private String name;
    private String chatuserInfo;
    private String participants;
}
