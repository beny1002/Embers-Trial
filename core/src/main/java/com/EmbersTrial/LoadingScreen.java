package com.EmbersTrial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScreen {
    private Stage stage;
    private Skin skin;

    public LoadingScreen(Table table) {
    this.skin = new Skin(Gdx.files.internal("metal-ui.json"));

    table.clear();
    table.add(new Label("Loading...", skin));
    }

}
