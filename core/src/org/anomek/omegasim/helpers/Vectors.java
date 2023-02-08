package org.anomek.omegasim.helpers;

import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Vectors {

    public static final List<Vector2> CARDINALS = asList(
            new Vector2(1, 0),
            new Vector2(-1, 0),
            new Vector2(0, 1),
            new Vector2(0, -1)
    );

    public static List<Vector2> INTERCARDINALS = Stream.of(
            new Vector2(1, 1),
            new Vector2(1, -1),
            new Vector2(-1, 1),
            new Vector2(-1, -1)
    ).map(v -> v.setLength(1)).collect(toList());

    public static List<Vector2> DIRECTIONS = Stream.of(CARDINALS, INTERCARDINALS)
            .flatMap(List::stream)
            .collect(toList());

    public static Vector2 nextDirection(Random random) {
        return DIRECTIONS.get(random.nextInt(DIRECTIONS.size())).cpy();
    }


    public static float distance(Vector2 lineStart, Vector2 lineDir, Vector2 point) {
        Vector2 lineEnd = lineStart.cpy().add(lineDir);
        float m = (lineStart.y - lineEnd.y) / (lineStart.x - lineEnd.x);
        float a = m;
        float b = -1;
        float c = lineStart.y - m * lineStart.x;

        return Math.abs(a * point.x + b * point.y + c) / (float) Math.sqrt(a * a + b * b);
    }
}
