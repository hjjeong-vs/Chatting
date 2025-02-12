package com.KoreaItAcdemy.OurNotion.Chat.repository;

import com.KoreaItAcdemy.OurNotion.Chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    // :roomId로 파라미터를 바인딩하여 실제 값이 쿼리로 전달되도록 수정
    @Query("SELECT c.name FROM ChatRoom c WHERE c.id = :roomId")
    String findRoomNameByRoomId(@Param("roomId") String roomId);

    // userseq로 닉네임 가져오기
}
