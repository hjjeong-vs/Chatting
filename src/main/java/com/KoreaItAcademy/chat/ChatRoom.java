package com.KoreaItAcademy.chat;

import java.util.Date;
import java.util.UUID;

public class ChatRoom {

    private String id;
    private String name;
    private Date creationDate;

    public ChatRoom(String name) {
        this.id = UUID.randomUUID().toString(); // 고유 ID 생성
        this.name = name;
        this.creationDate = new Date();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Date getCreationDate() { return creationDate; }  
}
