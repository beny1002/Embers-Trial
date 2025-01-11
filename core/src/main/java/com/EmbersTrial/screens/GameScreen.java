package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen {
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
}
