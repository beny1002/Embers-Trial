package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;

public class CutsceneScreen {
    private Stage stage;
    private Skin skin;
    private FitViewport viewport;
    private TextButton skipButton;
    private boolean buttonAdded = false;
    private MediaPlayer mediaPlayer;
    private JFrame videoFrame;

    public CutsceneScreen(Runnable onComplete) {
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        setupVideo(onComplete);
        setupSkipButton(onComplete);

        fadeInEffect();
    }

    private void setupVideo(Runnable onComplete) {
        // Initialize JavaFX
        Platform.startup(() -> {});

        // Create a JFrame to host the JavaFX content
        videoFrame = new JFrame("Cutscene");
        videoFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        videoFrame.setUndecorated(true);
        videoFrame.setBackground(new Color(0, 0, 0, 0));
        videoFrame.setSize(1920, 1080);
        videoFrame.setLocationRelativeTo(null);

        // Embed a JFXPanel into the JFrame
        JFXPanel jfxPanel = new JFXPanel();
        videoFrame.add(jfxPanel);

        // Run JavaFX code on the JavaFX application thread
        Platform.runLater(() -> {
            // Load and play the video
            String videoPath = Gdx.files.internal("path/to/your/video.mp4").file().toURI().toString();
            Media media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Create a JavaFX scene with the MediaView
            StackPane root = new StackPane(mediaView);
            Scene scene = new Scene(root, 1920, 1080, javafx.scene.paint.Color.BLACK);
            jfxPanel.setScene(scene);

            // Start the video
            mediaPlayer.setOnEndOfMedia(() -> {
                disposeInput();
                videoFrame.dispose();
                Platform.exit();
                onComplete.run();
            });
            mediaPlayer.play();
        });

        videoFrame.setVisible(true);
    }

    private void setupSkipButton(Runnable onComplete) {
        skipButton = new TextButton("Skip", skin);
        skipButton.setSize(200, 80);
        skipButton.setPosition(viewport.getWorldWidth() - skipButton.getWidth() - 20, 20);

        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
                disposeInput();
                videoFrame.dispose();
                Platform.exit();
                onComplete.run();
            }
        });

        stage.addActor(skipButton);
    }

    private void fadeInEffect() {
        stage.addAction(Actions.sequence(
            Actions.fadeOut(0f), // Start fully black
            Actions.fadeIn(1f)   // Fade in over 1 second
        ));
    }

    private void disposeInput() {
        Gdx.input.setInputProcessor(null);
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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        if (videoFrame != null) {
            videoFrame.dispose();
        }
        stage.dispose();
        skin.dispose();
    }
}
