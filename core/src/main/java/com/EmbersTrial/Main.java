package com.EmbersTrial;

import com.EmbersTrial.screens.CutsceneScreen;
import com.EmbersTrial.screens.GameScreen;
import com.EmbersTrial.screens.MainMenuScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main implements ApplicationListener {
    private MainMenuScreen mainMenuScreen;
    private CutsceneScreen cutsceneScreen;
    private GameScreen gameScreen;
    private String currentScreen;

    @Override
    public void create() {
        mainMenuScreen = new MainMenuScreen(this);
        currentScreen = "mainMenu"; //start with the main menu
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1); //clear screen

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

    @Override
    public void resize(int width, int height) {
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

    public void setGameScreen() {
        if (cutsceneScreen != null) {
            cutsceneScreen.dispose();
            cutsceneScreen = null;
        }
        gameScreen = new GameScreen();
        currentScreen = "game";
    }

    public void setCutsceneScreen() {
        if (mainMenuScreen != null) {
            mainMenuScreen.dispose();
            mainMenuScreen = null;
        }
        cutsceneScreen = new CutsceneScreen(() -> setGameScreen());
        currentScreen = "cutscene";
    }

    @Override
    public void dispose() {
        if (mainMenuScreen != null) mainMenuScreen.dispose();
        if (cutsceneScreen != null) cutsceneScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
}
