package com.game.kalah.domain;

import com.game.kalah.domain.enums.GameStatus;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long gameId;
    @OneToMany
    private List<Player> player;
    @OneToOne
    private Board board;
    @UpdateTimestamp
    private LocalDateTime updateTimestamp;
    @NonNull
    private GameStatus gameStatus;
}
