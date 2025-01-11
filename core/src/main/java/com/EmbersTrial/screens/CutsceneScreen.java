package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CutsceneScreen {
    private Stage stage;
    private Skin skin;
    private FitViewport viewport;

    public CutsceneScreen(Runnable onComplete) {
        //set up stage and viewport
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        setupCutscene(onComplete);
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

        // Transition to GameScreen after dialogue ends
        stage.addAction(Actions.sequence(
            Actions.delay(dialogue.length * 2f + 2f), //total duration of dialogue
            Actions.run(onComplete) //callback to transition
        ));
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
