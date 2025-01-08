package com.EmbersTrial;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Stage stage;
    Skin skin;

    @Override
    public void create() {
        backgroundTexture = new Texture("bacground.png");


        menuBox = new Texture("emberx64.png");
        viewport = new FitViewport(8, 5);

        spriteBatch = new SpriteBatch();
        */

        // Load the texture for menuBox (from local assets directory)
        menuBox = new Texture(Gdx.files.internal("icons/emberx64.png"));

        // Set up skin and stage for UI

        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table mainMenu = new Table();
        Table loadingScreen = new Table();
        Table gameScreen = new Table();
        mainMenu.setFillParent(true);
        loadingScreen.setFillParent(true);
        gameScreen.setFillParent(true);
        stage.addActor(mainMenu);


        // Create the root table for UI layout
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.padTop(100);


        mainMenu.padTop(100);
        Label gameTitle = new Label("Ember's Trials", skin);
        gameTitle.setFontScale(2);
        mainMenu.add(gameTitle).spaceBottom(20);
        mainMenu.row();

        TextButton startButton = new TextButton("start", skin);
        root.add(startButton).width(200).height(50).spaceBottom(10);
        root.row();

        TextButton optionButton = new TextButton("options", skin);
        root.add(optionButton);
        mainMenu.add(startButton).width(200).height(50).spaceBottom(10);
        mainMenu.row();
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(loadingScreen);
                loadingScreen.add(new Label("loading...", skin));
                //if loading is done stage.clear(); and stage.addActor(gameScreen);
            }
        });

        TextButton exitButton = new TextButton("exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainMenu.add(exitButton);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.WHITE);

        stage.act();
        stage.draw();

        /*
        input();
        logic();
        draw();
         */
    }
    /*
    private void input() {

    }
    private void logic() {

    }
    private void draw() {

        /* some tests for background
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.begin();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        spriteBatch.draw(menuBox, 1, 3, 3, 1);
        spriteBatch.draw(menuBox, 1, 2, 2, 1);

        spriteBatch.end();
    }

    private void createButton(String name,  float x, float y, float width, float height) {
        Sprite sprite = new Sprite(menuBox);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (menuBox != null) menuBox.dispose(); // Dispose of menuBox if loaded
    }
}
