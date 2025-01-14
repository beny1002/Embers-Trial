package com.EmbersTrial.ui;

import com.EmbersTrial.player.Player;
import com.EmbersTrial.player.Stats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameUI {

    private Stage stage;
    private Player player; // Reference to the player instance
    private Texture heartTexture; // The heart texture
    private Texture xpTextureFilled; // The filled XP texture
    private Texture staminaTextureFilled; // The filled stamina texture
    private static final int MAX_HEALTH = 100; // Max health
    private static final int HEART_WIDTH = 32; // Heart width in pixels
    private static final int HEART_HEIGHT = 32; // Heart height in pixels
    private static final int HEART_SPACING = 5; // Spacing between hearts
    private static final int MAX_XP = 100; // Max XP for the player
    private static final int XP_SLOT_WIDTH = 32; // Width of each XP slot
    private static final int XP_SLOT_HEIGHT = 32; // Height of each XP slot
    private static final int XP_SPACING = 5; // Space between XP slots
    private static final int MAX_STAMINA = 100; // Max stamina
    private static final int STAMINA_SLOT_WIDTH = 32; // Width of each stamina slot
    private static final int STAMINA_SLOT_HEIGHT = 32; // Height of each stamina slot
    private static final int STAMINA_SPACING = 5; // Space between stamina slots


    public GameUI(Player player) {
        this.player = player;

        // Initialize the stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load the textures
        heartTexture = new Texture(Gdx.files.internal("heart.png"));
        xpTextureFilled = new Texture(Gdx.files.internal("exp.png"));
        staminaTextureFilled = new Texture(Gdx.files.internal("stamina.png"));

        // Table layout for organizing XP and Stamina labels
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        // Add the table to the stage
        stage.addActor(table);
    }

    // Render the UI (draw hearts based on health and XP slots)
    public void render(float delta) {
        // Get current health and calculate number of hearts
        int currentHealth = player.getStats().getHealth();
        int numHearts = currentHealth / (MAX_HEALTH / 10); // 1 heart for every 10 health

        // Get current XP and calculate number of filled XP slots
        int currentXP = player.getStats().getExp();
        int numFilledXP = currentXP / 10; // 1 filled slot for every 10 XP (integer division)

        // Ensure numFilledXP never exceeds 10 slots
        numFilledXP = Math.min(numFilledXP, 10);

        // Get current stamina and calculate number of filled stamina slots
        float currentStamina = player.getStats().getStamina();
        float numFilledStamina = currentStamina / 10; // 1 filled slot for every 10 stamina (integer division)

        // Ensure numFilledStamina never exceeds 10 slots
        numFilledStamina = Math.min(numFilledStamina, 10);

        // Clear the screen and render the health hearts, XP slots, and stamina slots
        Batch batch = stage.getBatch();
        batch.begin();

        // Draw health as hearts (positioning the hearts below the XP label)
        int startX = 0; // Start drawing hearts from 0px from the left (top-left corner)
        int startY = Gdx.graphics.getHeight() - 50; // Starting Y position for hearts (50px from the top)

        for (int i = 0; i < numHearts; i++) {
            batch.draw(heartTexture, startX + i * (HEART_WIDTH + HEART_SPACING), startY, HEART_WIDTH, HEART_HEIGHT); // Draw hearts horizontally
        }

        // Draw XP slots (only filled XP slots)
        int xpStartX = 0; // Start drawing XP from 0px from the left (top-left corner)
        int xpStartY = startY - HEART_HEIGHT - 10; // Place XP slots below the hearts

        for (int i = 0; i < numFilledXP; i++) {
            batch.draw(xpTextureFilled, xpStartX + i * (XP_SLOT_WIDTH + XP_SPACING), xpStartY, XP_SLOT_WIDTH, XP_SLOT_HEIGHT); // Draw filled XP slots horizontally
        }

        // Draw stamina slots (like hearts, only filled slots)
        int staminaStartX = 0; // Start drawing stamina from 0px from the left (top-left corner)
        int staminaStartY = xpStartY - XP_SLOT_HEIGHT - 10; // Place stamina slots below the XP slots

        for (int i = 0; i < numFilledStamina; i++) {
            batch.draw(staminaTextureFilled, staminaStartX + i * (STAMINA_SLOT_WIDTH + STAMINA_SPACING), staminaStartY, STAMINA_SLOT_WIDTH, STAMINA_SLOT_HEIGHT); // Draw stamina slots horizontally
        }

        batch.end();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }



    public void dispose() {
        heartTexture.dispose(); // Dispose of the heart texture
        xpTextureFilled.dispose(); // Dispose of the filled XP texture
        staminaTextureFilled.dispose(); // Dispose of the filled stamina texture
        stage.dispose();
    }
}
