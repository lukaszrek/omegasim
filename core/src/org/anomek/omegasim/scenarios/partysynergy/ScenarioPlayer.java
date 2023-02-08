package org.anomek.omegasim.scenarios.partysynergy;

import org.anomek.omegasim.entities.Bounds;
import org.anomek.omegasim.entities.Player;
import org.anomek.omegasim.entities.PreparableEntity;
import org.anomek.omegasim.entities.Visibility;

import java.util.List;

public class ScenarioPlayer implements PreparableEntity, WithIndex {

    Player p;
    Playstation marker;
    int indx;
    PlayerScenarioInfo info;
    PlayersAoe aoes;

    ScenarioPlayer(Textures textures, int indx, ScenarioRandom random) {
        p = new Player();
        p.texture = textures.jobs().get(indx);
        info = new PlayerScenarioInfo();
        this.indx = indx;
        aoes = new PlayersAoe(textures, p.bounds, random.isStack(indx));
    }

    void assign(Playstation marker) {
        this.marker = marker;
        marker.owner = this;
    }

    public void init(Settings settings, ScenarioRandom random) {
        aoes.init();
        marker.markerVisibility.show(Timing.PARTY_SYNERGY_EXECUTION, new Visibility.Smooth(Timing.MARKERS_FADE_IN));
        marker.markerVisibility.hide(Timing.ATTACK_AOE_SHOW);
        info.init(indx, settings, random);
    }

    @Override
    public List<PreparedEntity> prepare() {
        return PreparableEntity.prepareAll(p, marker, aoes);
    }

    @Override
    public Bounds bounds() {
        return p.bounds;
    }

    @Override
    public int index() {
        return indx;
    }
}
