package it.polimi.ingsw.model.modelview;

import it.polimi.ingsw.model.Player;

public class PlayerView {
    private final String name;
    private final int score;
    private final ShelfView shelfView;
    private final PersonalObjCardView personalObjCardView;

    public PlayerView(Player player) {
        this.name = player.getNickname();
        this.score = player.getScore();
        this.shelfView = new ShelfView(player.getMyShelf());
        this.personalObjCardView = new PersonalObjCardView(player.getMyPersonalOBjCard());
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public ShelfView getShelfView() {
        return shelfView;
    }

    public PersonalObjCardView getPersonalObjCardView() {
        return personalObjCardView;
    }
}
