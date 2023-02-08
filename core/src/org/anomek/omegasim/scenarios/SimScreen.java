package org.anomek.omegasim.scenarios;

import com.badlogic.gdx.Screen;
import org.anomek.omegasim.scenarios.partysynergy.ScenarioRandom;

public interface SimScreen extends Screen {

    void registerCallbacks(Callbacks callbacks);

    interface Callbacks {
        void restart();
        void back();
    }
}
