package com.EmbersTrial;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    //Texture backgroundTexture;
    Texture menuBox;
    SpriteBatch spriteBatch;
    //FitViewport viewport;
    Stage stage;
    Skin skin;


    @Override
    public void create() {
        /* background tests
        backgroundTexture = new Texture("bacground.png");

        menuBox = new Texture("emberx64.png");
        viewport = new FitViewport(8, 5);

        spriteBatch = new SpriteBatch();
        */
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        skin = new Skin(Gdx.files.internal("buttonSkin.json"));

        Button button = new Button(skin);
        root.add(button);
        root.row();
        Button ass = new Button(skin);
        root.add(ass);
        root.left();


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
    */

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
