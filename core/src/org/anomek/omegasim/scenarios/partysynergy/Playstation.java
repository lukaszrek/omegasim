package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.OmegaSim;
import org.anomek.omegasim.entities.Entity;
import org.anomek.omegasim.entities.Layers;
import org.anomek.omegasim.entities.Visibility;

import java.util.List;
import java.util.Random;

import static java.util.Collections.singletonList;

public class Playstation implements Entity {

    private static Random random = new Random();

    int id;
    Visibility markerVisibility = new Visibility(false);
    Visibility tetherVisibility = new Visibility(false);
    Texture texture;
    WithIndex owner;
    Playstation other;
    int size = 40;

    @Override
    public int layer() {
        return Layers.PLAYERS - 1;
    }

    @Override
    public void update(long now) {
        markerVisibility.update(now);
        tetherVisibility.update(now);
    }

    private Vector2 fuzzy(Vector2 org) {
//        return org.cpy().add(random.nextFloat()*3-2, random.nextFloat()*3-2);
        return org;
    }

    @Override
    public void render(OmegaSim sim) {
        if (tetherVisibility.visible()) {
            sim.shape.begin(ShapeRenderer.ShapeType.Line);
            sim.shape.setColor(1, 1, 1, .8f * tetherVisibility.visibility());
            sim.shape.line(fuzzy(owner.bounds().position), fuzzy(other.owner.bounds().position));
            sim.shape.end();
        }

        if (markerVisibility.visible()) {
            sim.batch.begin();
            Rectangle rect = owner.bounds().topAnchor(size, size).scale(markerVisibility.visibility()).rect();
            sim.batch.setColor(1, 1, 1, markerVisibility.visibility());
            sim.batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
            sim.batch.setColor(1, 1, 1, 1);
            sim.batch.end();
        }

    }

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }
}
