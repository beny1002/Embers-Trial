package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private Music mainMenuMusic;
    private Actor blackOverlay; // programmatically created black overlay
    private Texture whitePixelTexture; // texture for the black overlay

    public MainMenuScreen(Main mainApp) {
        this.mainApp = mainApp;

        //setup assets
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        titleTexture = new Texture(Gdx.files.internal("Logos/gameLogo.png"));
        skin = new Skin(Gdx.files.internal("metal-ui.json")); // reload the skin

        //load and configure background music
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainmenucrackle.mp3"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(mainApp.getPreferences().getFloat("volume", 0.5f)); // set initial volume
        mainMenuMusic.play();

        //create a white pixel texture
        createWhitePixelTexture();

        setupUI();
        setupBlackOverlay(); // programmatically create the black overlay
    }

    private void createWhitePixelTexture() {
        //create a 1x1 white pixel texture using Pixmap
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixelTexture = new Texture(pixmap);
        pixmap.dispose(); // dispose of pixmap after use
    }

    private void setupBlackOverlay() {
        blackOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0, 0, 0, getColor().a); // black with adjustable alpha
                batch.draw(whitePixelTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                batch.setColor(1, 1, 1, 1); // reset the batch color
            }
        };
        blackOverlay.setColor(0, 0, 0, 0); // fully transparent initially
        stage.addActor(blackOverlay);
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
                System.out.println("Start button clicked");

                // create a black overlay for fade-out
                Actor fadeOverlay = new Actor() {
                    @Override
                    public void draw(Batch batch, float parentAlpha) {
                        batch.setColor(0, 0, 0, getColor().a); // black with adjustable alpha
                        batch.draw(whitePixelTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                        batch.setColor(1, 1, 1, 1); // reset batch color
                    }
                };
                fadeOverlay.setColor(0, 0, 0, 0); // initially transparent
                fadeOverlay.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
                fadeOverlay.setPosition(0, 0);
                stage.addActor(fadeOverlay);

                // fade to black and delay before switching to cutscene screen
                fadeOverlay.addAction(Actions.sequence(
                    Actions.fadeIn(1f), // fade in over 1 second
                    Actions.run(() -> {
                        System.out.println("Transitioning to CutsceneScreen");
                        mainApp.setCutsceneScreen(); // switch to cutscene screen
                    })
                ));
            }
        });
        mainMenu.add(startButton)
            .width(208)
            .height(139)
            .spaceBottom(10);
        mainMenu.row();

        //options button
        ImageButton optionButton = new ImageButton(
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/options_idle.png"))),
            new TextureRegionDrawable(new Texture(Gdx.files.internal("mainMenuAssets/options_hover.png")))
        );
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                OptionsPage optionsMenu = new OptionsPage(stage, skin, mainMenu, mainMenuMusic);
                optionsMenu.showOptionsPage();
            }
        });
        mainMenu.add(optionButton)
            .width(208)
            .height(139)
            .spaceBottom(10);
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
            .width(208)
            .height(139)
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
        mainMenuMusic.dispose(); // dispose the music
        whitePixelTexture.dispose(); // dispose the white pixel texture
    }
}
