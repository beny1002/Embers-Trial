package com.EmbersTrial.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Particle {
    private Vector2 position;
    private Vector2 velocity;
    private float lifespan;
    private Color color;
    private Texture texture; // A small dot or pixel texture

    public Particle(Vector2 position, Vector2 velocity, float lifespan, Color color, Texture texture) {
        this.position = new Vector2(position);
        this.velocity = new Vector2(velocity);
        this.lifespan = lifespan;
        this.color = color;
        this.texture = texture;
    }

    public boolean isAlive() {
        return lifespan > 0;
    }

    public void update(float deltaTime) {
        position.add(velocity.cpy().scl(deltaTime)); // Update position based on velocity
        lifespan -= deltaTime; // Reduce lifespan
    }

    public void render(SpriteBatch batch) {
        if (isAlive()) {
            batch.setColor(color.r, color.g, color.b, lifespan); // Fade out as lifespan decreases
            batch.draw(texture, position.x, position.y, 4, 4); // Draw a 4x4 pixel
            batch.setColor(Color.WHITE); // Reset color
        }
    }
}
