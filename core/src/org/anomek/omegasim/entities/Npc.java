package org.anomek.omegasim.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import org.anomek.omegasim.OmegaSim;

import java.util.ArrayList;
import java.util.List;

public class Npc implements Entity {

    Cast cast;

    public Visibility visibility = new Visibility(true);
    public Bounds bounds = new Bounds();
    public Movement movement = new Movement(bounds);
    public Texture texture;

    public Npc(float x, float y, float size, Texture texture) {
        bounds.position.x = x;
        bounds.position.y = y;
        bounds.size(size);
        this.texture = texture;
    }

    public void startCasting(String name, long duration) {
        cast = new Cast();
        cast.name = name;
        cast.duration = duration;
        cast.startTime = TimeUtils.millis();
        cast.owner = () -> bounds;
    }


    @Override
    public int layer() {
        return Layers.NPC;
    }

    public void update(long now) {
        visibility.update(now);
        movement.update(now);
        if (cast != null && cast.finished()) {
            cast = null;
        }
    }

    public void render(OmegaSim sim) {
        if (visibility.visibility() > 0) {
            sim.batch.begin();
            sim.batch.setColor(1, 1, 1, visibility.visibility());
            Rectangle rect = bounds.cpy().scale(visibility.visibility()).rect();
            sim.batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
            sim.batch.setColor(1, 1, 1, 1);
            sim.batch.end();
        }
    }

    @Override
    public List<PreparedEntity> prepare() {
        List<PreparedEntity> prepared = new ArrayList<>();
        prepared.add(this);
        if (cast != null) {
            prepared.addAll(cast.prepare());
        }
        return prepared;
    }
}
