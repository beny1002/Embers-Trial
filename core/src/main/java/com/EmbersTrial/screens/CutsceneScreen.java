package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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

    public CutsceneScreen(Runnable onComplete) {
        //set up stage and viewport
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        setupCutscene(onComplete);

        //set up input handling with inputmultiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); //stage must handle button clicks

        //initialize inputadapter
        cutsceneInputAdapter = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Gdx.app.log("CutsceneScreen", "Screen clicked: Adding skip button.");
                if (!buttonAdded) {
                    setupSkipButton(onComplete); //add skip button on first click
                    buttonAdded = true;
                }
                return true; //consume the event
            }
        };

        multiplexer.addProcessor(cutsceneInputAdapter);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setupCutscene(Runnable onComplete) {
        stage.clear();

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
            dialogueLabel.getColor().a = 0; //start initially transparent

            dialogueTable.row();
            dialogueTable.add(dialogueLabel).pad(10);
            dialogueLabel.addAction(Actions.sequence(
                Actions.delay(i * 2f), //delay before showing
                Actions.fadeIn(1f) //fade in over 1 second
            ));
        }

        //transition to gamescreen after dialogue ends
        stage.addAction(Actions.sequence(
            Actions.delay(dialogue.length * 2f + 2f), //total duration of dialogue
            Actions.run(() -> {
                Gdx.app.log("CutsceneScreen", "Cutscene ended. Removing input listener.");
                disposeInput(); //remove the input listener when cutscene ends
                onComplete.run(); //callback to transition
            })
        ));
    }

    private void setupSkipButton(Runnable onComplete) {
        skipButton = new TextButton("Skip", skin);
        skipButton.setSize(200, 80); //set button size

        //dynamically position button in bottom-right corner
        skipButton.setPosition(
            viewport.getWorldWidth() - skipButton.getWidth() - 20, //right margin
            20 //bottom margin
        );

        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.log("CutsceneScreen", "Skip button clicked."); //debug log
                disposeInput(); //remove input listener on skip
                stage.clear(); //clear all ongoing actions and actors
                onComplete.run(); //immediately transition to the next screen
            }
        });

        stage.addActor(skipButton);
        Gdx.app.log("CutsceneScreen", "Skip button added to stage.");
    }

    private void disposeInput() {
        //clear the input listener
        if (Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
            multiplexer.removeProcessor(cutsceneInputAdapter);
        }
        Gdx.app.log("CutsceneScreen", "Input listener disposed.");
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);

        if (buttonAdded && skipButton != null) {
            //reposition the skip button after resizing
            skipButton.setPosition(
                viewport.getWorldWidth() - skipButton.getWidth() - 20,
                20
            );
        }
    }

    public void dispose() {
        disposeInput(); //ensure the input listener is removed
        stage.dispose();
        skin.dispose();
    }
}
