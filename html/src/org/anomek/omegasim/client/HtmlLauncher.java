package org.anomek.omegasim.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import org.anomek.omegasim.OmegaSim;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(1000, 1000, true);
                config.padHorizontal = 0;
                config.padVertical = 0;
                return config;
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new OmegaSim();
        }
}