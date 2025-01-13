package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsPage {

    private Stage stage;
    private Skin skin;
    private Table mainMenu;
    private Music mainMenuMusic; // music reference for dynamic updates

    //preferences keys
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOLUME = "sound.volume";
    private static final String PREFS_NAME = "EmberTrials";

    public OptionsPage(Stage stage, Skin skin, Table mainMenu, Music mainMenuMusic) {
        this.stage = stage;
        this.skin = skin;
        this.mainMenu = mainMenu;
        this.mainMenuMusic = mainMenuMusic;
    }

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isSoundEffectsEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME, 0.5f);
    }

    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
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

        //music volume slider
        Label musicVolumeLabel = new Label("Music Volume", skin);
        final Slider musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicVolumeSlider.setValue(getMusicVolume());
        musicVolumeSlider.addListener(event -> {
            float newVolume = musicVolumeSlider.getValue();
            setMusicVolume(newVolume);
            mainMenuMusic.setVolume(newVolume); // dynamically update the music volume
            return false;
        });

        optionsTable.add(musicVolumeLabel).left().pad(10);
        optionsTable.row();
        optionsTable.add(musicVolumeSlider).width(300).padBottom(20);
        optionsTable.row();

        //sound effects volume slider
        Label soundVolumeLabel = new Label("Sound Volume", skin);
        final Slider soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundVolumeSlider.setValue(getSoundVolume());
        soundVolumeSlider.addListener(event -> {
            setSoundVolume(soundVolumeSlider.getValue());
            return false;
        });

        optionsTable.add(soundVolumeLabel).left().pad(10);
        optionsTable.row();
        optionsTable.add(soundVolumeSlider).width(300).padBottom(20);
        optionsTable.row();

        //toggle music button
        Label musicToggleLabel = new Label("Music Enabled", skin);
        final CheckBox musicToggle = new CheckBox("", skin);
        musicToggle.setChecked(isMusicEnabled());
        musicToggle.addListener(event -> {
            setMusicEnabled(musicToggle.isChecked());
            mainMenuMusic.setVolume(isMusicEnabled() ? getMusicVolume() : 0f); // mute or unmute
            return false;
        });

        optionsTable.add(musicToggleLabel).left().pad(10);
        optionsTable.row();
        optionsTable.add(musicToggle).padBottom(20);
        optionsTable.row();

        //toggle sound effects button
        Label soundToggleLabel = new Label("Sound Effects Enabled", skin);
        final CheckBox soundToggle = new CheckBox("", skin);
        soundToggle.setChecked(isSoundEffectsEnabled());
        soundToggle.addListener(event -> {
            setSoundEffectsEnabled(soundToggle.isChecked());
            return false;
        });

        optionsTable.add(soundToggleLabel).left().pad(10);
        optionsTable.row();
        optionsTable.add(soundToggle).padBottom(20);
        optionsTable.row();

        //back button
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
