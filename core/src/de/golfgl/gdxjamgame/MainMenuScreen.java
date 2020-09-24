package de.golfgl.gdxjamgame;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen extends Table {
    private final GdxJamGame game;
    private final TextButton playButton;

    public MainMenuScreen(final GdxJamGame game) {
        super(game.skin);
        this.game = game;
        setBackground(game.white);
        setColor(0, 0, 0, 1f);
        setFillParent(true);
        row();
        add("Gdx Jam Game Sept 2020").expand();
        row();
        add("PARALLEL WORLDS").expand();

        Rotator firstRotator = new Rotator(game);
        Rotator secondRotator = new Rotator(game);

        row();
        add(firstRotator).pad(50);
        row();
        add(secondRotator).pad(50);
        row();
        add().expand();

        row();
        playButton = new TextButton("Play", game.skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playButton.setDisabled(true);
                game.bell.play();
                goToNextScreen();

            }
        });
        add(playButton).padBottom(30);
        game.stage.addFocusableActor(playButton);

        new ActionProducer().addMainMenuSwingActions(firstRotator, secondRotator);
    }

    private void goToNextScreen() {
        addAction(Actions.sequence(Actions.fadeOut(.2f, Interpolation.fade),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        PlayScreen playScreen = new PlayScreen(game);
                        game.playScreen = playScreen;
                        getStage().addActor(playScreen);
                    }
                }), Actions.removeActor()));
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            game.stage.setFocusedActor(playButton);
        }
    }
}
