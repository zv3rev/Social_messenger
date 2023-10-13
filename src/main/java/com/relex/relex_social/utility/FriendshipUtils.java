package com.relex.relex_social.utility;

import com.relex.relex_social.dto.response.FriendshipDto;
import com.relex.relex_social.entity.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipUtils {
    public FriendshipDto toDto(Friendship friendship){
        return new FriendshipDto(
                friendship.getId(),
                friendship.getSenderId(),
                friendship.getRecipientId(),
                friendship.getRequestDate(),
                friendship.getApprovedDate(),
                friendship.getDeniedDate());
    }
}
