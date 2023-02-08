package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import org.anomek.omegasim.scenarios.MenuScreen;

import java.util.ArrayList;
import java.util.List;

public class Menu implements MenuScreen {

    Skin skin;
    Stage stage;
    Textures textures;

    int[] order = new int[]{0, 1, 2, 3};

    List<ImageButton> jobButtons = new ArrayList<>();

    List<ImageButton> orderButtons = new ArrayList<>();
    List<Image> orderReversedAll = new ArrayList<>();
    List<Image> orderReversedEdge = new ArrayList<>();

    List<TextButton> reverseButtons = new ArrayList<>();

    StartCallback startCallback;

    Menu(Textures textures) {
        this.textures = textures;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        initSkin();

        Table table = new Table(skin);
        table.setFillParent(true);
//        table.setDebug(true);
        stage.addActor(table);

        table.add("Omega Party Synergy Simulator");
        table.row();
        table.add("Select your job and position");
        table.row();

        Table row = new Table(skin);
        ButtonGroup<ImageButton> group1 = new ButtonGroup<>();
        for (int i = 0; i < 8; i++) {
            ImageButton imageButton = new ImageButton(skin, "job" + i);
            row.add(imageButton);
            group1.add(imageButton);
            jobButtons.add(imageButton);
        }
        table.add(row);
        table.row();

        table.add("Marker order");
        table.row();

        row = new Table(skin);
        row.add("LEFT & RIGHT CLOSE");
        row.add("RIGHT FAR");
        row.row();
        row.add("");

        ButtonGroup<TextButton> group2 = new ButtonGroup<>();
        TextButton textButton1 = new TextButton("Mirror ALL", skin);
        row.add(textButton1);
        reverseButtons.add(textButton1);
        group2.add(textButton1);
        TextButton textButton2 = new TextButton("Mirror On EDGE", skin);
        row.add(textButton2);
        reverseButtons.add(textButton2);
        group2.add(textButton2);
        textButton1.addListener(event -> updatePs());
        textButton2.addListener(event -> updatePs());
        row.row();
        float scale = 0.3f;
        for (int i = 0; i < 4; i++) {
            ImageButton imageButton = new ImageButton(skin, "ps" + i);
            int ii = i;
            imageButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    psButtonPressed(ii);
                }
            });
            row.add(imageButton);
            orderButtons.add(imageButton);

            Image image = new Image(textures.playstation().get(3 - i));
            image.setScale(scale);
            row.add(image);
            orderReversedAll.add(image);

            int indx = i == 0 || i == 3 ? 3 - i : i;
            image = new Image(textures.playstation().get(indx));
            image.setScale(scale);
            row.add(image);
            orderReversedEdge.add(image);

            row.row();
        }
        table.add(row);


        table.row();

        TextButton start = new TextButton("Start", skin);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startCallback.start(settings());
            }
        });
        table.add(start);
        updatePs();
    }

    private Settings settings() {
        Settings settings = new Settings();
        for (int i = 0; i < jobButtons.size(); i++) {
            if (jobButtons.get(i).isChecked()) {
                settings.playerIndx = i;
                break;
            }
        }

        settings.orderClose = new int[4];
        settings.orderFar = new int[4];

        boolean fullReverse = reverseButtons.get(0).isChecked();
        for (int i = 0; i < 4; i++) {
            settings.orderClose[i] = order[i];
            int farInx = fullReverse || i == 0 || i == 3 ? 3 - i : i;
            settings.orderFar[i] = order[farInx];
        }
        return settings;
    }

    private boolean psButtonPressed(int i) {
        if (i > 0) {
            int tmp = order[i];
            order[i] = order[i - 1];
            order[i - 1] = tmp;
        }
        updatePs();
        return true;
    }

    private boolean updatePs() {
        Color tint1 = reverseButtons.get(0).isChecked()
                ? new Color(1, 1, 1, 1)
                : new Color(.3f, .3f, .3f, 1);
        Color tint2 = reverseButtons.get(1).isChecked()
                ? new Color(1, 1, 1, 1)
                : new Color(.3f, .3f, .3f, 1);

        for (int i = 0; i < 4; i++) {
            int idx = order[i];
            int idxrev = order[3 - i];
            int idxswp = order[i == 0 || i == 3 ? 3 - i : i];

            orderButtons.get(i).setStyle(skin.get("ps" + idx, ImageButtonStyle.class));
            orderReversedAll.get(i).setDrawable(new TextureRegionDrawable(textures.playstation().get(idxrev)).tint(tint1));
            orderReversedEdge.get(i).setDrawable(new TextureRegionDrawable(textures.playstation().get(idxswp)).tint(tint2));
        }
        return true;
    }


    private void initSkin() {
        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libGDX font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);

        int i = 0;
        for (Texture texture : textures.jobs()) {
            TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = drawable;
            imageButtonStyle.imageDown = drawable.tint(new Color(0, 0, 1, 1));
            imageButtonStyle.imageOver = drawable.tint(new Color(0, 1, 0, 1));
            imageButtonStyle.imageChecked = drawable;
            imageButtonStyle.up = skin.newDrawable("white", Color.BLACK);
            imageButtonStyle.checked = skin.newDrawable("white", Color.RED);

            skin.add("job" + i, imageButtonStyle);
            i++;
        }

        i = 0;
        for (Texture texture : textures.playstation()) {
            TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = drawable;
            imageButtonStyle.imageDown = drawable.tint(new Color(0, 0, 1, 1));
            imageButtonStyle.imageOver = drawable.tint(new Color(0, 1, 0, 1));

            skin.add("ps" + i, imageButtonStyle);
            i++;
        }

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(.1f, .1f, .1f, 1);
        stage.act(delta);
        stage.draw();
    }


    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void setOnStartCallback(StartCallback callback) {
        this.startCallback = callback;
    }
}
