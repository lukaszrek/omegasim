package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.entities.*;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.List;

import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.MF_SIZE;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.M_ATTACK_AOE_SIZE;

public class M implements PreparableEntity {

    private final Textures textures;

    final Npc m;
    private final Aoe aoe;
    private final Vector2 attackPosition;
    private final boolean altAttack;

    public M(Textures textures, ScenarioRandom random) {
        this.textures = textures;
        this.altAttack = random.altM;
        this.m = new Npc(430, 500, MF_SIZE, textures.m);
        this.attackPosition = random.attackNorth().scl(-250).add(500, 500);
        if (altAttack) {
            DonutAoe donut = new DonutAoe();
            donut.position = attackPosition;
            donut.radius = M_ATTACK_AOE_SIZE;
            donut.fadeIn = Timing.AOE_FADE_IN;
            aoe = donut;
        } else {
            CircleAoe circle = new CircleAoe();
            circle.position = attackPosition;
            circle.radius = M_ATTACK_AOE_SIZE;
            circle.fadeIn = Timing.AOE_FADE_IN;
            aoe = circle;
        }
    }

    public void init() {
        m.startCasting("Party Synergy", Timing.PARTY_SYNERGY_CAST_TIME);
        m.visibility.hide(Timing.PARTY_SYNERGY_EXECUTION, new Smooth(Timing.MF_FADE_OUT));

        aoe.show(Timing.ATTACK_AOE_SHOW, Timing.ATTACK_AOE_DAMAGE - Timing.ATTACK_AOE_SHOW + 100);
    }


    // timing is ATTACK_MF_APPEAR
    public void attack() {
        m.bounds.position = attackPosition.cpy();
        m.visibility.show(new Smooth(Timing.MF_FADE_IN));
        m.visibility.hide(Timing.ATTACK_MF_DISAPPEAR - Timing.ATTACK_MF_APPEAR, new Smooth(Timing.MF_FADE_OUT));
        if (altAttack) {
            m.texture = textures.shield;
        }
    }

    @Override
    public List<PreparedEntity> prepare() {
        return prepareAll(m, aoe);
    }
}
