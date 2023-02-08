package org.anomek.omegasim.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.OmegaSim;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public class Player implements Entity {

    public Bounds bounds = new Bounds();
    public Texture texture;
    public boolean control = true;

    float speed = 0.15f;
    long lastUpdate;

    private final static Map<Integer, Vector2> MOVEMENT;

    static {
        MOVEMENT = new HashMap<>();
        MOVEMENT.put(Keys.A, new Vector2(-1, 0));
        MOVEMENT.put(Keys.D, new Vector2(1, 0));
        MOVEMENT.put(Keys.W, new Vector2(0, 1));
        MOVEMENT.put(Keys.S, new Vector2(0, -1));
    }

    @Override
    public int layer() {
        return Layers.SELF;
    }

    public void update(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
        }

        long elapsedMillis = now - lastUpdate;

        Vector2 move = new Vector2();
        MOVEMENT.forEach((key, vector) -> {
            if (Gdx.input.isKeyPressed(key)) {
                move.add(vector);
            }
        });
        move.setLength(elapsedMillis * speed);
        if(control) {
            bounds.position.add(move);
        }
        lastUpdate = now;
    }

    public void render(OmegaSim sim) {
        sim.batch.begin();
        Rectangle rect = bounds.rect();
        sim.batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
        sim.batch.end();
    }

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }
}
