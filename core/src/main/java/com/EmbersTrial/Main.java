package com.EmbersTrial;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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


        mainMenu.padTop(100);
        Label gameTitle = new Label("Ember's Trials", skin);
        gameTitle.setFontScale(2);
        mainMenu.add(gameTitle).spaceBottom(20);
        mainMenu.row();

        TextButton startButton = new TextButton("start", skin);
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
    }
}
