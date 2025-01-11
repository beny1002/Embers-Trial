package com.EmbersTrial.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Movement {
    private Vector2 position;
    private Vector2 direction;
    private float speed;

    public Movement() {
        position = new Vector2(600, 400); //starting position
        direction = new Vector2(0, 0);
        speed = 200f; //movement speed in pixels per second
    }

    public void update(float deltaTime) {
        direction.set(0, 0);

        //handle input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1;

        direction.nor(); //normalize direction vector
        position.add(direction.scl(speed * deltaTime)); // Update position
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
