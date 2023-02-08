package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.entities.Bounds;
import org.anomek.omegasim.entities.Npc;
import org.anomek.omegasim.entities.PreparableEntity;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.List;

public class PartyMember implements PreparableEntity, WithIndex {

    Npc npc;
    int id;
    Playstation marker;
    PlayersAoe aoes;
    PlayerScenarioInfo info;

    public PartyMember(Textures textures, int id, ScenarioRandom random) {
        npc = new Npc((-4 + id) * 80 + 500, 400, 30, textures.jobs().get(id));
        aoes = new PlayersAoe(textures, npc.bounds, random.isStack(id));
        aoes.index = this.id;
        info = new PlayerScenarioInfo();
        this.id = id;
    }

    public void assign(Playstation marker) {
        this.marker = marker;
        marker.owner = this;
    }

    public void init(Settings settings, ScenarioRandom random) {
        info.init(id, settings, random);
        aoes.init();
        marker.markerVisibility.show(Timing.PARTY_SYNERGY_EXECUTION, new Smooth(Timing.MARKERS_FADE_IN));
        marker.markerVisibility.hide(Timing.ATTACK_MF_APPEAR, new Smooth(500));
        marker.tetherVisibility.show(Timing.PARTY_SYNERGY_EXECUTION, new Smooth(Timing.MARKERS_FADE_IN));
        marker.tetherVisibility.hide(Timing.ATTACK_MF_APPEAR);
        npc.visibility.hide(Timing.ATTACK_MF_APPEAR, new Smooth(1500));
        npc.visibility.show(Timing.EYE_AOE_DAMAGE - 2500, new Smooth(1500));

        Vector2 target = random.eyeNorth().cpy().setLength(470);
        int direction = info.left ? 1 : -1;

        if (random.far) {
            target.rotateDeg(27.5f * direction);
            target.rotateDeg(41.6f * info.rank * direction);
        } else {
            Vector2 first = target.rotateDeg(27.5f * direction);
            Vector2 last = first.cpy().rotateDeg(41.6f * 3 * direction);
            Vector2 diff = last.add(first.cpy().scl(-1)).scl(.33f * info.rank);
            target = first.add(diff);
        }

        target.add(500, 500);

        npc.movement.move(Timing.ATTACK_AOE_DAMAGE, target);
    }

    @Override
    public List<PreparedEntity> prepare() {
        return PreparableEntity.prepareAll(npc, marker, aoes);
    }

    @Override
    public Bounds bounds() {
        return npc.bounds;
    }

    @Override
    public int index() {
        return id;
    }

    public void stack(ScenarioRandom random) {
        marker.tetherVisibility.show();

        Vector2 position = random.forceNorth().cpy().rotate90(1).setLength(2);
        if (!info.leftForStack()) {
            if (random.far) {
                position.scl(-1);
            } else {
                position.rotate90(1);
            }
        }
        position.add(500, 500);

        npc.movement.move(2000, position);
    }

    public void runToStack() {
        Vector2 displacement = npc.bounds.position.cpy().add(-500, -500).setLength(100);
        npc.movement.move(1000, npc.bounds.position.cpy().add(displacement));
    }
}
