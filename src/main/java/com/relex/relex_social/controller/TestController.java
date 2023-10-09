package com.relex.relex_social.controller;

import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {
    private ProfileRepository profileRepository;

    @GetMapping("/{id}")
    public String findProfile(@PathVariable Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isPresent()){
            return profile.get().getNickname();
        }
        else {
            return "Нет такого пользователя";
        }
    }
}
