package de.golfgl.gdxjamgame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntArray;

import de.golfgl.gdx.controllers.ControllerMenuDialog;

public class ScoreScreen extends ControllerMenuDialog {
    private final GdxJamGame game;

    public ScoreScreen(final GdxJamGame game, IntArray scores) {
        super("", game.skin);

        this.game = game;

        Table content = getContentTable();
        content.setSkin(game.skin);
        content.defaults();

        content.add("SCORES");
        add();

        int total = 0;
        for (int level = 0; level < scores.size; level++) {
            content.row();
            content.add("Level " + (level + 1), "small").right().padRight(15);
            int levelScore = scores.get(level);
            content.add(String.valueOf(levelScore), "small");
            total += levelScore;
        }
        content.row().padTop(10);
        content.add("TOTAL").right().padRight(15);
        content.add(String.valueOf(total));

        getButtonTable().padTop(20);
        button("Close");
    }
}
