package de.golfgl.gdxjamgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen extends Table {
    private final GdxJamGame game;

    public MainMenuScreen(GdxJamGame game) {
        super(game.skin);
        this.game = game;
        setBackground(game.white);
        setColor(0, 0, 0, 1f);
        setFillParent(true);
        row();
        add("Gdx Jam Game Sept 2020").expand();

        Rotator firstRotator = new Rotator(game);
        Rotator secondRotator = new Rotator(game);

        row();
        add(firstRotator).pad(50);
        row();
        add(secondRotator).pad(50);
        row();
        add().expand();

        row();
        final TextButton playButton = new TextButton("Play", game.skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playButton.setDisabled(true);
                goToNextScreen();

            }
        });
        add(playButton).padBottom(30);

        new ActionProducer().addMainMenuSwingActions(firstRotator, secondRotator);
    }

    private void goToNextScreen() {
        addAction(Actions.sequence(Actions.fadeOut(.2f, Interpolation.fade),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        getStage().addActor(new PlayScreen(game));
                    }
                }), Actions.removeActor()));
    }
}
