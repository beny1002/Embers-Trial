package com.EmbersTrial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class TileMap {
    private Texture tileSheet;
    private List<Tile> tiles; // List of tiles manually placed
    private List<Tile> placedTiles; // List of placed tiles
    private int tileWidth = 110; // Width of each tile
    private int tileHeight = 110; // Height of each tile

    public TileMap(String tileSheetPath) {
        // Load the tile sheet
        tileSheet = new Texture(Gdx.files.internal(tileSheetPath));

        // Define the tiles
        tiles = new ArrayList<>();
        tiles.add(new Tile(new TextureRegion(tileSheet, 0, 0, tileWidth, tileHeight), 0, 0)); // Tile 0
        tiles.add(new Tile(new TextureRegion(tileSheet, 110, 0, tileWidth, tileHeight), 0, 0)); // Tile 1
        tiles.add(new Tile(new TextureRegion(tileSheet, 220, 0, tileWidth, tileHeight), 0, 0)); // Tile 2

        // Initialize placed tiles list
        placedTiles = new ArrayList<>();
    }

    public void placeTile(int tileIndex, int worldX, int worldY) {
        if (tileIndex >= 0 && tileIndex < tiles.size()) {
            Tile tile = new Tile(tiles.get(tileIndex).getTextureRegion(), worldX, worldY);
            placedTiles.add(tile); // Add to placed tiles
        }
    }

    public void generateSampleMap() {
        // Place tiles manually to generate a sample map
        placeTile(0, 0, 0);   // Tile 0 at (0, 0)
        placeTile(1, 110, 0); // Tile 1 at (110, 0)
        placeTile(2, 220, 0); // Tile 2 at (220, 0)
        placeTile(0, 0, 110); // Tile 0 at (0, 110)
        placeTile(1, 110, 110); // Tile 1 at (110, 110)
    }

    public void render(Batch batch, OrthographicCamera camera) {
        // Render tiles within the camera's viewport
        for (Tile tile : placedTiles) {
            if (tile.isVisible(camera, tileWidth, tileHeight)) {
                batch.draw(tile.getTextureRegion(), tile.getX(), tile.getY(), tileWidth, tileHeight);
            }
        }
    }

    public TextureRegion getBlankTile() {
        // Return the first tile as a blank placeholder
        return tiles.get(0).getTextureRegion();
    }

    public void dispose() {
        tileSheet.dispose();
    }

    // Internal Tile class
    private static class Tile {
        private TextureRegion textureRegion;
        private int x, y;

        public Tile(TextureRegion textureRegion, int x, int y) {
            this.textureRegion = textureRegion;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public TextureRegion getTextureRegion() {
            return textureRegion;
        }

        public boolean isVisible(OrthographicCamera camera, int tileWidth, int tileHeight) {
            float camLeft = camera.position.x - camera.viewportWidth / 2;
            float camRight = camera.position.x + camera.viewportWidth / 2;
            float camBottom = camera.position.y - camera.viewportHeight / 2;
            float camTop = camera.position.y + camera.viewportHeight / 2;

            return x + tileWidth > camLeft && x < camRight && y + tileHeight > camBottom && y < camTop;
        }
    }
}
