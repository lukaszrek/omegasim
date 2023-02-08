package org.anomek.omegasim.entities;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.OmegaSim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class Knockback implements Entity {

    public Vector2 center;
    public float distance;
    public long time = 500;
    public Player player;
    public List<Bounds> affected = new ArrayList<>();
    private List<Movement> movements;

    public long when;

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }

    @Override
    public int layer() {
        return 0;
    }

    @Override
    public void update(long now) {
        if(when != 0 && when < now) {
            player.control = false;

            movements = affected.stream()
                    .map(b -> {
                        Movement m = new Movement(b);

                        Vector2 displacement = b.position.cpy().add(center.cpy().scl(-1)).setLength(distance);
                        m.move(b.position.cpy().add(displacement));
                        m.speed = displacement.len() / time;
                        return m;
                    }).collect(Collectors.<Movement>toList());
            when = 0;
        }

        if (movements != null) {
            movements.forEach(m -> m.update(now));
            if (movements.stream().noneMatch(Movement::moving)) {
                player.control = true;
                movements = null;
            }
        }
    }

    @Override
    public void render(OmegaSim sim) {
    }
}
