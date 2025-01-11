package com.EmbersTrial.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayerRenderer {
    private Texture playerTexture; //Single texture for the player
    private int originalWidth;
    private int originalHeight;

    public PlayerRenderer(Texture playerTexture) {
        this.playerTexture = playerTexture;
        this.originalWidth = playerTexture.getWidth();
        this.originalHeight = playerTexture.getHeight();
    }

    public void render(SpriteBatch batch, Vector2 position) {
        if (playerTexture != null) {
            //draw the texture scaled to 4x its original size
            batch.draw(playerTexture, position.x, position.y, originalWidth * 4, originalHeight * 4);
        }
    }

    public void dispose() {
        if (playerTexture != null) {
            playerTexture.dispose();
        }
    }
}
