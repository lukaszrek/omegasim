package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.Screen;
import org.anomek.omegasim.OmegaSim;
import org.anomek.omegasim.scenarios.MenuScreen;
import org.anomek.omegasim.scenarios.SimScreen;

public class PartySynergyScenario {

    public Textures textures;
    public Menu menu;

    public PartySynergyScenario() {
        textures = new Textures();
        menu = new Menu(textures);
    }

    public MenuScreen getMenu() {
        return menu;
    }

    public SimScreen getSim(Object settings, OmegaSim sim) {
        return new PartySynergyScreen(textures, (Settings) settings, sim);
    }
}
