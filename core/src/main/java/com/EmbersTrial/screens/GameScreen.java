package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.EmbersTrial.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    //pause-related variables
    private boolean isPaused = false;
    private Stage pauseStage;
    private Skin skin;
    private float pauseCooldown = 0.2f; //debounce time for ESC key
    private float pauseCooldownTimer = 0f;
    private ShapeRenderer shapeRenderer; //used to render the transparent overlay

    //player-related variables
    private Player player;
    private Texture playerSpriteSheet;
    private Main mainApp;

    //track if the screen has been disposed
    private boolean disposed = false;

    public GameScreen(Main mainApp) {
        this.mainApp = mainApp;

        //camera and viewport setup
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        //stage and SpriteBatch setup
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        //load background texture
        try {
            backgroundTexture = new Texture(Gdx.files.internal("map.png"));
            Gdx.app.log("GameScreen", "map.png loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to load map.png", e);
        }

        //initialize player
        try {
            playerSpriteSheet = new Texture(Gdx.files.internal("player_stand_front.png"));
            player = new Player(playerSpriteSheet);
            Gdx.app.log("GameScreen", "Player initialized successfully.");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to initialize player", e);
        }

        //initialize pause menu
        initPauseMenu();
    }

    private void initPauseMenu() {
        pauseStage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        //resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(event -> {
            if (resumeButton.isPressed()) {
                togglePause(false); //unpause the game
                pauseStage.unfocus(resumeButton); //reset button focus
            }
            return true;
        });

        //exit to main menu button
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                Gdx.app.log("GameScreen", "Exiting to Main Menu...");
                mainApp.setMainMenuScreen(); //transition to MainMenuScreen
                dispose(); //clean up resources for GameScreen
            }
            return true;
        });

        pauseTable.add(resumeButton).width(200).height(50).padBottom(10);
        pauseTable.row();
        pauseTable.add(exitButton).width(200).height(50);
    }

    public void render() {
        if (disposed) return; //skip rendering if the screen has been disposed

        //update the pause cooldown timer
        if (pauseCooldownTimer > 0) {
            pauseCooldownTimer -= Gdx.graphics.getDeltaTime();
        }

        //handle pause toggle
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) && pauseCooldownTimer <= 0) {
            togglePause(!isPaused);
            pauseCooldownTimer = pauseCooldown; //reset cooldown timer
        }

        //clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        if (isPaused) {
            //render the game under the transparent overlay
            renderGame();

            //draw transparent black overlay
            drawTransparentOverlay();

            //render the pause menu
            if (pauseStage != null) {
                pauseStage.act();
                pauseStage.draw();
            }
        } else {
            //render the game normally
            renderGame();

            //update game logic
            updateGameLogic();
        }
    }

    private void renderGame() {
        //update the camera
        camera.update();

        //render the game
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        //render the player
        if (player != null) {
            player.render(batch);
        } else {
            Gdx.app.error("GameScreen", "Player is null during rendering!");
        }
        batch.end();

        //render stage elements
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void updateGameLogic() {
        //update player and other game logic only when not paused
        if (player != null) {
            player.update(Gdx.graphics.getDeltaTime());
        }
    }

    private void drawTransparentOverlay() {
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.5f)); //black with 50% opacity
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
    }

    private void togglePause(boolean pause) {
        if (isPaused == pause) return; //prevent redundant toggles
        isPaused = pause;
        Gdx.input.setInputProcessor(isPaused ? pauseStage : stage);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        disposed = true; //mark the screen as disposed

        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
            backgroundTexture = null;
        }
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        if (pauseStage != null) {
            pauseStage.dispose();
            pauseStage = null;
        }
        if (skin != null) {
            skin.dispose();
            skin = null;
        }
        if (playerSpriteSheet != null) {
            playerSpriteSheet.dispose();
            playerSpriteSheet = null;
        }
    }
}
