package org.anomek.omegasim.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.anomek.omegasim.OmegaSim;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class Waymark implements Entity {

    public Texture texture;
    public Bounds bounds = new Bounds();
    public boolean circle;

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }

    @Override
    public int layer() {
        return Layers.WAYMARKS;
    }

    @Override
    public void update(long now) {
    }

    @Override
    public void render(OmegaSim sim) {
        sim.batch.begin();
        Rectangle rect = bounds.rect();
        sim.batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
        sim.batch.end();
        sim.shape.begin(ShapeRenderer.ShapeType.Line);
        sim.shape.setColor(Color.WHITE);
        if (circle) {
            sim.shape.circle(bounds.position.x, bounds.position.y, bounds.height * 0.55f);
        } else {
            sim.shape.rect(rect.x, rect.y, rect.width, rect.height);
        }
        sim.shape.end();
    }
}
