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
    private Actor blackOverlay;
    private Texture whitePixelTexture;

    public MainMenuScreen(Main mainApp) {
        this.mainApp = mainApp;

        // Setup assets
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        titleTexture = new Texture(Gdx.files.internal("Logos/gameLogo.png"));
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        // Load and configure background music
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("dungeon ambiance.wav"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(mainApp.getPreferences().getFloat("volume", 0.5f));
        mainMenuMusic.play();

        // Create a white pixel texture
        createWhitePixelTexture();

        setupUI();
        setupBlackOverlay();
    }

    private void createWhitePixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    private void setupBlackOverlay() {
        blackOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0, 0, 0, getColor().a);
                batch.draw(whitePixelTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                batch.setColor(1, 1, 1, 1);
            }
        };
        blackOverlay.setColor(0, 0, 0, 0);
        stage.addActor(blackOverlay);
    }

    private void setupUI() {
        Table mainMenu = new Table();
        mainMenu.setFillParent(true);
        stage.addActor(mainMenu);

        // Title
        com.badlogic.gdx.scenes.scene2d.ui.Image gameTitleImage = new com.badlogic.gdx.scenes.scene2d.ui.Image(titleTexture);
        mainMenu.add(gameTitleImage)
            .width(viewport.getWorldWidth() * 0.7f)
            .height(viewport.getWorldHeight() * 0.25f)
            .spaceBottom(30);
        mainMenu.row();

        // Start game button
        ImageButton startButton = createCustomButton(
            "start_button_bright.png",
            "start_button_hover.png",
            2.0f,
            this::fadeToCutsceneScreen
        );
        mainMenu.add(startButton)
            .width(250) // Button hitbox width
            .height(120) // Button hitbox height
            .spaceBottom(15);
        mainMenu.row();

        // Options button
        ImageButton optionButton = createCustomButton(
            "options_button_bright.png",
            "options_button_hover.png",
            2.0f,
            () -> {
                OptionsPage optionsMenu = new OptionsPage(stage, skin, mainMenu, mainMenuMusic);
                optionsMenu.showOptionsPage();
            }
        );
        mainMenu.add(optionButton)
            .width(250)
            .height(120)
            .spaceBottom(15);
        mainMenu.row();

        // Exit button
        ImageButton exitButton = createCustomButton(
            "exit_button_bright.png",
            "exit_button_hover.png",
            2.0f,
            Gdx.app::exit
        );
        mainMenu.add(exitButton)
            .width(250)
            .height(120)
            .spaceBottom(15);
    }

    private ImageButton createCustomButton(String normalTexture, String hoverTexture, float scaleFactor, Runnable onClickAction) {
        TextureRegionDrawable normalDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal(normalTexture)));
        TextureRegionDrawable hoverDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal(hoverTexture)));
        ImageButton button = new ImageButton(normalDrawable, hoverDrawable);

        // Scale the image inside the button
        button.getImage().setScale(scaleFactor);

        // Center the image after scaling
        button.getImageCell().pad(0).center();
        button.getImage().setOrigin(button.getImage().getWidth() / 2, button.getImage().getHeight() / 2);

        // Add click listener
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onClickAction.run();
                return true;
            }
        });

        return button;
    }

    private void fadeToCutsceneScreen() {
        Actor fadeOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0, 0, 0, getColor().a);
                batch.draw(whitePixelTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                batch.setColor(1, 1, 1, 1);
            }
        };
        fadeOverlay.setColor(0, 0, 0, 0);
        fadeOverlay.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        fadeOverlay.setPosition(0, 0);
        stage.addActor(fadeOverlay);

        fadeOverlay.addAction(Actions.sequence(
            Actions.fadeIn(1f),
            Actions.run(mainApp::setCutsceneScreen)
        ));
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
        skin.dispose();
        mainMenuMusic.dispose();
        whitePixelTexture.dispose();
    }
}
