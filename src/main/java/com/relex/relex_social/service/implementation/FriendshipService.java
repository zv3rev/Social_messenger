package com.relex.relex_social.service.implementation;

import com.relex.relex_social.dto.response.FriendshipDto;
import com.relex.relex_social.dto.response.ProfileDto;
import com.relex.relex_social.entity.Friendship;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.exception.FriendshipRequestAlreadyExistException;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.exception.WrongMethodException;
import com.relex.relex_social.repository.FriendshipRepository;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.service.interfaces.IFriendshipService;
import com.relex.relex_social.utility.FriendshipUtils;
import com.relex.relex_social.utility.ProfileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class FriendshipService implements IFriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final ProfileRepository profileRepository;
    private final ProfileUtils profileUtils;
    private final FriendshipUtils friendshipUtils;


    @Override
    public List<ProfileDto> getFriendsListWithVisibilityCheck(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with  id %d wasn't found", profileId)));
        if (!profile.getIsFriendsListVisible()) {
            throw new AccessDeniedException("The user has closed access to the friends list");
        }

        return getFriendsList(profileId).stream()
                .map(profileUtils::toDto)
                .toList();
    }

    @Override
    public List<ProfileDto> getFriendsListWithoutVisibilityCheck(Long profileId) {
        return getFriendsList(profileId).stream()
                .map(profileUtils::toDto)
                .toList();
    }

    @Override
    @Transactional
    public FriendshipDto sendRequest(Long profileId, Long friendId) {
        if (!profileRepository.existsById(friendId)) {
            throw new ResourceNotFoundException(String.format("User with  id %d wasn't found", friendId));
        }

        if (friendshipRepository.existsFriendshipRequestBetweenUsers(profileId, friendId)) {
            throw new FriendshipRequestAlreadyExistException(String.format("You're already friends or have sent friendship request to user with id %d", friendId));
        }

        if (profileId.equals(friendId)) {
            throw new IllegalArgumentException("You can not add yourself in friends");
        }

        Friendship friendship = Friendship.builder()
                .senderId(profileId)
                .recipientId(friendId)
                .requestDate(new Timestamp(new Date().getTime()))
                .build();
        return friendshipUtils.toDto(friendshipRepository.save(friendship));
    }

    @Override
    public List<FriendshipDto> getProfileRequests(Long profileId) {
        List<Friendship> friendshipRequests = friendshipRepository.getFriendshipBySenderIdAndApprovedDateIsNullAndDeniedDateIsNull(profileId);
        return friendshipRequests.stream()
                .map(friendshipUtils::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteRequest(Long profileId, Long requestId) {
        Optional<Friendship> request = friendshipRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Request with id %d wasn't found", requestId));
        }
        if (!request.get().getSenderId().equals(profileId)) {
            throw new AccessDeniedException("This request does not belong to you");
        }
        if (request.get().getApprovedDate() != null) {
            throw new WrongMethodException(String.format("You are already friends with a user with id %d. Use a special method to delete friends", request.get().getRecipientId()));
        }

        friendshipRepository.deleteById(requestId);
    }

    @Override
    public List<FriendshipDto> getIncomingRequests(Long profileId) {
        List<Friendship> incomingRequests = friendshipRepository.getFriendshipByRecipientIdAndApprovedDateIsNullAndDeniedDateIsNull(profileId);
        return incomingRequests.stream()
                .map(friendshipUtils::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void replyToRequest(Long profileId, Long requestId, Boolean isApproved) {
        Optional<Friendship> request = friendshipRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Request with id %d wasn't found", requestId));
        }
        Friendship friendship = request.get();
        if (!friendship.getRecipientId().equals(profileId)) {
            throw new AccessDeniedException("This request does not belong to you");
        }

        if (isApproved) {
            friendship.setApprovedDate(new Timestamp(new Date().getTime()));
        } else {
            friendship.setDeniedDate(new Timestamp(new Date().getTime()));
        }
        friendshipRepository.save(friendship);
    }

    @Override
    public void removeFriend(Long profileId, Long friendId) {
        Friendship friendship = friendshipRepository.getFriendshipBetweenUsers(profileId, friendId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("You are not friends with id %d", friendId)));

        friendshipRepository.deleteById(friendship.getId());
    }

    private List<Profile> getFriendsList(Long profileId) {
        List<Long> friendsId = friendshipRepository.getFriends(profileId);
        Iterable<Profile> friends = profileRepository.findAllById(friendsId);
        return StreamSupport.stream(friends.spliterator(), false)
                .toList();
    }
}
