package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.graphics.Color;
import org.anomek.omegasim.entities.Bounds;
import org.anomek.omegasim.entities.CircleAoe;
import org.anomek.omegasim.entities.PreparableEntity;
import org.anomek.omegasim.entities.Stack;

import java.util.List;

public class PlayersAoe implements PreparableEntity {

    Stack stack;
    CircleAoe spread;
    int index;

    PlayersAoe(Textures textures, Bounds owner, boolean spawnStack) {
        spread = new CircleAoe();
        spread.position = owner.position;
        spread.color = new Color(1, .3f, .3f, .8f);
        spread.fadeIn = 100;
        spread.fadeOut = 100;
        spread.radius = Constants.SPREAD_RADIUS;

        if (spawnStack) {
            stack = new Stack();
            stack.visibility.hide();
            stack.bounds.position = owner.position;
            stack.bounds.size(120);
            stack.texture = textures.stack;
        }
    }


    void init() {
        spread.show(Timing.EYE_AOE_DAMAGE - 250, 300);
        if (stack != null) {
            stack.visibility.show(Timing.STACK_MARKER_APPEARS);
        }
    }

    @Override
    public List<PreparedEntity> prepare() {
        return PreparableEntity.prepareAll(stack, spread);
    }
}
