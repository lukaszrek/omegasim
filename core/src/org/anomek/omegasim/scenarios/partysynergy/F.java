package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.entities.Aoe;
import org.anomek.omegasim.entities.Npc;
import org.anomek.omegasim.entities.PreparableEntity;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.List;

import static org.anomek.omegasim.entities.CombinedAoe.crossAoe;
import static org.anomek.omegasim.entities.CombinedAoe.sidesAoe;
import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.MF_SIZE;

public class F implements PreparableEntity {

    private final Textures textures;

    final Npc f;
    private final Aoe aoe;

    private final boolean altAttack;
    private final Vector2 attackPosition;

    F(Textures textures, ScenarioRandom random) {
        this.textures = textures;
        this.altAttack = random.altF;
        this.attackPosition = random.attackNorth().cpy().scl(250).add(500, 500);
        this.f = new Npc(570, 500, MF_SIZE, textures.f);
        if (altAttack) {
            aoe = sidesAoe(attackPosition, random.attackNorth(), Constants.F_ATTACK_TUNNEL_WIDTH, 500, Timing.AOE_FADE_IN);
        } else {
            aoe = crossAoe(attackPosition, random.attackNorth(), Constants.F_ATTACK_CROSS_WIDTH, Timing.AOE_FADE_IN);
        }
    }

    public void init() {
        f.startCasting("Party Synergy", Timing.PARTY_SYNERGY_CAST_TIME);
        f.visibility.hide(Timing.PARTY_SYNERGY_EXECUTION, new Smooth(Timing.MF_FADE_OUT));
        f.visibility.show(Timing.ATTACK_MF_APPEAR, new Smooth(Timing.MF_FADE_IN));

        aoe.show(Timing.ATTACK_AOE_SHOW, Timing.ATTACK_AOE_DAMAGE - Timing.ATTACK_AOE_SHOW + 100);
    }

    // timing is ATTACK_MF_APPEAR
    public void attack() {
        f.bounds.position = attackPosition.cpy();
        f.visibility.show(new Smooth(Timing.MF_FADE_IN));
        f.visibility.hide(Timing.ATTACK_MF_DISAPPEAR - Timing.ATTACK_MF_APPEAR, new Smooth(Timing.MF_FADE_OUT));
        if (altAttack) {
            f.texture = textures.blades;
        }
    }

    @Override
    public List<PreparedEntity> prepare() {
        return prepareAll(f, aoe);
    }
}
