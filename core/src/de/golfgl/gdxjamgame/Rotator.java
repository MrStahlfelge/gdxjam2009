package de.golfgl.gdxjamgame;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class Rotator extends Image {
    public Rotator(GdxJamGame game) {
        super(game.white);
        setScaling(Scaling.stretch);
    }

    @Override
    public float getPrefHeight() {
        return 4;
    }

    @Override
    public float getPrefWidth() {
        return 400;
    }

    @Override
    public float getMinHeight() {
        return getPrefHeight();
    }

    @Override
    public float getMinWidth() {
        return getPrefWidth();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
    }
}
