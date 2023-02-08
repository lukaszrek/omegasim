package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.helpers.Vectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ScenarioRandom {

    final Random r;

    final boolean altM;
    final boolean altF;

    final boolean far;

    private final Vector2 attackNorth;
    private final Vector2 eyeNorth;
    private final Vector2 forceNorth;

    private final int stack1;
    private final int stack2;

    final List<Integer> markers;

    Vector2 attackNorth() {
        return attackNorth.cpy();
    }

    Vector2 eyeNorth() {
        return eyeNorth.cpy();
    }

    Vector2 forceNorth() {
        return forceNorth.cpy();
    }


    ScenarioRandom() {
        Random random = new Random();
        this.r = random;
        altM = random.nextBoolean();
        altF = random.nextBoolean();
        far = random.nextBoolean();
        attackNorth = Vectors.nextDirection(random);
        eyeNorth = Vectors.nextDirection(random);
        forceNorth = Vectors.nextDirection(random);

        stack1 = random.nextInt(8);
        int second;
        while ((second = random.nextInt(8)) == stack1) {
            // empty
        }
        stack2 = second;

        List<Integer> markersOrdered = new ArrayList<>(IntStream.range(0, 4)
                .flatMap(i -> IntStream.of(i, i))
                .boxed()
                .collect(toList()));
        markers = new ArrayList<>();
        while(!markersOrdered.isEmpty()) {
            markers.add(markersOrdered.remove(random.nextInt(markersOrdered.size())));
        }
    }

    public boolean isStack(int indx) {
        return stack1 == indx || stack2 == indx;
    }
}
