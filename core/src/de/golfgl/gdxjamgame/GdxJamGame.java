package de.golfgl.gdxjamgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GdxJamGame extends ApplicationAdapter {
    Skin skin;
    Stage stage;
    TextureAtlas atlas;
    Drawable white;
    boolean isPaused;

    @Override
    public void create() {
        stage = new Stage(new ExtendViewport(800, 450));
        InputMultiplexer input = new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                switch (keycode) {
                    case Input.Keys.BACK:
                    case Input.Keys.ESCAPE:
                        stage.clear();
                        prepareUI();
                        return true;
                }
                return super.keyDown(keycode);
            }
        });
        Gdx.input.setInputProcessor(input);

        prepareSkin();

        prepareUI();
    }

    private void prepareUI() {
        stage.addActor(new MainMenuScreen(this));
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
}
