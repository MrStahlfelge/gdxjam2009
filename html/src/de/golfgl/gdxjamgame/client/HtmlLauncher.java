package de.golfgl.gdxjamgame.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.golfgl.gdxjamgame.GdxJamGame;

public class HtmlLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
		return config;
	}

	@Override
	public ApplicationListener createApplicationListener() {
		GdxJamGame game = new GdxJamGame();
		return game;
	}
}