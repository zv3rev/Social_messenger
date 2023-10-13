package com.relex.relex_social.service.implementation;

import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileDetailsService implements UserDetailsService {
    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByNickname(username).orElseThrow(() -> new UsernameNotFoundException("No user with this username"));
        return new User(
                profile.getNickname(),
                profile.getPassword(),
                Set.of(new SimpleGrantedAuthority("USER"))
        );
    }
}