package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.EmbersTrial.player.Player;
import com.EmbersTrial.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.EmbersTrial.Enemy.Goblin;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.List;

public class GameScreen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Music combatMusic;




    //TESTING GOBLIN
    private List<Goblin> goblins;


    private com.badlogic.gdx.graphics.Cursor customCursor;
    // zoom-related variables
    private float maxZoom = 3f; // maximum zoom out
    private float minZoom = 0.5f; // maximum zoom in
    private float zoomStep = 0.1f; // how much zoom changes per scroll

    // pause-related variables
    private boolean isPaused = false;
    private Stage pauseStage;
    private Skin skin;
    private float pauseCooldown = 0.2f; // debounce time for ESC key
    private float pauseCooldownTimer = 0f;

    // player-related variables
    private Player player;
    private Main mainApp;

    // tilemap
    private TiledMap tiledmap;
    private OrthogonalTiledMapRenderer orthoRenderer;

    // GameUI instance
    private GameUI gameUI;

    // InputMultiplexer to handle multiple input processors
    private InputMultiplexer inputMultiplexer;

    public GameScreen(Main mainApp) {
        this.mainApp = mainApp;

        // camera and viewport setup
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);

        // spritebatch setup
        batch = new SpriteBatch();

        // initialize tile map
        tiledmap = new TmxMapLoader().load("untitled.tmx");
        orthoRenderer = new OrthogonalTiledMapRenderer(tiledmap, 4f);

        float mapWidth = tiledmap.getProperties().get("width", Integer.class) * 160; //40 * x (the unit scale from orthoRenderer)
        System.out.print(mapWidth);

        float mapHeight = tiledmap.getProperties().get("height", Integer.class) * 120; //30 * y (the unit scale from orthoRenderer)
        System.out.print(mapHeight);
        camera.position.set(mapWidth / 2, mapHeight / 2, 0);
        float mapCenterX = mapWidth / 2;
        float mapCenterY = mapHeight / 2;

        // initialize player
        player = new Player(new Texture(Gdx.files.internal("Entities/player/player_down_up_idle.png")), (TiledMapTileLayer) tiledmap.getLayers().get(0));
        player.getPosition().set(mapCenterX - 50, mapCenterY - 400);


        //TEST INITIALIZE GOBLIN
        goblins = new ArrayList<>();
        spawnGoblins(5); // Spawn 5 goblins as an example

        // initialize pause menu
        initPauseMenu();

        // initialize UI
        gameUI = new GameUI(player);

        // Initialize InputMultiplexer
        inputMultiplexer = new InputMultiplexer();

        // Add zoom input handling
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY > 0 ? zoomStep : -zoomStep;
                camera.zoom = Math.max(minZoom, Math.min(maxZoom, camera.zoom));
                camera.update();
                return true;
            }
        });

        // Add the pause stage to the InputMultiplexer
        inputMultiplexer.addProcessor(pauseStage);

        // Set the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);

        initializeCustomCursor();
        // Load and configure background music
        combatMusic = Gdx.audio.newMusic(Gdx.files.internal("combat theme.wav"));
        combatMusic.setLooping(true);
        combatMusic.setVolume(mainApp.getPreferences().getFloat("volume", 0.2f));
        combatMusic.play();

    }

    //TEST METHOD GOBLIN
    private void spawnGoblins(int count) {
        for (int i = 0; i < count; i++) {
            // Spawn goblins at random positions on the map
            float spawnX = (float) Math.random() * tiledmap.getProperties().get("width", Integer.class) * 160;
            float spawnY = (float) Math.random() * tiledmap.getProperties().get("height", Integer.class) * 120;
            goblins.add(new Goblin(new Texture(Gdx.files.internal("Entities/player/player_down_up_idle.png")), new Vector2(spawnX, spawnY)));
        }
    }

    private void initializeCustomCursor() {
        // Load the custom cursor image as a Pixmap
        Pixmap originalPixmap = new Pixmap(Gdx.files.internal("crosshair.png"));

        // Scale factor (e.g., 0.5 for half size)
        float scaleFactor = 0.5f; // Change this to adjust the size

        // Create a scaled Pixmap
        int newWidth = (int) (originalPixmap.getWidth() * scaleFactor);
        int newHeight = (int) (originalPixmap.getHeight() * scaleFactor);
        Pixmap scaledPixmap = new Pixmap(newWidth, newHeight, originalPixmap.getFormat());

        // Draw the original Pixmap into the scaled Pixmap
        scaledPixmap.drawPixmap(originalPixmap,
            0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(), // Source dimensions
            0, 0, newWidth, newHeight // Target dimensions
        );

        // Create the custom cursor with the scaled Pixmap
        customCursor = Gdx.graphics.newCursor(scaledPixmap, newWidth / 2, newHeight / 2);

        // Set the custom cursor
        Gdx.graphics.setCursor(customCursor);

        // Dispose the original and scaled Pixmaps after use
        originalPixmap.dispose();
        scaledPixmap.dispose();
    }



    private void initPauseMenu() {
        pauseStage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        // resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(event -> {
            if (resumeButton.isPressed()) {
                togglePause(false);
            }
            return true;
        });

        // exit to main menu button
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                mainApp.setMainMenuScreen();
                dispose();
            }
            return true;
        });

        pauseTable.add(resumeButton).width(200).height(50).padBottom(10);
        pauseTable.row();
        pauseTable.add(exitButton).width(200).height(50);
    }

    public void render() {
        // prevent rendering if disposed
        if (batch == null) return;

        // update the pause cooldown timer
        if (pauseCooldownTimer > 0) {
            pauseCooldownTimer -= Gdx.graphics.getDeltaTime();
        }

        // handle pause toggle
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) && pauseCooldownTimer <= 0) {
            togglePause(!isPaused);
            pauseCooldownTimer = pauseCooldown;
        }

        // clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        orthoRenderer.setView(camera);
        orthoRenderer.render();

        camera.position.set(player.getPosition().x + 2 * (player.getPlayerWidth() / 2), player.getPosition().y + 2 * (player.getPlayerHeight() / 2), 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        if (!isPaused) {
            // update game logic
            player.update(Gdx.graphics.getDeltaTime());

            //TEST GOBLIN
            for (Goblin goblin : goblins) {
                goblin.update(Gdx.graphics.getDeltaTime(), player.getPosition());
            }
        }

        batch.begin();

        // render the player
        player.render(batch);

        // Render goblins
        for (Goblin goblin : goblins) {
            goblin.render(batch);
        }

        batch.end();

        // render pause overlay and menu if paused
        if (isPaused) {
            renderPauseOverlay();
            pauseStage.act();
            pauseStage.draw();
        }

        // render the UI overlay
        gameUI.render(Gdx.graphics.getDeltaTime());
    }

    private void renderPauseOverlay() {
        // render a transparent black rectangle over the entire screen
        batch.begin();
        batch.setColor(0, 0, 0, 0.5f);
        batch.end();
        batch.setColor(Color.WHITE);
    }

    private void togglePause(boolean pause) {
        isPaused = pause;
        if (isPaused) {
            inputMultiplexer.addProcessor(pauseStage); // add pause stage input
        } else {
            inputMultiplexer.removeProcessor(pauseStage); // remove pause stage input
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        gameUI.resize(width, height);
    }

    public void dispose() {
        if (batch != null) batch.dispose();
        if (tiledmap != null) tiledmap.dispose();
        if (player != null) player.dispose();
        if (pauseStage != null) pauseStage.dispose();
        if (skin != null) skin.dispose();
        if (customCursor != null) {
            customCursor.dispose();
        }

        gameUI.dispose();

        // Dispose goblins
        for (Goblin goblin : goblins) {
            goblin.dispose();
        }
    }
}
