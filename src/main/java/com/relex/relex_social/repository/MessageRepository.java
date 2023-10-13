package com.relex.relex_social.repository;

import com.relex.relex_social.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    String SELECT_MESSAGES_BETWEEN_USERS_QUERY =
            "SELECT m FROM Message m " +
                    "WHERE m.senderId = :id1 AND m.recipientId = :id2 OR " +
                    "m.senderId = :id2 AND m.recipientId = :id1 " +
                    "ORDER BY m.sendTime";

    @Query(SELECT_MESSAGES_BETWEEN_USERS_QUERY)
    List<Message> getMessagesBetweenUsers(@Param("id1") Long id1, @Param("id2") Long id2);
}
