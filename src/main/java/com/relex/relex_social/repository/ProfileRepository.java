package com.relex.relex_social.repository;

import com.relex.relex_social.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile,Long> {
    Optional<Profile> findByNickname(String nickname);
    Boolean existsByNickname(String string);
    Boolean existsByEmail(String email);
}
