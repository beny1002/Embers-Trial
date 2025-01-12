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
        if (playerTexture == null || playerTexture.getTextureObjectHandle() == 0) {
            System.err.println("Invalid texture being rendered!");
            return;
        }
        batch.draw(playerTexture, position.x, position.y, originalWidth * 4, originalHeight * 4);
    }


    public void setTexture(Texture newTexture) {
//        if (playerTexture != null) {
//            playerTexture.dispose(); // Dispose of the old texture
//        }
        if (newTexture == null || newTexture.getTextureObjectHandle() == 0) {
            System.err.println("Invalid texture passed to setTexture!");
            return;
        }
        playerTexture = newTexture; // Update reference without disposal
//        originalWidth = newTexture.getWidth();
//        originalHeight = newTexture.getHeight();
    }

    public void dispose() {
        if (playerTexture != null) {
            playerTexture.dispose();
            playerTexture = null;
        }
    }
}
