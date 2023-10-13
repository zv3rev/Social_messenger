package com.relex.relex_social.controller;

import com.relex.relex_social.dto.response.FriendshipDto;
import com.relex.relex_social.service.interfaces.IAuthService;
import com.relex.relex_social.service.interfaces.IFriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final IAuthService authService;
    private final IFriendshipService friendshipService;


    @GetMapping
    public ResponseEntity getFriends() {
        Long profileId = authService.getAuthId();
        return ResponseEntity.ok(friendshipService.getFriendsList(profileId));
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity removeFriend(@PathVariable Long friendId) {
        Long profileId = authService.getAuthId();
        friendshipService.removeFriend(profileId, friendId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests")
    public ResponseEntity sendFriendshipRequest(@RequestParam Long friendId) {
        Long profileId = authService.getAuthId();
        FriendshipDto request = friendshipService.sendRequest(profileId, friendId);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .path("/{id}")
                                .buildAndExpand(request.getId())
                                .toUri())
                .build();
    }

    @GetMapping("/requests")
    public ResponseEntity getRequests() {
        Long profileId = authService.getAuthId();
        List<FriendshipDto> requests = friendshipService.getProfileRequests(profileId);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity deleteRequest(@PathVariable Long requestId) {
        Long profileId = authService.getAuthId();
        friendshipService.deleteRequest(profileId, requestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/responses")
    public ResponseEntity getIncomingRequests() {
        Long profileId = authService.getAuthId();
        List<FriendshipDto> incomingRequests = friendshipService.getIncomingRequests(profileId);
        return ResponseEntity.ok(incomingRequests);
    }

    //TODO: норм ли использовать bool в параметрах
    @PatchMapping("/responses/{requestId}")
    public ResponseEntity replyToRequest(@PathVariable Long requestId, @RequestParam Boolean isApproved) {
        Long profileId = authService.getAuthId();
        friendshipService.replyToRequest(profileId, requestId, isApproved);
        return ResponseEntity.ok().build();
    }
}
