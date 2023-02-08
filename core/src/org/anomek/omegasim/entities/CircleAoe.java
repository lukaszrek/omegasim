package org.anomek.omegasim.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.OmegaSim;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.List;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static java.util.Collections.singletonList;

public class CircleAoe implements Entity, Aoe {

    private Visibility visibility = new Visibility(false);

    public Vector2 position;
    public int radius = 1;
    public long fadeIn = 0;
    public long fadeOut = 0;
    public Color color = Constants.AOE_ORANGE.cpy();

    @Override
    public void show(long when, long howLong) {
        visibility.show(when, new Smooth(fadeIn));
        visibility.hide(when + howLong, new Smooth(fadeOut));
    }

    @Override
    public int layer() {
        return Layers.AOE_MARKERS;
    }

    @Override
    public void update(long now) {
        visibility.update(now);
    }

    @Override
    public void render(OmegaSim sim) {
        if (!visibility.visible()) {
            return;
        }
        sim.shape.begin(Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Color color = this.color.cpy();
        color.a *= visibility.visibility();
        sim.shape.setColor(color);
        sim.shape.circle(position.x, position.y, radius * visibility.visibility());
        sim.shape.end();
    }

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }
}
