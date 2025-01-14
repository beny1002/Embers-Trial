package com.EmbersTrial.Enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    private String name;
    private double hp;
    private double defense;
    private double damage;
    private double attackSpeed;
    private double numWaves = 1; // Tracks waves for scaling stats

    protected Texture currentTexture;
    protected Vector2 position;
    protected Vector2 direction;
    protected float speed;
    protected TiledMapTileLayer collisionLayer;

    public Enemy(String name, double baseHp, double baseDefense, double baseDamage, double baseAttackSpeed, Texture texture, Vector2 position, TiledMapTileLayer collisionLayer, float speed) {
        this.name = name;
        this.hp = baseHp + (numWaves * 10);
        this.defense = baseDefense + (numWaves * 2);
        this.damage = baseDamage + (numWaves * 3);
        this.attackSpeed = baseAttackSpeed + (numWaves * 0.1);

        this.currentTexture = texture;
        this.position = position;
        this.direction = new Vector2(0, 0);
        this.collisionLayer = collisionLayer;
        this.speed = speed;
    }

    public void update(float deltaTime, Vector2 targetPosition) {
        direction.set(targetPosition).sub(position).nor();
        float currentSpeed = speed * deltaTime;
        Vector2 newPos = new Vector2(position).add(direction.scl(currentSpeed));

        if (!isColliding(newPos)) {
            position.set(newPos);
        }
    }

    private boolean isColliding(Vector2 newPos) {
        float tileWidth = collisionLayer.getTileWidth() * 4f;
        float tileHeight = collisionLayer.getTileHeight() * 4f;

        int tileX = (int) (newPos.x / tileWidth);
        int tileY = (int) (newPos.y / tileHeight);

        if (tileX >= 0 && tileX < collisionLayer.getWidth() && tileY >= 0 && tileY < collisionLayer.getHeight()) {
            TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
            return cell != null && cell.getTile().getProperties().containsKey("blocked");
        }

        return false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, position.x, position.y);
    }

    public void takeDamage(double attackDamage) {
        double effectiveDamage = Math.max(attackDamage - defense, 0);
        hp -= effectiveDamage;
        if (hp < 0) hp = 0;

        System.out.println(name + " took " + effectiveDamage + " damage. Remaining HP: " + hp);
    }

    public void dispose() {
        if (currentTexture != null) {
            currentTexture.dispose();
        }
    }

    public abstract void handleAnimation(float deltaTime, Vector2 targetPosition);
}
