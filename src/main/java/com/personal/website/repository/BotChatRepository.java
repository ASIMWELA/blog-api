package com.personal.website.repository;

import com.personal.website.entity.BotChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BotChatRepository extends JpaRepository<BotChatEntity, Long> {
    @Query(value = "SELECT * FROM bot_chat_table AS bc ORDER BY bc.message_date ASC", nativeQuery = true)
    List<BotChatEntity> getChatMessages();
}
