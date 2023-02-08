package org.anomek.omegasim.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static java.util.Arrays.asList;
import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;

public class CombinedAoe implements Aoe {

    private final List<Aoe> aoes;

    public CombinedAoe(Aoe... aoes) {
        this.aoes = asList(aoes);
    }

    @Override
    public void show(long when, long howLong) {
        aoes.forEach(aoe -> aoe.show(when, howLong));
    }

    public static Aoe crossAoe(Vector2 startPoint, Vector2 direction, int width, long fadeIn) {
        LineAoe line1 = new LineAoe();
        line1.startPoint = startPoint;
        line1.direction = direction;
        line1.width(width);
        line1.fadeIn = fadeIn;
        LineAoe line2 = new LineAoe();
        line2.startPoint = startPoint;
        line2.direction = direction.cpy().rotate90(1);
        line2.width(width);
        line2.fadeIn = fadeIn;
        return new CombinedAoe(line1, line2);
    }

    public static Aoe sidesAoe(Vector2 startPoint, Vector2 direction, int innerWidth, int outerWidth, long fadeIn) {
        LineAoe line1 = new LineAoe();
        line1.direction = direction;
        line1.startPoint = startPoint.cpy().add(direction.cpy().rotate90(1).setLength(innerWidth / 2.0f));
        line1.width(outerWidth, 0);
        line1.fadeIn = fadeIn;
        LineAoe line2 = new LineAoe();
        line2.direction = direction;
        line2.startPoint = startPoint.cpy().add(direction.cpy().rotate90(-1).setLength(innerWidth / 2.0f));
        line2.width(0, outerWidth);
        line2.fadeIn = fadeIn;
        return new CombinedAoe(line1, line2);
    }

    @Override
    public List<PreparedEntity> prepare() {
        return prepareAll(aoes);
    }
}
