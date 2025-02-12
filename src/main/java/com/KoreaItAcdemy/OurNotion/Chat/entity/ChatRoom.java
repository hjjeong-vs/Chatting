package com.KoreaItAcdemy.OurNotion.Chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_Chat_List")
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id", length = 36)  // 채팅방 코드 (UUID)
    private String id;

    @Column(name = "room_name", nullable = false)
    private String name; // 채팅방 명

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate; // 방 생성일

    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate; // 데이터 변경 일자

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modificationDate = LocalDateTime.now();
    }

    public ChatRoom(String name, String chatuserInfo, String participants) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }
}
