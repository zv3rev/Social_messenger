package com.relex.relex_social.service.implementation;

import com.relex.relex_social.dto.request.SendMessageRequest;
import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.dto.response.ProfileDto;
import com.relex.relex_social.entity.AllowedToSend;
import com.relex.relex_social.entity.Message;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.exception.SendingRestrictionException;
import com.relex.relex_social.repository.MessageRepository;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.service.interfaces.IFriendshipService;
import com.relex.relex_social.service.interfaces.IMessageService;
import com.relex.relex_social.utility.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final MessageRepository messageRepository;
    private final MessageUtils messageUtils;
    private final ProfileRepository profileRepository;
    private final IFriendshipService friendshipService;

    @Override
    @Transactional
    public Long send(Long senderId, String recipientNickname, SendMessageRequest sendMessageRequest) {
        Profile recipient = profileRepository.findByNickname(recipientNickname)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with nickname %s not found", recipientNickname)));
        List<Long> recipientFriendIds = friendshipService.getFriendsListWithoutVisibilityCheck(recipient.getId()).stream()
                .map(ProfileDto::getId)
                .toList();

        if (recipient.getAllowedToSend() == AllowedToSend.NONE) {
            throw new SendingRestrictionException("The user has disabled receiving messages");
        }
        if (!recipientFriendIds.contains(senderId) && recipient.getAllowedToSend() == AllowedToSend.FRIENDS) {
            throw new SendingRestrictionException("The user has restricted receiving messages only from friends");
        }

        Message message = Message.builder()
                .senderId(senderId)
                .recipientId(recipient.getId())
                .sendTime(new Timestamp(new Date().getTime()))
                .messageText(sendMessageRequest.getMessage())
                .build();
        return messageRepository.save(message).getId();
    }

    @Override
    @Transactional
    public List<MessageDto> getConversation(Long profileId, String chatPartnerNickname) {
        Profile chatPartner = profileRepository.findByNickname(chatPartnerNickname).orElseThrow(() -> new ResourceNotFoundException(String.format("User with nickname %s not found", chatPartnerNickname)));

        return messageRepository.getMessagesBetweenUsers(profileId, chatPartner.getId()).stream()
                .map(messageUtils::toDto).collect(Collectors.toList());
    }

}
