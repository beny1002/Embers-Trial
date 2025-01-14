package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CutsceneScreen {
    private Stage stage;
    private Skin skin;
    private FitViewport viewport;
    private TextButton skipButton;
    private boolean buttonAdded = false; //track if the skip button has been added
    private InputAdapter cutsceneInputAdapter;
    private Actor fadeOverlay; // fade overlay actor
    private Texture blackTexture; // dynamic texture for black overlay

    public CutsceneScreen(Runnable onComplete) {
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        createBlackTexture(); // create the black texture for the overlay
        setupFadeOverlay(); // add the fade overlay
        setupCutscene(onComplete);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        cutsceneInputAdapter = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!buttonAdded) {
                    setupSkipButton(onComplete);
                    buttonAdded = true;
                }
                return true;
            }
        };

        multiplexer.addProcessor(cutsceneInputAdapter);
        Gdx.input.setInputProcessor(multiplexer);

        fadeOverlay.addAction(Actions.fadeOut(1f)); // fade in the scene
    }

    private void createBlackTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    private void setupFadeOverlay() {
        fadeOverlay = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0, 0, 0, getColor().a);
                batch.draw(blackTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                batch.setColor(1, 1, 1, 1);
            }
        };
        fadeOverlay.setColor(0, 0, 0, 1);
        fadeOverlay.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(fadeOverlay);
    }

    private void setupCutscene(Runnable onComplete) {
        Table dialogueTable = new Table();
        dialogueTable.setFillParent(true);
        stage.addActor(dialogueTable);

        String[] dialogue = {
            "This is a test dialogue.",
            "This is a work in progress.",
            "This game is called Ember's Trial."
        };

        for (int i = 0; i < dialogue.length; i++) {
            String line = dialogue[i];
            Label dialogueLabel = new Label(line, skin);
            dialogueLabel.setFontScale(2.0f);
            dialogueLabel.getColor().a = 0;

            dialogueTable.row();
            dialogueTable.add(dialogueLabel).pad(10);
            dialogueLabel.addAction(Actions.sequence(
                Actions.delay(i * 2f),
                Actions.fadeIn(1f)
            ));
        }

        stage.addAction(Actions.sequence(
            Actions.delay(dialogue.length * 2f + 2f),
            Actions.run(() -> {
                disposeInput();
                onComplete.run();
            })
        ));
    }

    private void setupSkipButton(Runnable onComplete) {
        skipButton = new TextButton("Skip", skin);
        skipButton.setSize(200, 80);
        skipButton.setPosition(viewport.getWorldWidth() - skipButton.getWidth() - 20, 20);

        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                disposeInput();
                stage.clear();
                onComplete.run();
            }
        });

        stage.addActor(skipButton);
    }

    private void disposeInput() {
        if (Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
            multiplexer.removeProcessor(cutsceneInputAdapter);
        }
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        if (buttonAdded && skipButton != null) {
            skipButton.setPosition(viewport.getWorldWidth() - skipButton.getWidth() - 20, 20);
        }
    }

    public void dispose() {
        disposeInput();
        stage.clear();
        stage.dispose();
        skin.dispose();
        blackTexture.dispose();
    }
}
