package org.anomek.omegasim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.anomek.omegasim.scenarios.MenuScreen;
import org.anomek.omegasim.scenarios.SimScreen;
import org.anomek.omegasim.scenarios.partysynergy.PartySynergyScenario;


public class OmegaSim extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shape;
    public BitmapFont font;

    PartySynergyScenario scenario;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        scenario = new PartySynergyScenario();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        setMenuScreen();
    }

    void setMenuScreen() {
        MenuScreen menuScreen = scenario.getMenu();
        menuScreen.setOnStartCallback(this::setSimScreen);
        setScreen(menuScreen);
    }

    void setSimScreen(Object settings) {
        SimScreen screen = scenario.getSim(settings, this);
        screen.registerCallbacks(new SimScreen.Callbacks() {
            @Override
            public void restart() {
                setSimScreen(settings);
            }

            @Override
            public void back() {
                setMenuScreen();
            }
        });
        setScreen(screen);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose();
    }
}
