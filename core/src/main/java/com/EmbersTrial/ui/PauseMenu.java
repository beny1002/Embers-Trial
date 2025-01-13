package com.EmbersTrial.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseMenu {

    private Stage stage;
    private Skin skin;
    private ShapeRenderer overlayRenderer;

    public PauseMenu(Runnable onResume, Runnable onExit) {
        // Initialize stage and overlay renderer
        stage = new Stage(new ScreenViewport());
        overlayRenderer = new ShapeRenderer();

        // Load skin (replace with appropriate skin file if necessary)
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        // Create pause menu layout
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();
        stage.addActor(pauseTable);

        // Resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(event -> {
            if (resumeButton.isPressed()) {
                onResume.run();
            }
            return true;
        });

        // Exit button
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                onExit.run();
            }
            return true;
        });

        pauseTable.add(resumeButton).width(200).height(50).padBottom(20);
        pauseTable.row();
        pauseTable.add(exitButton).width(200).height(50);
    }

    public void render(float delta) {
        // Render a transparent black overlay
        overlayRenderer.begin(ShapeRenderer.ShapeType.Filled);
        overlayRenderer.setColor(0, 0, 0, 0.5f); // Semi-transparent black
        overlayRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overlayRenderer.end();

        // Draw the pause menu
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        overlayRenderer.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
