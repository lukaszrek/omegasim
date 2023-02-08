package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.entities.CircleAoe;
import org.anomek.omegasim.entities.Npc;
import org.anomek.omegasim.entities.PreparableEntity;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.anomek.omegasim.entities.PreparableEntity.prepareAll;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.FORCE_AOE;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.MF_SIZE;

public class Force implements PreparableEntity {
    final List<Npc> four;
    final Npc lastM;
    private final List<CircleAoe> aoes;
    private final Npc f;

    Force(Textures textures, ScenarioRandom random) {
        Vector2 north = random.forceNorth().setLength(320);
        lastM = new Npc(500, 500, MF_SIZE, textures.m);
        lastM.visibility.hide();
        lastM.bounds.position.add(north);

        north.rotateDeg(45);
        four = IntStream.range(0, 4).mapToObj(id -> {
            Npc m = new Npc(500, 500, MF_SIZE, textures.m);
            m.visibility.hide();
            m.bounds.position.add(north);
            north.rotate90(1);
            return m;
        }).collect(toList());

        aoes = Stream.concat(
                four.stream(),
                Stream.of(lastM)
        ).map(m -> {
            CircleAoe aoe = new CircleAoe();
            aoe.fadeIn = Timing.AOE_FADE_IN;
            aoe.position = m.bounds.position.cpy();
            aoe.radius = FORCE_AOE;
            return aoe;
        }).collect(toList());

        f = new Npc(500, 500, MF_SIZE, textures.f);
        f.visibility.hide();
    }

    public void init() {
        four.forEach(m -> {
            m.visibility.show(Timing.ATTACK_AOE_DAMAGE, new Smooth(Timing.MF_FADE_IN));
        });
        lastM.visibility.show(Timing.EYE_AOE_DAMAGE, new Smooth(Timing.MF_FADE_IN));
        aoes.forEach(a -> {
            a.show(Timing.M_FORCE_AOE_APPEAR, Timing.M_FORCE_AOE_DAMAGE - Timing.M_FORCE_AOE_APPEAR);
        });
        f.visibility.show(Timing.EYE_AOE_DAMAGE, new Smooth(Timing.MF_FADE_IN));
    }

    @Override
    public List<PreparedEntity> prepare() {
        List<PreparedEntity> prepared = new ArrayList<>();
        prepared.addAll(prepareAll(four));
        prepared.addAll(prepareAll(aoes));
        prepared.addAll(prepareAll(lastM, f));
        return prepared;
    }
}
