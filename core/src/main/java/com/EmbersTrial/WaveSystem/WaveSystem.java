
    package com.EmbersTrial.WaveSystem;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

    public class WaveSystem extends ApplicationAdapter {
        private SpriteBatch batch;
        private OrthographicCamera camera;
        private Stage stage;
        private Label waveLabel;
        private int currentWave = 1;
        private long lastWaveTime;

        @Override
        public void create() {
            batch = new SpriteBatch();
            camera = new OrthographicCamera();
            stage = new Stage(new ScreenViewport(), batch);

            // Set up the label style
            Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
            waveLabel = new Label("Wave: " + currentWave, skin);

            // Align the label to the top-right corner
            Table table = new Table();
            table.setFillParent(true);
            table.top().right();
            table.add(waveLabel).pad(10);
            stage.addActor(table);

            // Initialize wave timer
            lastWaveTime = TimeUtils.millis();
        }

        @Override
        public void render() {
            // Clear the screen
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Update wave number every 5 seconds
            if (TimeUtils.timeSinceMillis(lastWaveTime) > 5000) {
                currentWave++;
                waveLabel.setText("Wave: " + currentWave);
                lastWaveTime = TimeUtils.millis();
            }

            // Draw the stage
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        @Override
        public void resize(int width, int height) {
            stage.getViewport().update(width, height, true);
        }

        @Override
        public void dispose() {
            batch.dispose();
            stage.dispose();
        }
    }


