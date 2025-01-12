package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen {
    private Stage stage;
    private Texture backgroundTexture;
    private Texture titleTexture;
    private FitViewport viewport;
    private Main mainApp;
    private Skin skin;

    public MainMenuScreen(Main mainApp) {
        this.mainApp = mainApp;

        //setup assets
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        titleTexture = new Texture(Gdx.files.internal("Logos/gameLogo.png"));
        skin = new Skin(Gdx.files.internal("metal-ui.json")); // reload the skin

        setupUI();
    }

    private void setupUI() {
        Table mainMenu = new Table();
        mainMenu.setFillParent(true);
        stage.addActor(mainMenu);

        //title
        com.badlogic.gdx.scenes.scene2d.ui.Image gameTitleImage = new com.badlogic.gdx.scenes.scene2d.ui.Image(titleTexture);
        mainMenu.add(gameTitleImage)
            .width(viewport.getWorldWidth() * 0.5f)
            .height(viewport.getWorldHeight() * 0.2f)
            .spaceBottom(20);
        mainMenu.row();

        //start game button
        ImageButton startButton = new ImageButton(
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/start_idle.png"))),
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/start_hover.png")))
        );
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainApp.setCutsceneScreen(); // transition to cutscene screen
            }
        });
        mainMenu.add(startButton)
            .width(208) // original button width
            .height(139) // original button height
            .spaceBottom(10); // reduced spacing
        mainMenu.row();

        //options button
        ImageButton optionButton = new ImageButton(
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/options_idle.png"))),
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/options_hover.png")))
        );
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // instantiate and display options menu
                OptionsPage optionsMenu = new OptionsPage(stage, skin, mainMenu);
                optionsMenu.showOptionsPage();
            }
        });
        mainMenu.add(optionButton)
            .width(208) // original button width
            .height(139) // original button height
            .spaceBottom(10); // reduced spacing
        mainMenu.row();

        //exit button
        ImageButton exitButton = new ImageButton(
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/exit_idle.png"))),
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/exit_hover.png")))
        );
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainMenu.add(exitButton)
            .width(208) // original button width
            .height(139) // original button height
            .spaceBottom(10);
    }

    public void render() {
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.getBatch().end();

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        titleTexture.dispose();
        skin.dispose(); // dispose the skin
    }
}
