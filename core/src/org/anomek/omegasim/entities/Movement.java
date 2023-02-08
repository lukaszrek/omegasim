package org.anomek.omegasim.entities;

import com.badlogic.gdx.math.Vector2;

public class Movement {

    private final Bounds owner;

    private Vector2 target;
    private long startMoving;
    private long now;

    public float speed = 0.15f;

    public Movement(Bounds owner) {
        this.owner = owner;
    }

    public void move(long delay, Vector2 target) {
        this.startMoving = now + delay;
        this.target = target;
    }

    public void move(Vector2 target) {
        this.target = target;
    }

    public boolean moving() {
        return target != null;
    }

    void update(long now) {
        long delta = this.now == 0 ? 0 : now - this.now;
        if (startMoving != 0 && startMoving < now) {
            startMoving = 0;
        }

        if (startMoving == 0 && target != null) {
            Vector2 v = owner.position.cpy().scl(-1).add(target).limit(delta * speed);
            owner.position.add(v);
            if (v.isZero() && delta != 0) {
                target = null;
            }
        }
        this.now = now;
    }

}

