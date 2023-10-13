package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.response.FriendshipDto;
import com.relex.relex_social.dto.response.ProfileDto;

import java.util.List;

public interface IFriendshipService {
    List<ProfileDto> getFriendsList(Long profileId);

    FriendshipDto sendRequest(Long profileId, Long friendId);

    List<FriendshipDto> getProfileRequests(Long profileId);

    void deleteRequest(Long profileId, Long requestId);

    List<FriendshipDto> getIncomingRequests(Long profileId);

    void replyToRequest(Long profileId, Long requestId, Boolean isApproved);

    void removeFriend(Long profileId, Long friendId);
}
