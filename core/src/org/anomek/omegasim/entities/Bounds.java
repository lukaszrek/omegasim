package org.anomek.omegasim.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bounds {
    public Vector2 position = new Vector2();
    public float width;
    public float height;

    public void size(float size) {
        width = size;
        height = size;
    }

    public Rectangle rect() {
        Rectangle rect = new Rectangle();
        rect.width = width;
        rect.height = height;
        rect.setCenter(position);
        return rect;
    }

    public Bounds topAnchor(float width, float height) {
        Bounds anchor = new Bounds();
        anchor.position = position.cpy().add(0, this.height / 2 + height / 2);
        anchor.width = width;
        anchor.height = height;
        return anchor;
    }

    public Bounds scale(float factor) {
        width *= factor;
        height *= factor;
        return this;
    }

    public Vector2 position(float x, float y) {
        position.x = x;
        position.y = y;
        return position;
    }

    public Bounds cpy() {
        Bounds cpy = new Bounds();
        cpy.position = position.cpy();
        cpy.height = height;
        cpy.width = width;
        return cpy;
    }


    public interface Bounded {
        Bounds bounds();
    }
}
