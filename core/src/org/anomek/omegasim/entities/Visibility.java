package org.anomek.omegasim.entities;

public class Visibility {

    private float visibility;

    private long now;
    private long showAt;
    private Animation showAnimation;
    private long hideAt;
    private Animation hideAnimation;

    public Visibility(boolean visible) {
        this.visibility = visible ? 1f : 0f;
    }

    public boolean visible() {
        return visibility > 0;
    }

    public float visibility() {
        return visibility;
    }

    public void hide() {
        visibility = 0;
        hideAnimation = null;
        hideAt = 0;
    }

    public void hide(long delay) {
        hideAnimation = null;
        hideAt = now + delay;
    }

    public void hide(Animation animation) {
        hideAt = now;
        hideAnimation = animation;
    }

    public void hide(long delay, Animation animation) {
        hideAt = now + delay;
        hideAnimation = animation;
    }

    public void show() {
        visibility = 1;
        showAt = 0;
        showAnimation = null;
    }

    public void show(long delay) {
        showAt = now + delay;
        showAnimation = null;
    }

    public void show(Animation animation) {
        showAt = now;
        showAnimation = animation;
    }

    public void show(long delay, Animation animation) {
        showAt = now + delay;
        showAnimation = animation;
    }

    public void update(long now) {
        this.now = now;

        if (showAt != 0 && showAt < now) {
            showAt = 0;
            if (showAnimation == null) {
                visibility = 1f;
            }
        }

        if (hideAt != 0 && hideAt < now) {
            hideAt = 0;
            if (hideAnimation == null) {
                visibility = 0f;
            }
        }

        if (showAt == 0 && showAnimation != null) {
            visibility = showAnimation.update(now);
            if (showAnimation.finished()) {
                showAnimation = null;
            }
        } else if (hideAt == 0 && hideAnimation != null) {
            visibility = 1 - hideAnimation.update(now);
            if (hideAnimation.finished()) {
                hideAnimation = null;
            }
        }
    }

    public interface Animation {
        float update(long now);

        boolean finished();
    }

    public static class Smooth implements Animation {

        private final long duration;

        private long start;
        boolean finished;

        public Smooth(long duration) {
            this.duration = duration;
        }

        @Override
        public float update(long now) {
            if (start == 0) {
                start = now;
            }

            long delta = now - start;
            if (delta > duration) {
                finished = true;
                return 1f;
            }
            return delta / (float) duration;
        }

        @Override
        public boolean finished() {
            return finished;
        }
    }
}
