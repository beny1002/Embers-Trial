package com.EmbersTrial.Enemy;

import com.EmbersTrial.Enemy.GoblinRenderer;
import com.EmbersTrial.player.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Goblin {
    private GoblinRenderer renderer;
    private Vector2 position;
    private Vector2 direction;
    private float speed;
    private Texture texture;
    private boolean isMoving = false;

    // Goblin stats
    private int health;

    // Animations
    private Texture[] walkingUpFrames, walkingDownFrames, walkingLeftFrames, walkingRightFrames;

    public Goblin(Texture goblinTexture, Vector2 spawnPosition) {
        this.texture = goblinTexture;
        this.position = new Vector2(spawnPosition);
        this.direction = new Vector2(0, 0);
        this.speed = 150f; // Adjust speed as necessary
        this.health = 50; // Goblin health

        renderer = new GoblinRenderer(texture);
        setAnimations();
    }

    public void setAnimations() {
        // Use player animations for now
        walkingUpFrames = new Texture[] {
            new Texture("Entities/player/player_up_up_walk1.png"),
            new Texture("Entities/player/player_up_up_walk2.png"),
        };
        walkingDownFrames = new Texture[] {
            new Texture("Entities/player/player_down_up_walk1.png"),
            new Texture("Entities/player/player_down_up_walk2.png"),
        };
        walkingLeftFrames = new Texture[] {
            new Texture("Entities/player/player_left_up_walk1.png"),
            new Texture("Entities/player/player_left_up_walk2.png"),
        };
        walkingRightFrames = new Texture[] {
            new Texture("Entities/player/player_right_up_walk1.png"),
            new Texture("Entities/player/player_right_up_walk2.png"),
        };
    }

    public void update(float deltaTime, Vector2 playerPosition) {
        // Calculate direction vector towards the player
        direction.set(playerPosition).sub(position).nor();

        // Update goblin position
        Vector2 newPos = new Vector2(position).add(direction.scl(speed * deltaTime));
        position.set(newPos);

        // Determine if the goblin is moving
        isMoving = direction.len2() > 0;

        // Update animation
        updateAnimation(deltaTime);
    }

    private void updateAnimation(float deltaTime) {
        Texture[] activeFrames = null;

        if (direction.y > 0) {
            activeFrames = walkingUpFrames;
        } else if (direction.y < 0) {
            activeFrames = walkingDownFrames;
        } else if (direction.x > 0) {
            activeFrames = walkingRightFrames;
        } else if (direction.x < 0) {
            activeFrames = walkingLeftFrames;
        }

        if (isMoving && activeFrames != null) {
            renderer.setTexture(activeFrames[(int) (Math.random() * activeFrames.length)]);
        }
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
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }
}
