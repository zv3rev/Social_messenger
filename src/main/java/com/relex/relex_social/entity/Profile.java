package com.relex.relex_social.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email;
    String password;
    String nickname;
    String firstName;
    String surname;
    String bio;
    ProfileStatus profileStatus;
    AllowedToSend allowedToSend;
    Boolean isFriendsListVisible;
}
