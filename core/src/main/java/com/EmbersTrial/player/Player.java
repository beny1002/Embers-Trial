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
    private Texture[] walkingUpFrames;
    private Texture[] walkingDownFrames;
    private Texture[] walkingLeftFrames;
    private Texture[] walkingRightFrames;

    public Player(Texture playerTexture) {
        this.texture = playerTexture;
        renderer = new PlayerRenderer(texture);
        position = new Vector2(100, 100);
        direction = new Vector2(0, 0);
        speed = 300f;
        setAnimations();
    }

    public void setAnimations() {
        walkingUpFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_back.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_back.png"))
        };
        walkingDownFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_front.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_front.png"))
        };
        walkingLeftFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceLeft.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_faceLeft.png"))
        };
        walkingRightFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceRight.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_faceRight.png"))
        };
    }

    private void updateAnimation(Texture[] frames, float deltaTime) {
        animationTimer += deltaTime;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0f;
            currentFrameIndex = (currentFrameIndex + 1) % frames.length;
            renderer.setTexture(frames[currentFrameIndex]);
        }
    }

    public void update(float deltaTime) {

        direction.set(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1; // Move up
            updateAnimation(walkingUpFrames, deltaTime);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1; // Move down
            updateAnimation(walkingDownFrames, deltaTime);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1; // Move left
            updateAnimation(walkingLeftFrames, deltaTime);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1; // Move right
            updateAnimation(walkingRightFrames, deltaTime);
        } else {
            if (direction.y > 0) {
                renderer.setTexture(new Texture(Gdx.files.internal("player_stand_back.png")));
            } else if (direction.y < 0) {
                renderer.setTexture(new Texture(Gdx.files.internal("player_stand_front.png")));
            } else if (direction.x < 0) {
                renderer.setTexture(new Texture(Gdx.files.internal("player_stand_left.png")));
            } else if (direction.x > 0) {
                renderer.setTexture(new Texture(Gdx.files.internal("player_stand_right.png")));
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
    }
}
