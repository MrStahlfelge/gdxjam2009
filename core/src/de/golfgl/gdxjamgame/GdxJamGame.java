package de.golfgl.gdxjamgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.golfgl.gdx.controllers.ControllerMenuDialog;
import de.golfgl.gdx.controllers.ControllerMenuStage;
import de.golfgl.gdx.controllers.IControllerActable;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

public class GdxJamGame extends ApplicationAdapter {
    public static final String TROPHY_PERFECT = "perfect";
    public static final String TROPHY_ZERO = "bad";
    public static final String TROPHY_DONE = "done";

    public IGameServiceClient gsClient;
    PlayScreen playScreen;
    Skin skin;
    ControllerMenuStage stage;
    TextureAtlas atlas;
    Drawable white;
    Sound bell;
    Sound playSound;
    Sound lostSound;

    @Override
    public void create() {
        stage = new ControllerMenuStage(new ExtendViewport(800, 450)) {
            @Override
            public boolean isDefaultActionKeyCode(int keyCode) {
                if (keyCode == Input.Keys.SPACE)
                    return true;
                else
                    return super.isDefaultActionKeyCode(keyCode);
            }
        };
        Gdx.input.setInputProcessor(stage);

        prepareSkin();
        prepareSounds();

        prepareUI();

        if (gsClient == null)
            gsClient = new NoGameServiceClient();

        // establish a connection to the game service without error messages or login screens
        gsClient.resumeSession();
    }

    private void prepareSounds() {
        bell = Gdx.audio.newSound(Gdx.files.internal("sound/bell.mp3"));
        playSound = Gdx.audio.newSound(Gdx.files.internal("sound/playing.mp3"));
        lostSound = Gdx.audio.newSound(Gdx.files.internal("sound/lost.mp3"));
    }

    private void prepareUI() {
        stage.addActor(new MainMenuScreen(this));

        Button pauseButton = new PauseButton();
        stage.addActor(pauseButton);
        stage.addFocusableActor(pauseButton);
        stage.setEscapeActor(pauseButton);
    }


    private void prepareSkin() {
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but
        // strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("skin/uiskin.json"));
        white = skin.getDrawable("white");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
    }

    private class PauseButton extends Button implements IControllerActable {
        public PauseButton() {
            super(new ButtonStyle());
        }

        @Override
        public boolean onControllerDefaultKeyDown() {
            if (playScreen != null && playScreen.getStage() != null && !playScreen.isPaused()) {
                playScreen.setPaused(true);
                new PauseDialog().show(stage);
            }
            return false;
        }

        @Override
        public boolean onControllerDefaultKeyUp() {
            return false;
        }
    }

    private class PauseDialog extends ControllerMenuDialog {

        private final TextButton restart;
        private final TextButton resume;

        public PauseDialog() {
            super("", skin);
            restart = new TextButton("Leave", skin);
            resume = new TextButton("Resume", skin);

            resume.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    hide();
                    playScreen.setPaused(false);
                }
            });

            restart.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    playScreen.setPaused(false);
                    playScreen.setGameOver();
                    hide();
                }
            });

            getContentTable().add(resume).expand();
            getContentTable().row();
            getContentTable().add(restart).expand();

            addFocusableActor(resume);
            addFocusableActor(restart);
        }

        @Override
        protected Actor getConfiguredDefaultActor() {
            return resume;
        }

        @Override
        protected Actor getConfiguredEscapeActor() {
            return restart;
        }
    }
}
