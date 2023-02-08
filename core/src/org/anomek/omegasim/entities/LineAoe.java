package org.anomek.omegasim.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.OmegaSim;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static java.util.Collections.singletonList;
import static org.anomek.omegasim.entities.Constants.AOE_ORANGE;

public class LineAoe implements Entity, Aoe {

    private Visibility visibility = new Visibility(false);

    public Vector2 startPoint;
    public Vector2 direction;
    private int leftWidth = 1;
    private int rightWidth = 1;
    public long fadeIn = 0;
    public Color color = AOE_ORANGE.cpy();


    public void width(int left, int right) {
        leftWidth = left;
        rightWidth = right;
    }

    public void width(int width) {
        leftWidth = width;
        rightWidth = width;
    }

    @Override
    public void show(long when, long howLong) {
        visibility.show(when, new Visibility.Smooth(fadeIn));
        visibility.hide(when + howLong);
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

        Vector2 normal = direction.cpy().rotate90(1);
        Vector2 edge1 = normal.cpy().setLength(leftWidth * visibility.visibility()).add(startPoint);
        Vector2 edge2 = normal.cpy().setLength(rightWidth * visibility.visibility()).scl(-1).add(startPoint);
        Vector2 point1 = moveToEdge(edge1, direction);
        Vector2 point2 = moveToEdge(edge1, direction.cpy().scl(-1));
        Vector2 point3 = moveToEdge(edge2, direction.cpy().scl(-1));
        Vector2 point4 = moveToEdge(edge2, direction);

        sim.shape.begin(Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Color color = this.color.cpy();
        color.a *= visibility.visibility();
        sim.shape.setColor(color);
        sim.shape.triangle(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y);
        sim.shape.triangle(point1.x, point1.y, point3.x, point3.y, point4.x, point4.y);
        fill(point1, point4, startPoint)
                .ifPresent(fill -> sim.shape.triangle(point1.x, point1.y, point4.x, point4.y, fill.x, fill.y));
        fill(point2, point3, startPoint)
                .ifPresent(fill -> sim.shape.triangle(point2.x, point2.y, point3.x, point3.y, fill.x, fill.y));
        sim.shape.end();
    }

    private Optional<Vector2> fill(Vector2 point1, Vector2 point2, Vector2 startPoint) {
        if (point1.x == point2.x || point1.y == point2.y) {
            return Optional.empty();
        }
        Vector2 fill1 = new Vector2(point1.x, point2.y);
        Vector2 fill2 = new Vector2(point2.x, point1.y);
        float dist1 = startPoint.dst2(fill1);
        float dist2 = startPoint.dst2(fill2);
        if (dist1 > dist2) {
            return Optional.of(fill1);
        } else {
            return Optional.of(fill2);
        }
    }


    private Vector2 moveToEdge(Vector2 point, Vector2 direction) {
        float mul = multiplierToEdge(point, direction);
        return point.cpy().add(direction.cpy().scl(mul));
    }

    private float multiplierToEdge(Vector2 point, Vector2 direction) {
        return Math.min(
                multiplierToEdge(point.x, direction.x),
                multiplierToEdge(point.y, direction.y)
        );
    }

    private float multiplierToEdge(float point, float direction) {
        if (direction == 0) {
            return Float.MAX_VALUE;
        }
        return direction > 0
                ? (1000 - point) / direction
                : -point / direction;
    }

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }
}
