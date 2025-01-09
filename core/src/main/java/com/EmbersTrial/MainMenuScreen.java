package com.EmbersTrial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen{
    private Table mainMenu;
    private Skin skin;
    private Stage stage;


    public MainMenuScreen() {
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());
        mainMenu = new Table();
        mainMenu.setFillParent(true);
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
                GameScreen gameScreen = new GameScreen();




            }
        });

        TextButton exitButton = new TextButton("exit", skin);
        mainMenu.add(exitButton);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
    public Table getMainMenu() {
        return mainMenu;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
