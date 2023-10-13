package com.relex.relex_social.repository;

import com.relex.relex_social.entity.JwtToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtToken, Long> {
    void deleteByProfileId(Long profileId);

    boolean existsByToken(String token);
}
