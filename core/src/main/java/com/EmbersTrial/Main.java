package com.EmbersTrial;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;

    private FitViewport uiViewport;

    @Override
    public void create() {
        // setting up the ui viewport to handle button and menu scaling
        uiViewport = new FitViewport(1920, 1080);

        // setting up the stage with the viewport
        stage = new Stage(uiViewport);
        Gdx.input.setInputProcessor(stage);

        // loading the background image and setting filtering for smooth scaling
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // initializing the sprite batch for rendering
        spriteBatch = new SpriteBatch();

        // loading the ui skin
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        // setting up the main menu table
        Table mainMenu = new Table();
        mainMenu.setFillParent(true); // this makes the table stretch to fit the viewport
        stage.addActor(mainMenu);

        // adding the game title to the menu
        Texture titleTexture = new Texture(Gdx.files.internal("Logos/gameLogo.png"));
        Image gameTitleImage = new Image(titleTexture);
        mainMenu.add(gameTitleImage)
            .width(uiViewport.getWorldWidth() * 0.5f) // take up half the viewport width
            .height(uiViewport.getWorldHeight() * 0.2f) // take up 20% of viewport height
            .spaceBottom(20);
        mainMenu.row(); // move to the next row for buttons

        // adding the start button
        TextButton startButton = new TextButton("Start Game", skin);
        startButton.getLabel().setFontScale(2f); //This line is temporary to scale the font size
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                CutsceneScreen cutsceneScreen = new CutsceneScreen(stage, skin);
                cutsceneScreen.showCutscene(() -> System.out.println("Cutscene completed"));
            }
        });
        mainMenu.add(startButton)
            .width(uiViewport.getWorldWidth() * 0.3f) // 30% of the viewport width
            .height(uiViewport.getWorldHeight() * 0.1f) // 10% of the viewport height
            .spaceBottom(10);
        mainMenu.row();

        // adding the options button
        TextButton optionButton = new TextButton("Options", skin);
        optionButton.getLabel().setFontScale(2f); //This line is temporary to scale the font size
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                OptionsPage optionsMenu = new OptionsPage(stage, skin, mainMenu);
                optionsMenu.showOptionsPage();
            }
        });
        mainMenu.add(optionButton)
            .width(uiViewport.getWorldWidth() * 0.3f)
            .height(uiViewport.getWorldHeight() * 0.1f)
            .spaceBottom(10);
        mainMenu.row();

        // adding the exit button
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(2f); //This line is temporary to scale the font size
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainMenu.add(exitButton)
            .width(uiViewport.getWorldWidth() * 0.3f)
            .height(uiViewport.getWorldHeight() * 0.1f);
    }

    @Override
    public void resize(int width, int height) {
        // updates the viewport when the screen size changes
        uiViewport.update(width, height, true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        // clearing the screen
        ScreenUtils.clear(1, 1, 1, 1);

        // drawing the background
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        spriteBatch.end();

        // rendering the ui
        stage.act();
        stage.draw();
    }

    @Override
    public void pause() {
        // not used but needed for the interface
    }

    @Override
    public void resume() {
        // not used but needed for the interface
    }

    @Override
    public void dispose() {
        // cleaning up resources when the app closes
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}
