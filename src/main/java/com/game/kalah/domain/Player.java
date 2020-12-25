package com.game.kalah.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    Integer category;
}
