package com.EmbersTrial.Enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class GoblinRenderer {
    private Texture goblinTexture;

    public GoblinRenderer(Texture goblinTexture) {
        this.goblinTexture = goblinTexture;
    }

    public void render(Batch batch, Vector2 position) {
        batch.draw(goblinTexture, position.x, position.y);
    }

    public void setTexture(Texture newTexture) {
        this.goblinTexture = newTexture;
    }

    public void dispose() {
        if (goblinTexture != null) {
            goblinTexture.dispose();
        }
    }
}
