package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen {
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture titleTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Main mainApp;

    public MainMenuScreen(Main mainApp) {
        this.mainApp = mainApp;

        //setup assets
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        spriteBatch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        titleTexture = new Texture(Gdx.files.internal("Logos/gameLogo.png"));
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        setupUI();
    }

    private void setupUI() {
        Table mainMenu = new Table();
        mainMenu.setFillParent(true);
        stage.addActor(mainMenu);

        //title
        Image gameTitleImage = new Image(titleTexture);
        mainMenu.add(gameTitleImage)
            .width(viewport.getWorldWidth() * 0.5f)
            .height(viewport.getWorldHeight() * 0.2f)
            .spaceBottom(20);
        mainMenu.row();

        //start Game Button
        TextButton startButton = new TextButton("Start Game", skin);
        startButton.getLabel().setFontScale(2f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                mainApp.setCutsceneScreen(); // Transition to CutsceneScreen
            }
        });
        mainMenu.add(startButton)
            .width(viewport.getWorldWidth() * 0.3f)
            .height(viewport.getWorldHeight() * 0.1f)
            .spaceBottom(10);
        mainMenu.row();

        //adding the options button
        TextButton optionButton = new TextButton("Options", skin);
        optionButton.getLabel().setFontScale(2f); //This line is temporary to scale the font size
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                OptionsPage optionsMenu = new OptionsPage(stage, skin, mainMenu);
                optionsMenu.showOptionsPage();
            }
        });
        mainMenu.add(optionButton)
            .width(viewport.getWorldWidth() * 0.3f)
            .height(viewport.getWorldHeight() * 0.1f)
            .spaceBottom(10);
        mainMenu.row();

        //exit Button
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(2f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainMenu.add(exitButton)
            .width(viewport.getWorldWidth() * 0.3f)
            .height(viewport.getWorldHeight() * 0.1f);
    }

    public void render() {
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        stage.dispose();
        spriteBatch.dispose();
        backgroundTexture.dispose();
        titleTexture.dispose();
        skin.dispose();
    }
}
