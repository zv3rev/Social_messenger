package com.relex.relex_social.repository;

import com.relex.relex_social.entity.Friendship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    String SELECT_FRIENDS_QUERY = "SELECT CASE WHEN f.recipientId=:profileId THEN f.senderId ELSE f.recipientId END " +
            "FROM Friendship f WHERE f.approvedDate IS NOT NULL";
    String SELECT_FRIENDSHIP_BETWEEN_USERS_QUERY = "SELECT f FROM Friendship f " +
            "WHERE (f.senderId = :profile1Id AND f.recipientId = :profile2Id) " +
            "OR (f.senderId = :profile2Id AND f.recipientId = :profile1Id) " +
            "AND f.approvedDate IS NOT NULL";

    String EXIST_FRIENDSHIP_REQUEST_BETWEEN_USERS_QUERY = "SELECT COUNT(f) <> 0 FROM Friendship f " +
            "WHERE (f.senderId = :profile1Id AND f.recipientId = :profile2Id) " +
            "OR (f.senderId = :profile2Id AND f.recipientId = :profile1Id) " +
            "AND f.approvedDate IS NOT NULL";

    @Query(SELECT_FRIENDS_QUERY)
    List<Long> getFriends(@Param("profileId") Long profileId);

    List<Friendship> getFriendshipBySenderIdAndApprovedDateIsNullAndDeniedDateIsNull(Long senderId);

    List<Friendship> getFriendshipByRecipientIdAndApprovedDateIsNullAndDeniedDateIsNull(Long recipientId);

    @Query(SELECT_FRIENDSHIP_BETWEEN_USERS_QUERY)
    Optional<Friendship> getFriendshipBetweenUsers(@Param("profile1Id") Long profile1Id, @Param("profile2Id") Long profile2Id);

    @Query(EXIST_FRIENDSHIP_REQUEST_BETWEEN_USERS_QUERY)
    Boolean existsFriendshipRequestBetweenUsers(@Param("profile1Id") Long profile1Id, @Param("profile2Id") Long profile2Id);
}
