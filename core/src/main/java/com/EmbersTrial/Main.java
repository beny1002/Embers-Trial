package com.EmbersTrial;

import com.EmbersTrial.screens.CutsceneScreen;
import com.EmbersTrial.screens.GameScreen;
import com.EmbersTrial.screens.MainMenuScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main implements ApplicationListener {
    private MainMenuScreen mainMenuScreen;
    private CutsceneScreen cutsceneScreen;
    private GameScreen gameScreen;
    private String currentScreen;

    private Stage transitionStage; // global stage for transitions
    private Actor fadeOverlay; // global fade overlay
    private Texture blackTexture; // black texture for overlay

    @Override
    public void create() {
        // Initialize screens
        mainMenuScreen = new MainMenuScreen(this);
        currentScreen = "mainMenu"; // Start with the main menu

        // Set up transition overlay
        setupTransitionOverlay();
    }

    private void setupTransitionOverlay() {
        transitionStage = new Stage();
        createBlackTexture();

        // Create fade overlay actor
        fadeOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0, 0, 0, getColor().a * parentAlpha);
                batch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.setColor(1, 1, 1, 1); // Reset batch color
            }
        };
        fadeOverlay.setColor(0, 0, 0, 0); // Fully transparent initially
        transitionStage.addActor(fadeOverlay);
    }

    private void createBlackTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void transitionToScreen(Runnable screenSwitchAction) {
        fadeOverlay.clearActions(); // Clear any previous actions to avoid stacking
        fadeOverlay.addAction(Actions.sequence(
            Actions.color(new Color(0, 0, 0, 1), 0.5f), // Fade to black over 0.5 seconds
            Actions.run(screenSwitchAction), // Switch to the new screen
            Actions.color(new Color(0, 0, 0, 0), 0.5f) // Fade back in over 0.5 seconds
        ));
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.DARK_GRAY); // Set a neutral background

        // Render the current screen
        if (currentScreen != null) {
            switch (currentScreen) {
                case "mainMenu":
                    mainMenuScreen.render();
                    break;
                case "cutscene":
                    cutsceneScreen.render();
                    break;
                case "game":
                    gameScreen.render();
                    break;
            }
        }

        // Render the transition overlay on top
        transitionStage.act();
        transitionStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resize the active screen
        if (currentScreen != null) {
            switch (currentScreen) {
                case "mainMenu":
                    mainMenuScreen.resize(width, height);
                    break;
                case "cutscene":
                    cutsceneScreen.resize(width, height);
                    break;
                case "game":
                    gameScreen.resize(width, height);
                    break;
            }
        }

        // Update the transition stage's viewport
        transitionStage.getViewport().update(width, height, true);
    }

    public void setGameScreen() {
        transitionToScreen(() -> {
            if (cutsceneScreen != null) {
                cutsceneScreen.dispose();
                cutsceneScreen = null;
            }
            if (gameScreen == null) {
                gameScreen = new GameScreen(this);
            }
            currentScreen = "game";
        });
    }

    public void setCutsceneScreen() {
        transitionToScreen(() -> {
            if (mainMenuScreen != null) {
                mainMenuScreen.dispose();
                mainMenuScreen = null;
            }
            if (cutsceneScreen == null) {
                cutsceneScreen = new CutsceneScreen(this::setGameScreen);
            }
            currentScreen = "cutscene";
        });
    }

    public void setMainMenuScreen() {
        transitionToScreen(() -> {
            if (gameScreen != null) {
                gameScreen.dispose();
                gameScreen = null;
            }
            if (mainMenuScreen == null) {
                mainMenuScreen = new MainMenuScreen(this);
            }
            currentScreen = "mainMenu";
        });
    }

    @Override
    public void dispose() {
        // Dispose of all resources
        if (mainMenuScreen != null) mainMenuScreen.dispose();
        if (cutsceneScreen != null) cutsceneScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();
        if (blackTexture != null) blackTexture.dispose();
        transitionStage.dispose();
    }

    public Preferences getPreferences() {
        return Gdx.app.getPreferences("EmberTrials");
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
