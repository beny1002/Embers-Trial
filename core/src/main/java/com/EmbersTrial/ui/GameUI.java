package com.EmbersTrial.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameUI {

    private Stage stage;
    private Label healthLabel;
    private Label xpLabel;

    public GameUI() {
        // Initialize the stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load inventory texture
        Texture inventoryTexture = new Texture(Gdx.files.internal("inventoryui.png"));

        // Create fonts and styles
        BitmapFont font = new BitmapFont();
        LabelStyle healthStyle = new LabelStyle(font, Color.RED);
        LabelStyle xpStyle = new LabelStyle(font, Color.BLUE);

        // Health and XP labels
        healthLabel = new Label("Health: 100/100", healthStyle);
        xpLabel = new Label("XP: 0/100", xpStyle);

        // Inventory image
        Image inventoryImage = new Image(inventoryTexture);

        // Table layout
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        table.add(healthLabel).pad(10).left();
        table.row();
        table.add(xpLabel).pad(10).left();
        table.row();
        table.add(inventoryImage).expandY().bottom().pad(10).left();

        stage.addActor(table);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void updateHealth(int currentHealth, int maxHealth) {
        healthLabel.setText("Health: " + currentHealth + "/" + maxHealth);
    }

    public void updateXP(int currentXP, int maxXP) {
        xpLabel.setText("XP: " + currentXP + "/" + maxXP);
    }

    public void dispose() {
        stage.dispose();
    }
}
