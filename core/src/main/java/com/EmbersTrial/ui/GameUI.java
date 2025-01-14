package com.EmbersTrial.ui;

import com.EmbersTrial.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameUI {

    private Stage stage;
    private Label healthLabel;
    private Label xpLabel;
    private Player player; // Reference to the player instance
    private Label staminaLabel;

    public GameUI(Player player) {
        this.player = player;

        // Initialize the stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create fonts and styles
        BitmapFont font = new BitmapFont();
        Label.LabelStyle healthStyle = new Label.LabelStyle(font, Color.RED);
        Label.LabelStyle xpStyle = new Label.LabelStyle(font, Color.BLUE);
        Label.LabelStyle staminaStyle = new Label.LabelStyle(font, Color.YELLOW);

        // Health and XP labels
        healthLabel = new Label("Health", healthStyle);
        xpLabel = new Label("EXP", xpStyle);
        staminaLabel = new Label("Stamina", staminaStyle);

        // Table layout
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        // Add labels to the table
        table.add(healthLabel).pad(10).left();
        table.row();
        table.add(xpLabel).pad(10).left();
        table.row();
        table.add(staminaLabel).pad(10).left();

        // Add the table to the stage
        stage.addActor(table);
    }

    public void render(float delta) {
        // Update labels with player stats
        healthLabel.setText("Health: " + player.getStats().getHealth() + "/" + player.getStats().getMaxHealth());
        xpLabel.setText("XP: " + player.getStats().getExp() + "/100");
        staminaLabel.setText("Stamina: " + (int) player.getStats().getStamina() + "/100");

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
