package com.EmbersTrial.screens;

import com.EmbersTrial.Main;
import com.EmbersTrial.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;

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
    private TileMap tileMap;
    private TiledMap tiledmap;
    private OrthogonalTiledMapRenderer orthoRenderer;

    public GameScreen(Main mainApp) {
        this.mainApp = mainApp;

        // camera and viewport setup
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera); // initial viewport
        //use this camera to see whole screen
        //camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        //camera.update();

        // spritebatch setup
        batch = new SpriteBatch();

        // initialize tile map
        //tileMap = new TileMap("untitled.png");
        tiledmap = new TmxMapLoader().load("untitled.tmx");
        orthoRenderer = new OrthogonalTiledMapRenderer(tiledmap, 3f);
        //tileMap.generateSampleMap(); // generate a sample map
        //ye

        // initialize player
        player = new Player(new Texture(Gdx.files.internal("player_stand_front.png")));

        // initialize pause menu
        initPauseMenu();

        // set input processor for scroll zoom
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                // zoom in or out based on scroll direction
                camera.zoom += amountY > 0 ? zoomStep : -zoomStep;
                camera.zoom = Math.max(minZoom, Math.min(maxZoom, camera.zoom)); // clamp zoom
                camera.update();
                return true; // event handled
            }
        });
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
                togglePause(false); // unpause the game
            }
            return true;
        });

        // exit to main menu button
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);

        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                mainApp.setMainMenuScreen();
                dispose(); // dispose resources after transitioning
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
            pauseCooldownTimer = pauseCooldown; // reset cooldown timer
        }

        // clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        orthoRenderer.setView(camera);
        orthoRenderer.render();

        camera.position.set(player.getPosition().x + 2 * (player.getPlayerWidth() / 2), player.getPosition().y + 2 * (player.getPlayerHeight() / 2), 0);
        camera.update();
        //System.out.println(player.getPosition().x + " " + player.getPosition().y);
        //System.out.println(player.getPosition().y + " "+ player.getPlayerHeight());
        //System.out.println((player.getPosition().x + player.getPlayerWidth() / 2) + " " + (player.getPosition().y + player.getPlayerHeight() / 2));
        batch.setProjectionMatrix(camera.combined);


        if (!isPaused) {
            // update game logic
            player.update(Gdx.graphics.getDeltaTime());
        }

        batch.begin();
        // render the tile map
        //tileMap.render(batch, camera);


        // render the player
        player.render(batch);
        batch.end();

        // render pause overlay and menu if paused
        if (isPaused) {
            renderPauseOverlay();
            pauseStage.act();
            pauseStage.draw();
        }
    }

    private void renderPauseOverlay() {
        // render a transparent black rectangle over the entire screen
        batch.begin();
        batch.setColor(0, 0, 0, 0.5f); // semi-transparent black
        batch.draw(tileMap.getBlankTile(), 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
        batch.setColor(Color.WHITE); // reset color to default
    }

    private void togglePause(boolean pause) {
        isPaused = pause;
        if (isPaused) {
            Gdx.input.setInputProcessor(pauseStage); // set input to pause menu
        } else {
            Gdx.input.setInputProcessor(null); // allow player control
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        if (batch != null) batch.dispose();
        if (tileMap != null) tileMap.dispose();
        if (player != null) player.dispose();
        if (pauseStage != null) pauseStage.dispose();
        if (skin != null) skin.dispose();
    }
}
