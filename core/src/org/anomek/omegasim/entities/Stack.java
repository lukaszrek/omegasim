package org.anomek.omegasim.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import org.anomek.omegasim.OmegaSim;

import java.util.List;

import static java.util.Collections.singletonList;

public class Stack implements Entity {

    public Visibility visibility = new Visibility(false);
    public Bounds bounds = new Bounds();
    public Texture texture;

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }

    @Override
    public int layer() {
        return Layers.PLAYERS - 1;
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
        Rectangle rect = bounds.rect();
        sim.batch.begin();
        sim.batch.setColor(1, 1, 1, visibility.visibility());
        sim.batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
        sim.batch.end();
    }
}
