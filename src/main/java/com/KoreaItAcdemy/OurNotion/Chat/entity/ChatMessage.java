package com.KoreaItAcdemy.OurNotion.Chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_Chat")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키 추가

    @Column(name = "chat_room_id", nullable = false)
    private String chatRoomId;  // 🔥 기존 ChatRoom 객체 대신 String 사용

    @Column(name = "userseq", nullable = false)
    private int sender; // 🔥 sender = userseq 그대로 유지

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "sendtime", nullable = false)
    private LocalDateTime sendTime;

    @PrePersist
    protected void onCreate() {
        this.sendTime = LocalDateTime.now();
    }
}
