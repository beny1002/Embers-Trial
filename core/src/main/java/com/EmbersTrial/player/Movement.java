package com.EmbersTrial.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Movement {
    private Vector2 position;
    private Vector2 direction;
    private float speed;

    private boolean isDashing = false; // start with no active boost
    private float boostDuration = 0.2f; // boost lasts for 0.2 seconds
    private float boostTimer = 0f; // tracks remaining boost time
    private float boostMultiplier = 3.0f; // 3x speed during boost
    private float boostCooldown = 0f; // tracks cooldown time
    private float boostCooldownMax = 3f; // maximum cooldown time (3 seconds)

    public Movement() {
        position = new Vector2(600, 400); // starting position
        direction = new Vector2(0, 0);
        speed = 200f; // movement speed in pixels per second
    }

    public void update(float deltaTime) {
        direction.set(0, 0);

        // reduce cooldown timer if active
        if (boostCooldown > 0) {
            boostCooldown -= deltaTime;
            if (boostCooldown < 0) {
                boostCooldown = 0; // reset cooldown when it reaches 0
            }
        }

        // handle boost activation
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isDashing && boostCooldown == 0) {
            isDashing = true; // start boost
            boostTimer = boostDuration; // set boost duration
            boostCooldown = boostCooldownMax; // start cooldown timer
            Gdx.app.log("Movement", "Boost activated! Cooldown started.");
        }

        // handle boost duration
        if (isDashing) {
            boostTimer -= deltaTime;
            if (boostTimer <= 0) {
                isDashing = false; // end boost
                Gdx.app.log("Movement", "Boost ended.");
            }
        }

        // handle movement input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1;

        // calculate current speed
        float currentSpeed = isDashing ? speed * boostMultiplier : speed;

        // normalize direction and update position
        direction.nor();
        position.add(direction.scl(currentSpeed * deltaTime));
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
