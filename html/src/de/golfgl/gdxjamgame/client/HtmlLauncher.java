package de.golfgl.gdxjamgame.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.golfgl.gdxgamesvcs.GameJoltClient;
import de.golfgl.gdxgamesvcs.IGameServiceIdMapper;
import de.golfgl.gdxjamgame.GdxJamGame;

public class HtmlLauncher extends GwtApplication {
    private static final String GAMEJOLT_APP_ID = "";
    private static final String GAMEJOLT_PRIVATE_KEY = "";

    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        GdxJamGame game = new GdxJamGame();
        GameJoltClient gjClient = new GameJoltClient();
        gjClient.initialize(GAMEJOLT_APP_ID, GAMEJOLT_PRIVATE_KEY);
        gjClient.setUserName(com.google.gwt.user.client.Window.Location.
                getParameter(GameJoltClient.GJ_USERNAME_PARAM));
        gjClient.setUserToken(com.google.gwt.user.client.Window.Location.
                getParameter(GameJoltClient.GJ_USERTOKEN_PARAM));
        gjClient
                .setGjScoreTableMapper(new IGameServiceIdMapper<Integer>() {
                    @Override
                    public Integer mapToGsId(String independantId) {
                        // your mapping here
                        return 547345;
                    }
                })
                .setGjTrophyMapper(new IGameServiceIdMapper<Integer>() {
                    @Override
                    public Integer mapToGsId(String independantId) {
                        switch (independantId) {
							case GdxJamGame.TROPHY_PERFECT:
								return 129483;
							case GdxJamGame.TROPHY_DONE:
								return 129485;
							case GdxJamGame.TROPHY_ZERO:
								return 129484;

						}
						return null;
                    }
                });
        game.gsClient = gjClient;
        return game;
    }
}