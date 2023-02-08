package org.anomek.omegasim.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import org.anomek.omegasim.OmegaSim;
import org.anomek.omegasim.entities.Bounds.Bounded;

import java.util.List;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
import static java.util.Collections.singletonList;

public class Cast implements Entity {

    String name;
    long duration;
    long startTime;
    Bounded owner;

    float progress;

    @Override
    public int layer() {
        return Layers.NPC;
    }

    @Override
    public void update(long now) {
        long elapsed = now - startTime;
        progress = elapsed / (float) duration;
    }

    @Override
    public void render(OmegaSim sim) {

        sim.shape.begin(Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        Rectangle bounds = owner.bounds().topAnchor(owner.bounds().width, 20).rect();

        sim.shape.setColor(1, 1, 0, 0.5f);
        sim.shape.rect(bounds.x, bounds.y, bounds.width * progress, bounds.height);
        sim.shape.end();

        sim.shape.begin(Line);
        sim.shape.setColor(1, 1, 0, 1);
        sim.shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        sim.shape.end();


        sim.batch.begin();
        sim.font.setColor(0, 0, 0, 1);
        sim.font.draw(sim.batch, name, bounds.x, bounds.y + 20);
        sim.batch.end();
    }

    public boolean finished() {
        return progress >= 1f;
    }

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }
}
