package com.EmbersTrial;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CutsceneScreen {
    private Stage stage;
    private Skin skin;


    public CutsceneScreen(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public void showCutscene(Runnable onComplete) {
        stage.clear();

        Table dialogueTable = new Table();
        dialogueTable.setFillParent(true);
        stage.addActor(dialogueTable);

        String[] dialogue = {
            "This is a test dialogue",
            "This is a work in progress",
            "This game is called Ember's Trial"
        };

        for (int i = 0; i < dialogue.length; i++) {
            String line = dialogue[i];
            Label dialogueLabel = new Label(line, skin);
            dialogueLabel.setFontScale(1.5f);
            dialogueLabel.setColor(1, 1, 1, 0);

            dialogueTable.row();
            dialogueTable.add(dialogueLabel).pad(10);
            dialogueLabel.addAction(Actions.sequence(
                Actions.delay(i * 2f),
                Actions.fadeIn(1f)
            ));
        }

        stage.addAction(Actions.sequence(
            Actions.delay(dialogue.length * 2f + 2f),
            Actions.run(onComplete)
        ));
    }

}
