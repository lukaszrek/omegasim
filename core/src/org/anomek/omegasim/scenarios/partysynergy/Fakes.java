package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.entities.Npc;
import org.anomek.omegasim.entities.PreparableEntity;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.List;

import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;
import static org.anomek.omegasim.helpers.Vectors.nextDirection;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.MF_SIZE;

public class Fakes implements PreparableEntity {

    private final Npc m;
    private final Npc f;


    Fakes(Textures textures, ScenarioRandom random) {
        m = new Npc(500, 500, MF_SIZE, textures.m);
        m.visibility.hide();
        f = new Npc(0, 0, MF_SIZE, textures.f);
        f.visibility.hide();

        while (true) {
            Vector2 next = nextDirection(random.r);
            if (!next.isOnLine(random.attackNorth())) {
                f.bounds.position(500, 500).add(next.setLength(300));
                break;
            }
        }
    }

    public void attack() {
        m.visibility.show(new Smooth(Timing.MF_FADE_IN));
        f.visibility.show(new Smooth(Timing.MF_FADE_IN));
        m.visibility.hide(Timing.ATTACK_AOE_DAMAGE - Timing.ATTACK_MF_APPEAR, new Smooth(Timing.MF_FADE_OUT));
        f.visibility.hide(Timing.ATTACK_AOE_DAMAGE- Timing.ATTACK_MF_APPEAR,  new Smooth(Timing.MF_FADE_OUT));
    }

    @Override
    public List<PreparedEntity> prepare() {
        return prepareAll(m, f);
    }
}
