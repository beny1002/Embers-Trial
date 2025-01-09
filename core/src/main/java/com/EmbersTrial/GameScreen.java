package com.EmbersTrial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen {
    private Stage stage;
    private Skin skin;
    private SpriteBatch spritebatch;
    private FitViewport viewport;
    private Texture background;
    private Texture player;


    public GameScreen() {
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        background = new Texture("menu_bg_1.png");
        player = new Texture("player_unarmed.png");
        spritebatch = new SpriteBatch();
        viewport = new FitViewport(8,5);
    }
    public void show() {
    }

    public void render() {
        viewport.apply();
        spritebatch.setProjectionMatrix(viewport.getCamera().combined);
        ScreenUtils.clear(Color.BLACK);
        spritebatch.begin();

        spritebatch.end();


    }

    public void dispose() {
        stage.dispose();
    }
    public void setStage(Stage stage) {
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);
    }



}
