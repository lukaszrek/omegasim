package org.anomek.omegasim.entities;

import com.badlogic.gdx.graphics.Texture;
import org.anomek.omegasim.OmegaSim;

import java.util.ArrayList;
import java.util.List;

import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;

public class Arena implements Entity {

    public Texture texture;
    private final List<PreparableEntity> entities;

    public Arena(List<PreparableEntity> entities) {
        this.entities = entities;
    }

    @Override
    public int layer() {
        return Layers.ARENA;
    }

    @Override
    public void update(long now) {
    }

    public void render(OmegaSim sim) {
        sim.batch.begin();
        sim.batch.draw(texture, 0, 0, 1000, 1000);
        sim.batch.end();
    }

    @Override
    public List<PreparedEntity> prepare() {
        List<PreparedEntity> prepared = new ArrayList<>();
        prepared.add(this);
        prepared.addAll(prepareAll(entities));
        return prepared;
    }
}
