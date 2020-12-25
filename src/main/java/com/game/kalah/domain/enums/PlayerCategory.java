package com.game.kalah.domain.enums;

import javax.persistence.Embeddable;

@Embeddable
public enum PlayerCategory {
    HUMAN(1),
    COMPUTER(2);

    private int category;

    PlayerCategory(int playerCategory) {
        category = playerCategory;
    }

    public int getCategory() {
        return this.category;
    }
}
