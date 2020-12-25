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
public class Board implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long boardId;
    private String boardState;
}
