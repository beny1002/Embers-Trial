package com.EmbersTrial.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Player {
    private PlayerRenderer renderer;
    private Vector2 position;
    private Vector2 direction;
    private float speed;
    private Texture texture;
    private float animationTimer = 0f;
    private float animationSpeed = 0.1f;
    private int currentFrameIndex = 0;
    private Texture[] walkingUpFrames, walkingDownFrames, walkingLeftFrames, walkingRightFrames;
    private Texture standingBack, standingFront, standingLeft, standingRight;
    private Vector2 lastDirection = new Vector2(0, 0);

    private boolean isBoosting = false;
    private float boostDuration = 0.5f; // boost lasts for 0.5 seconds
    private float boostTimer = 0f;
    private float boostCooldown = 0f; // cooldown time left
    private float boostCooldownMax = 3f; // cooldown duration
    private float boostMultiplier = 3f; // 3x speed during boost

    // cooldown message variables
    private String cooldownMessage = "";
    private float messageTimer = 0f;
    private final float messageDisplayTime = 1.5f; // time to display the message
    private float messageAlpha = 0f; // alpha for fading
    private BitmapFont font;
    private GlyphLayout layout;

    private TiledMapTileLayer collisionLayer;

    public Player(Texture playerTexture, TiledMapTileLayer  collisionLayer) {
        this.texture = playerTexture;
        this.collisionLayer = collisionLayer;

        renderer = new PlayerRenderer(texture);
        position = new Vector2(100, 100);
        direction = new Vector2(0, 0);
        speed = 300f;

        font = new BitmapFont(); // default font for rendering text
        font.setColor(Color.RED); // set the font color to red
        layout = new GlyphLayout(); // used to calculate text width/height
        setAnimations();

    }

    public int getPlayerWidth(){
        return standingBack.getWidth();
    }

    public int getPlayerHeight(){
        return standingBack.getHeight();
    }

    public void setAnimations() {
        standingBack = new Texture(Gdx.files.internal("player_stand_back.png"));
        standingFront = new Texture(Gdx.files.internal("player_stand_front.png"));
        standingLeft = new Texture(Gdx.files.internal("player_stand_left.png"));
        standingRight = new Texture(Gdx.files.internal("player_stand_right.png"));

        walkingUpFrames = new Texture[]{
            //added two walking to make the animation slower
            new Texture(Gdx.files.internal("player_leftLegWalk_back.png")),
            new Texture(Gdx.files.internal("player_leftLegWalk_back.png")),
            standingBack,
            new Texture(Gdx.files.internal("player_rightLegWalk_back.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_back.png")),
            standingBack,
        };
        walkingDownFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_front.png")),
            new Texture(Gdx.files.internal("player_leftLegWalk_front.png")),
            standingFront,
            new Texture(Gdx.files.internal("player_rightLegWalk_front.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_front.png")),
            standingFront,
        };
        walkingLeftFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceLeft.png")),
            new Texture(Gdx.files.internal("player_leftLegWalk_faceLeft.png")),
            standingLeft,
            new Texture(Gdx.files.internal("player_rightLegWalk_faceLeft.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_faceLeft.png")),
            standingLeft,
        };
        walkingRightFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceRight.png")),
            new Texture(Gdx.files.internal("player_leftLegWalk_faceRight.png")),
            standingRight,
            new Texture(Gdx.files.internal("player_rightLegWalk_faceRight.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_faceRight.png")),
            standingRight,
        };
    }

    private void updateAnimation(Texture[] frames, float deltaTime, boolean immediateStep) {
        if (immediateStep) {
            currentFrameIndex = (currentFrameIndex + 1) % frames.length;
            renderer.setTexture(frames[currentFrameIndex]);
            animationTimer = 0f;
        } else {
            animationTimer += deltaTime;
            if (animationTimer >= animationSpeed) {
                animationTimer = 0f;
                currentFrameIndex = (currentFrameIndex + 1) % frames.length;
                renderer.setTexture(frames[currentFrameIndex]);
            }
        }
    }

    private float stepCooldownTimer = 0.15f; // min 150ms between steps
    private float stepTimer = 0f;

    public void update(float deltaTime) {
        stepTimer += deltaTime;
        direction.set(0, 0);
        boolean isMoving = false;

        // cooldown logic
        if (boostCooldown > 0) {
            boostCooldown -= deltaTime;
            if (boostCooldown < 0) boostCooldown = 0;
        }

        // message fade logic
        if (messageTimer > 0) {
            messageTimer -= deltaTime;
            if (messageTimer > messageDisplayTime / 2) {
                messageAlpha = (messageDisplayTime - messageTimer) / (messageDisplayTime / 2); // fade in
            } else {
                messageAlpha = messageTimer / (messageDisplayTime / 2); // fade out
            }
        } else {
            cooldownMessage = "";
            messageAlpha = 0f;
        }

        // boost logic
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!isBoosting && boostCooldown == 0) {
                isBoosting = true;
                boostTimer = boostDuration;
                boostCooldown = boostCooldownMax;
            } else if (boostCooldown > 0) {
                cooldownMessage = String.format("Boost on cooldown for %.1f seconds", boostCooldown);
                messageTimer = messageDisplayTime;
                messageAlpha = 0f;
            }
        }

        if (isBoosting) {
            boostTimer -= deltaTime;
            if (boostTimer <= 0) isBoosting = false;
        }

        // movement logic
        handleMovement(deltaTime, isMoving);
    }

    private void handleMovement(float deltaTime, boolean isMoving) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && !(Gdx.input.isKeyPressed(Input.Keys.D)) && !(Gdx.input.isKeyPressed(Input.Keys.A))) {
            direction.y += 1;
            lastDirection.set(0, 1);
            isMoving = true;
            handleAnimation(walkingUpFrames, deltaTime);
        }  else if (Gdx.input.isKeyPressed(Input.Keys.S) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.D))) {
            direction.y -= 1;
            lastDirection.set(0, -1);
            isMoving = true;
            handleAnimation(walkingDownFrames, deltaTime);
        }  else if (Gdx.input.isKeyPressed(Input.Keys.A) && !(Gdx.input.isKeyPressed(Input.Keys.S)) && !(Gdx.input.isKeyPressed(Input.Keys.W))) {
            direction.x -= 1;
            lastDirection.set(-1, 0);
            isMoving = true;
            handleAnimation(walkingLeftFrames, deltaTime);
        }  else if (Gdx.input.isKeyPressed(Input.Keys.D) && !(Gdx.input.isKeyPressed(Input.Keys.S)) && !(Gdx.input.isKeyPressed(Input.Keys.W))) {
            direction.x += 1;
            lastDirection.set(1, 0);
            isMoving = true;
            handleAnimation(walkingRightFrames, deltaTime);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;
            direction.x += 1;
            lastDirection.set(1, -1);
            isMoving = true;
            handleAnimation(walkingRightFrames, deltaTime);
        }   else if(Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)){
            direction.x  += 1;
            direction.y += 1;
            isMoving = true;
            lastDirection.set(1, 1);
            handleAnimation(walkingRightFrames, deltaTime);
        }   else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)){
            direction.x -= 1;
            direction.y += 1;
            lastDirection.set(-1, 1);
            isMoving = true;
            handleAnimation(walkingLeftFrames, deltaTime);
        }   else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)){
            direction.x -= 1;
            direction.y -= 1;
            lastDirection.set(-1, -1);
            isMoving = true;
            handleAnimation(walkingLeftFrames, deltaTime);
        }

        if (!isMoving) resetToIdleState();
        movePlayer(deltaTime);
    }

    private void handleAnimation(Texture[] frames, float deltaTime) {
        if (stepTimer >= stepCooldownTimer) {
            updateAnimation(frames, deltaTime, true);
            stepTimer = 0f;
        } else {
            updateAnimation(frames, deltaTime, false);
        }
    }

    private void resetToIdleState() {
        currentFrameIndex = 0;
        if (lastDirection.y > 0) renderer.setTexture(standingBack);
        else if (lastDirection.y < 0) renderer.setTexture(standingFront);
        else if (lastDirection.x < 0) renderer.setTexture(standingLeft);
        else if (lastDirection.x > 0) renderer.setTexture(standingRight);
    }

    private void movePlayer(float deltaTime) {
        float currentSpeed = isBoosting ? speed * boostMultiplier : speed;
        direction.nor();
        //position.add(direction.scl(currentSpeed * deltaTime));

        Vector2 newPos = new Vector2(position.x, position.y).add(direction.scl(currentSpeed * deltaTime));

        // Check for collisions before moving
        if (!isColliding(newPos)) {
            position.set(newPos);
        }

    }

    private boolean isColliding(Vector2 newPos) {
        float tileWidth = collisionLayer.getTileWidth() * 4f - (2f + ((float) 1/2)); //by 4 because of scaling
        float tileHeight = collisionLayer.getTileHeight() * 4f; // multiply by 4 because of scaling

        int tileX = (int) (newPos.x / tileWidth);
        int tileY = (int) (newPos.y / tileHeight);

        if (tileX >= 0 && tileX < collisionLayer.getWidth() && tileY >= 0 && tileY < collisionLayer.getHeight()) {
            TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
            if (cell != null && cell.getTile().getProperties().containsKey("blocked")) {
                System.out.print("colliding");
                return true;
            }
        }
        return false;

    }

    public void render(SpriteBatch batch) {
        renderer.render(batch, position);

        // render cooldown message
        if (!cooldownMessage.isEmpty() && messageAlpha > 0) {
            layout.setText(font, cooldownMessage);
            float textWidth = layout.width;
            float screenWidth = Gdx.graphics.getWidth();
            font.setColor(1, 0, 0, messageAlpha); // red color with alpha
            font.draw(batch, cooldownMessage, (screenWidth - textWidth) / 2, 50); // bottom of screen
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        renderer.dispose();
        font.dispose();
        for (Texture frame : walkingUpFrames) frame.dispose();
        for (Texture frame : walkingDownFrames) frame.dispose();
        for (Texture frame : walkingLeftFrames) frame.dispose();
        for (Texture frame : walkingRightFrames) frame.dispose();

        standingBack.dispose();
        standingFront.dispose();
        standingLeft.dispose();
        standingRight.dispose();
    }
}
