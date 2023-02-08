package org.anomek.omegasim.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.anomek.omegasim.OmegaSim;
import org.anomek.omegasim.entities.Visibility.Smooth;

import java.util.List;

import static java.util.Collections.singletonList;

public class DonutAoe implements Entity, Aoe {

    public Visibility visibility = new Visibility(false);

    public Vector2 position;
    public int radius;
    public long fadeIn = 0;
    public Color color = Constants.AOE_ORANGE.cpy();

    @Override
    public void show(long when, long howLong) {
        visibility.show(when, new Smooth(fadeIn));
        visibility.hide(when + howLong);
    }


    @Override
    public int layer() {
        return Layers.AOE_MARKERS;
    }

    @Override
    public void update(long now) {
        visibility.update(now);
    }

    @Override
    public void render(OmegaSim sim) {
        if (!visibility.visible()) {
            return;
        }
        Pixmap overlay = new Pixmap(1000, 1000, Pixmap.Format.RGBA8888);
        Color color = this.color.cpy();
        color.a *= visibility.visibility();
        overlay.setColor(color);
        overlay.fillRectangle(0, 0, 1000, 1000);

// the trick is to "redraw" the inner circle with an "invisible" colour alpha=0
        overlay.setBlending(Blending.None);
        overlay.setColor(1, 1, 1, 0);
        overlay.fillCircle((int) position.x, 1000 - (int) position.y, radius);
        overlay.setBlending(Blending.SourceOver);

        Texture t = new Texture(overlay);
        overlay.dispose();

        sim.batch.begin();
        sim.batch.draw(t, 0, 0);
        sim.batch.end();
    }

    @Override
    public List<PreparedEntity> prepare() {
        return singletonList(this);
    }
}
