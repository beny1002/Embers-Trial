package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.EmbersTrial.player.Player;

public class GameScreen {
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    private FitViewport viewport;

    //Pause-related variables
    private boolean isPaused = false;
    private Stage pauseStage;
    private Skin skin;

    //TESTING VARIABLES
    private Player player;
    private Texture playerSpriteSheet;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        //stage and SpriteBatch setup
        stage = new Stage(viewport);
        batch = new SpriteBatch();

        //load background texture
        try {
            backgroundTexture = new Texture(Gdx.files.internal("map.png"));
            Gdx.app.log("GameScreen", "map.png loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to load map.png", e);
        }

        // TESTING SETUP
        try {
            playerSpriteSheet = new Texture(Gdx.files.internal("spritesheet.png"));
            player = new Player(playerSpriteSheet);
            Gdx.app.log("GameScreen", "Player sprite sheet loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to load player sprite sheet", e);
        }

        //Initialize pause menu
        initPauseMenu();
    }

    private void initPauseMenu() {
        //Set up the pause menu stage
        pauseStage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("metal-ui.json")); // Load a UI skin

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        // Add "Resume" button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(event -> {
            if (resumeButton.isPressed()) {
                isPaused = false; //Unpause the game
                Gdx.input.setInputProcessor(stage);
            }
            return true;
        });

        // Add "Exit to Main Menu" button
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                System.out.println("Exiting to Main Menu...");
                // mainApp.setMainMenuScreen();
            }
            return true;
        });

        //add buttons to the pause menu table
        pauseTable.add(resumeButton).width(200).height(50).padBottom(10);
        pauseTable.row();
        pauseTable.add(exitButton).width(200).height(50);
    }

    public void showGameScreen() {
        Gdx.input.setInputProcessor(stage);
    }

    public void render() {
        //clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        if (isPaused) {
            //render the pause menu
            pauseStage.act();
            pauseStage.draw();
        } else {
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
            }
            batch.end();

            //update game logic
            if (player != null) {
                player.update(Gdx.graphics.getDeltaTime());
            }

            //render stage elements
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        //handle pause toggle
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            togglePause();
        }
    }

    private void togglePause() {
        isPaused = !isPaused;
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
        batch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        stage.dispose();
        pauseStage.dispose();
        skin.dispose();

        //TESTING CODE:
        if (playerSpriteSheet != null) playerSpriteSheet.dispose();
    }
}


/*public class GameScreen {
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public GameScreen() {
        //object creation ;D
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera); // Adjust to your resolution
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        batch = new SpriteBatch();

        //try adding the background texture (debugging statements)
        try {
            backgroundTexture = new Texture(Gdx.files.internal("map.png"));
            Gdx.app.log("GameScreen", "map.png loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to load map.png", e);
        }
    }

    public void showGameScreen() {
        Gdx.input.setInputProcessor(stage);
    }

    public void render() {
        //clear the screen prior to render
        ScreenUtils.clear(0, 0, 0, 1);

        //make sure the camera is updated
        camera.update();

        //render the background image
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        } else {
            Gdx.app.log("GameScreen", "No background texture to draw");
        }
        batch.end();

        //render stage elements
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        batch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        stage.dispose();
    }
}*/
