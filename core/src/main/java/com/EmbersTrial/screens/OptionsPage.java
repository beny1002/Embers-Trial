package com.EmbersTrial.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsPage {

    private Stage stage;
    private Skin skin;
    private Table mainMenu;

    public OptionsPage(Stage stage, Skin skin, Table mainMenu) {
        this.stage = stage;
        this.skin = skin;
        this.mainMenu = mainMenu;
    }

    public void showOptionsPage() {
        stage.clear();

        Table optionsTable = new Table();
        optionsTable.setFillParent(true);
        stage.addActor(optionsTable);

        Label optionsTitle = new Label("Options", skin);
        optionsTitle.setFontScale(2);
        optionsTable.add(optionsTitle).spaceBottom(20);
        optionsTable.row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(mainMenu);
            }
        });

        optionsTable.add(backButton).width(200).height(50).spaceTop(20);
    }
}
