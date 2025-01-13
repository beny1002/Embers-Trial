package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.EmbersTrial.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen {
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Pause-related variables
    private boolean isPaused = false;
    private Stage pauseStage;
    private Skin skin;
    private float pauseCooldown = 0.2f; // Debounce time for ESC key
    private float pauseCooldownTimer = 0f;

    // Player-related variables
    private Player player;
    private Texture playerSpriteSheet;
    private Main mainApp;
    private boolean disposed = false; // Prevent use of disposed resources

    public GameScreen(Main mainApp) {
        this.mainApp = mainApp;

        // Camera and viewport setup
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // Stage and SpriteBatch setup
        stage = new Stage(viewport);
        batch = new SpriteBatch();

        // Load background texture
        try {
            backgroundTexture = new Texture(Gdx.files.internal("map.png"));
            Gdx.app.log("GameScreen", "map.png loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to load map.png", e);
        }

        // Initialize player
        try {
            playerSpriteSheet = new Texture(Gdx.files.internal("player_stand_front.png"));
            player = new Player(playerSpriteSheet);
            Gdx.app.log("GameScreen", "Player initialized successfully.");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to initialize player", e);
        }

        // Initialize pause menu
        initPauseMenu();
    }

    private void initPauseMenu() {
        pauseStage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        // Resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(event -> {
            if (resumeButton.isPressed()) {
                togglePause(false); // Unpause the game
            }
            return true;
        });

        // Exit to main menu button
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                Gdx.app.log("GameScreen", "Exiting to Main Menu...");
                mainApp.setMainMenuScreen();
                dispose(); // Dispose resources after transitioning
            }
            return true;
        });

        pauseTable.add(resumeButton).width(200).height(50).padBottom(10);
        pauseTable.row();
        pauseTable.add(exitButton).width(200).height(50);
    }

    public void render() {
        if (disposed) {
            Gdx.app.log("GameScreen", "Render called on a disposed GameScreen. Skipping frame.");
            return;
        }

        // Update the pause cooldown timer
        if (pauseCooldownTimer > 0) {
            pauseCooldownTimer -= Gdx.graphics.getDeltaTime();
        }

        // Handle pause toggle
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) && pauseCooldownTimer <= 0) {
            togglePause(!isPaused);
            pauseCooldownTimer = pauseCooldown; // Reset cooldown timer
        }

        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Render the game
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        if (player != null) {
            player.render(batch);
        }
        batch.end();

        // Update game logic if not paused
        if (!isPaused && player != null) {
            player.update(Gdx.graphics.getDeltaTime());
        }

        // Render the transparent black overlay and pause menu if paused
        if (isPaused) {
            renderPauseOverlay();
            pauseStage.act();
            pauseStage.draw();
        } else {
            // Render stage elements
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    private void renderPauseOverlay() {
        // Render a transparent black rectangle over the entire screen
        batch.begin();
        batch.setColor(0, 0, 0, 0.5f); // Semi-transparent black
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
        batch.setColor(Color.WHITE); // Reset color to default
    }

    private void togglePause(boolean pause) {
        isPaused = pause;
        if (isPaused) {
            Gdx.input.setInputProcessor(pauseStage); // Set input to pause menu
        } else {
            Gdx.input.setInputProcessor(stage); // Return input to the game
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        if (disposed) {
            Gdx.app.log("GameScreen", "dispose() called on an already disposed GameScreen.");
            return;
        }
        disposed = true;

        if (batch != null) batch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (stage != null) stage.dispose();
        if (pauseStage != null) pauseStage.dispose();
        if (skin != null) skin.dispose();
        if (playerSpriteSheet != null) playerSpriteSheet.dispose();
    }
}
