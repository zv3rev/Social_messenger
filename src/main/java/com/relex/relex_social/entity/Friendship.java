package com.relex.relex_social.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "friendship")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long senderId;
    Long recipientId;
    Timestamp requestDate;
    Timestamp approvedDate;
    Timestamp deniedDate;


}
