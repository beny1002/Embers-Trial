package com.EmbersTrial.Enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

// Basic enemy stats now scale with waves
public class Enemy {
    // Track the number of waves to dynamically increase stats
    private   double numWaves = 1;

    // Base stats for the enemy
    private String name;
    private double hp;
    private double defense;
    private double damage;
    private double attackSpeed;

    // Position and animation fields
    private float x, y;
    private Animation<TextureRegion> animation;
    private float stateTime;

    // Constructor for setting stats and animation
    public Enemy(String name, double baseHp, double baseDefense, double baseDamage, double baseAttackSpeed, Texture spriteSheet, int frameCols, int frameRows, float x, float y) {
        this.name = name;
        this.hp = baseHp + (numWaves * 10); // Increase HP with waves
        this.defense = baseDefense + (numWaves * 2); // Increase Defense with waves
        this.damage = baseDamage + (numWaves * 3); // Increase Damage with waves
        this.attackSpeed = baseAttackSpeed + (numWaves * 0.1); // Slightly increase Attack Speed with waves

        // Set position
        this.x = x;
        this.y = y;

        // Create animation from sprite sheet
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / frameCols, spriteSheet.getHeight() / frameRows);
        Array<TextureRegion> frames = new Array<>();
        for (TextureRegion[] row : tmp) {
            for (TextureRegion frame : row) {
                frames.add(frame);
            }
        }
        this.animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        this.stateTime = 0f;
    }

    // Increment wave count and update scaling factor
    public double incrementWaves() {
        numWaves++;
        return numWaves;
    }

    // Apply damage to this enemy's HP
    public void takeDamage(double attackDamage, double defense) {
        double effectiveDamage = Math.max(attackDamage - defense, 0); // No negative damage
        hp -= effectiveDamage;
        if (hp < 0) {
            hp = 0; // Ensure HP doesn't go below 0
        }
        System.out.println(name + " took " + effectiveDamage + " damage. Remaining HP: " + hp);
    }

    // Update animation state
    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    // Render the enemy
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        batch.draw(currentFrame, x, y);
    }

    // Getters for position
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Static classes for specific enemy types with predefined stats and animations
    public static class Goblin extends Enemy {
        public Goblin(Texture spriteSheet, float x, float y) {
            super("Goblin", 50, 5, 10, 1.2, spriteSheet, 4, 2, x, y);
        }
    }

    public static class Knight extends Enemy {
        public Knight(Texture spriteSheet, float x, float y) {
            super("Knight", 100, 10, 20, 1.0, spriteSheet, 4, 2, x, y);
        }
    }

    public static class Dragon extends Enemy {
        public Dragon(Texture spriteSheet, float x, float y) {
            super("Dragon", 200, 15, 40, 0.8, spriteSheet, 4, 2, x, y);
        }
    }

    public static class Bandit extends Enemy {
        public Bandit(Texture spriteSheet, float x, float y) {
            super("Bandit", 75, 7, 15, 1.5, spriteSheet, 4, 2, x, y);
        }
    }

    public static class Archer extends Enemy {
        public Archer(Texture spriteSheet, float x, float y) {
            super("Archer", 60, 5, 12, 1.8, spriteSheet, 4, 2, x, y);
        }
    }

    public static class Slime extends Enemy {
        public Slime(Texture spriteSheet, float x, float y) {
            super("Slime", 40, 3, 8, 1.0, spriteSheet, 4, 2, x, y);
        }
    }
}
