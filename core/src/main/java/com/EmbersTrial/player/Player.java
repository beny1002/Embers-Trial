package com.EmbersTrial.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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


    public Player(Texture playerTexture) {
        this.texture = playerTexture;
        renderer = new PlayerRenderer(texture);
        position = new Vector2(100, 100);
        direction = new Vector2(0, 0);
        speed = 300f;
        setAnimations();
    }

    public void setAnimations() {
        standingBack = new Texture(Gdx.files.internal("player_stand_back.png"));
        standingFront = new Texture(Gdx.files.internal("player_stand_front.png"));
        standingLeft = new Texture(Gdx.files.internal("player_stand_left.png"));
        standingRight = new Texture(Gdx.files.internal("player_stand_right.png"));

        walkingUpFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_back.png")),
            standingBack,
            new Texture(Gdx.files.internal("player_rightLegWalk_back.png")),
            standingBack,
        };
        walkingDownFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_front.png")),
            standingFront,
            new Texture(Gdx.files.internal("player_rightLegWalk_front.png")),
            standingFront,
        };
        walkingLeftFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceLeft.png")),
            standingLeft,
            new Texture(Gdx.files.internal("player_rightLegWalk_faceLeft.png")),
            standingLeft,
        };
        walkingRightFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceRight.png")),
            standingRight,
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


    private float stepCooldownTimer = 0.15f; //min 150ms between steps
    private float stepTimer = 0f;

    public void update(float deltaTime) {
        stepTimer += deltaTime; //increment step timer
        direction.set(0, 0);
        boolean isMoving = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;
            lastDirection.set(0, 1);
            isMoving = true;

            //trigger animation update
            if (stepTimer >= stepCooldownTimer) {
                updateAnimation(walkingUpFrames, deltaTime, true);
                stepTimer = 0f; //reset cooldown
            } else {
                updateAnimation(walkingUpFrames, deltaTime, false);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;
            lastDirection.set(0, -1);
            isMoving = true;

            if (stepTimer >= stepCooldownTimer) {
                updateAnimation(walkingDownFrames, deltaTime, true);
                stepTimer = 0f;
            } else {
                updateAnimation(walkingDownFrames, deltaTime, false);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;
            lastDirection.set(-1, 0);
            isMoving = true;

            if (stepTimer >= stepCooldownTimer) {
                updateAnimation(walkingLeftFrames, deltaTime, true);
                stepTimer = 0f;
            } else {
                updateAnimation(walkingLeftFrames, deltaTime, false);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;
            lastDirection.set(1, 0);
            isMoving = true;

            if (stepTimer >= stepCooldownTimer) {
                updateAnimation(walkingRightFrames, deltaTime, true);
                stepTimer = 0f;
            } else {
                updateAnimation(walkingRightFrames, deltaTime, false);
            }
        }

        if (!isMoving) {
            currentFrameIndex = 0;
            if (lastDirection.y > 0) {
                renderer.setTexture(standingBack);
            } else if (lastDirection.y < 0) {
                renderer.setTexture(standingFront);
            } else if (lastDirection.x < 0) {
                renderer.setTexture(standingLeft);
            } else if (lastDirection.x > 0) {
                renderer.setTexture(standingRight);
            }
        }

        direction.nor();
        position.add(direction.scl(speed * deltaTime));
    }





    public void render(SpriteBatch batch) {
        renderer.render(batch, position);
    }

    public void dispose() {
        renderer.dispose();
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
