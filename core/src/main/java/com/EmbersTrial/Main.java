package com.EmbersTrial;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    private Texture backgroundTexture;
    private Texture menuBox;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;

    @Override
    public void create() {
        // Load background and menu box textures
        backgroundTexture = new Texture("background.png");
        menuBox = new Texture(Gdx.files.internal("icons/emberx64.png"));

        // Initialize sprite batch
        spriteBatch = new SpriteBatch();

        // Set up skin and stage for UI
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create the main menu table
        Table mainMenu = new Table();
        mainMenu.setFillParent(true);
        stage.addActor(mainMenu);

        // Add a game title
        Label gameTitle = new Label("Ember's Trials", skin);
        gameTitle.setFontScale(2);
        mainMenu.add(gameTitle).spaceBottom(20);
        mainMenu.row();

        // Add buttons to the main menu
        TextButton startButton = new TextButton("Start", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                Table loadingScreen = new Table();
                loadingScreen.setFillParent(true);
                loadingScreen.add(new Label("Loading...", skin));
                stage.addActor(loadingScreen);
                // Simulate loading complete: Clear and show gameScreen
                Gdx.app.postRunnable(() -> {
                    stage.clear();
                    Table gameScreen = new Table();
                    gameScreen.setFillParent(true);
                    gameScreen.add(new Label("Game Screen", skin));
                    stage.addActor(gameScreen);
                });
            }
        });
        mainMenu.add(startButton).width(200).height(50).spaceBottom(10);
        mainMenu.row();

        TextButton optionButton = new TextButton("Options", skin);
        mainMenu.add(optionButton).width(200).height(50).spaceBottom(10);
        mainMenu.row();

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainMenu.add(exitButton).width(200).height(50);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        // Clear the screen
        ScreenUtils.clear(Color.WHITE);

        // Begin drawing the background
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // Render the stage
        stage.act();
        stage.draw();
    }


    @Override
    public void pause() {
        // Invoked when the application is paused
    }

    @Override
    public void resume() {
        // Invoked when the application is resumed after pause
    }

    @Override
    public void dispose() {
        // Dispose resources
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (menuBox != null) menuBox.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}
