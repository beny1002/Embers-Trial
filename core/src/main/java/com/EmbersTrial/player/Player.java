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

    public Player(Texture playerTexture) {
        renderer = new PlayerRenderer(playerTexture); //use the single texture
        position = new Vector2(100, 100); //starting position
        direction = new Vector2(0, 0);    //initial direction
        speed = 300f;                    //movement speed (pixels per second)
    }

    public void update(float deltaTime) {
        //reset direction
        direction.set(0, 0);

        // Handle input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1; // Move up
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1; // Move down
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1; // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1; // Move right

        direction.nor();

        position.add(direction.scl(speed * deltaTime));
    }

    public void render(SpriteBatch batch) {
        renderer.render(batch, position); //render the player using the single texture
    }

    public void dispose() {
        renderer.dispose(); //dispose of the texture
    }
}
