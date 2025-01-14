package com.EmbersTrial.WaveSystem;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;

public class Waves extends ApplicationAdapter {
    private Stage stage;
    private Label waveLabel;
    private int currentWave = 1;
    private long lastWaveTime;

    // Assuming existing Stage is passed via constructor
    public Waves(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void create() {
        // Set up the label style
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        waveLabel = new Label("Wave: " + currentWave, skin);

        // Align the label to the top-right corner
        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.add(waveLabel).padTop(10).padRight(10);
        stage.addActor(table);

        // Initialize wave timer
        lastWaveTime = TimeUtils.millis();

        // Immediately update the label to display the initial wave number
        waveLabel.setText("Wave: " + currentWave);
    }

    public void updateWave() {
        // Update wave number every 5 seconds
        if (TimeUtils.timeSinceMillis(lastWaveTime) > 5000) {
            currentWave++;
            waveLabel.setText("Wave: " + currentWave);
            lastWaveTime = TimeUtils.millis();
        }
    }

    @Override
    public void render() {
        // Just call the updateWave method and let the existing rendering system handle the rest
        updateWave();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        // Dispose of any resources if needed
        stage.dispose();
    }
}
