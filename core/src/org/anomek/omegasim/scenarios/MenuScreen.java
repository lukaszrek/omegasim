package org.anomek.omegasim.scenarios;

import com.badlogic.gdx.Screen;

public interface MenuScreen extends Screen {

    void setOnStartCallback(StartCallback callback);

    interface StartCallback {
        void start(Object settings);
    }
}
