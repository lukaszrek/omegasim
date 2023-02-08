package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.entities.LineAoe;
import org.anomek.omegasim.entities.Npc;
import org.anomek.omegasim.entities.PreparableEntity;

import java.util.List;

import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;

public class Eye implements PreparableEntity {

    private final Npc npc;
    private final LineAoe aoe;

    Eye(Textures textures, ScenarioRandom random) {
        Vector2 position = random.eyeNorth().cpy().scl(520).add(500, 500);
        npc = new Npc(position.x, position.y, 200, textures.eye);
        npc.visibility.hide();
        aoe = new LineAoe();
        aoe.direction = random.eyeNorth();
        aoe.startPoint = new Vector2(500, 500);
        aoe.width(Constants.EYE_ATTACK_WIDTH);
        aoe.color = Color.NAVY.cpy();
        aoe.color.a = 0.8f;
        aoe.fadeIn = 300;
    }

    public void init() {
        npc.visibility.show(Timing.PARTY_SYNERGY_EXECUTION);
        npc.visibility.hide(Timing.EYE_DISAPPEAR);
        aoe.show(Timing.EYE_AOE_DAMAGE - 350, Timing.EYE_AOE_LINGER);
    }

    @Override
    public List<PreparedEntity> prepare() {
        return prepareAll(npc, aoe);
    }
}
