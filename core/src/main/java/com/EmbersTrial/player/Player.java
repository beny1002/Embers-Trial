package com.EmbersTrial.player;

import com.EmbersTrial.effects.Particle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private PlayerRenderer renderer;
    private Vector2 position;
    private Vector2 direction;
    private float speed;
    private Texture texture;
    private float animationTimer = 0f;
    private float animationSpeed = 0.1f;
    private int currentFrameIndex = 0;
    private Texture[] walkingUpFrames, walkingDownFrames, walkingLeftFrames, walkingRightFrames;
    private Texture standingBack, standingFront, standingLeft, standingRight;
    private Vector2 lastDirection = new Vector2(0, -1); // Default direction for idle

    private boolean isBoosting = false;
    private float boostDuration = 0.5f; // Boost lasts for 0.5 seconds
    private float boostTimer = 0f;
    private float boostCooldown = 0f; // Cooldown time left
    private float boostCooldownMax = 3f; // Cooldown duration
    private float boostMultiplier = 3f; // 3x speed during boost

    private boolean isSprinting = false;
    private float sprintMultiplier = 1.5f;

    private String cooldownMessage = "";
    private float messageTimer = 0f;
    private final float messageDisplayTime = 1.5f; // Time to display the message
    private float messageAlpha = 0f; // Alpha for fading
    private BitmapFont font;
    private GlyphLayout layout;

    private TiledMapTileLayer collisionLayer;

    //add particles
    private ArrayList<Particle> particles;
    private Texture particleTexture;
    private List<Particle> sprintParticles = new ArrayList<>();
    private List<Particle> boostParticles = new ArrayList<>();


    // Player stats for health and XP
    private Stats stats;

    public Player(Texture playerTexture, TiledMapTileLayer collisionLayer) {
        this.texture = playerTexture;
        this.collisionLayer = collisionLayer;

        renderer = new PlayerRenderer(texture);
        position = new Vector2(100, 100);
        direction = new Vector2(0, 0);
        speed = 300f;

        font = new BitmapFont(); // Default font for rendering text
        font.setColor(Color.RED); // Set the font color to red
        layout = new GlyphLayout(); // Used to calculate text width/height

        stats = new Stats(100, 0); // Initialize stats with 100 health and 0 XP
        setAnimations();

        // Initialize particles
        particleTexture = createParticleTexture(); // Small dot texture
        particles = new ArrayList<>();
    }

    public int getPlayerWidth() {
        return standingBack != null ? standingBack.getWidth() : 0;
    }

    public int getPlayerHeight() {
        return standingBack != null ? standingBack.getHeight() : 0;
    }

    public Vector2 getPosition() {
        return position; // Return the player's current position
    }

    public Stats getStats() {
        return stats; // Expose player stats for UI integration
    }

    public void setAnimations() {
        standingBack = new Texture(Gdx.files.internal("player_stand_back.png"));
        standingFront = new Texture(Gdx.files.internal("player_stand_front.png"));
        standingLeft = new Texture(Gdx.files.internal("player_stand_left.png"));
        standingRight = new Texture(Gdx.files.internal("player_stand_right.png"));

        walkingUpFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_back.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_back.png")),
            standingBack
        };

        walkingDownFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_front.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_front.png")),
            standingFront
        };

        walkingLeftFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceLeft.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_faceLeft.png")),
            standingLeft
        };

        walkingRightFrames = new Texture[]{
            new Texture(Gdx.files.internal("player_leftLegWalk_faceRight.png")),
            new Texture(Gdx.files.internal("player_rightLegWalk_faceRight.png")),
            standingRight
        };
    }

    public void update(float deltaTime) {
        direction.set(0, 0);
        handleSprintLogic(deltaTime);
        handleBoostLogic(deltaTime);
        handleAttack(deltaTime);

        boolean isMoving = handleMovement(deltaTime);

        if (!isMoving) {
            resetToIdleState();
        }

        movePlayer(deltaTime);

        // Update particles
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update(deltaTime);
            if (!particle.isAlive()) {
                iterator.remove(); // Remove dead particles
            }
        }
    }

    private void handleSprintLogic(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && stats.getStamina() != 0) {
            isSprinting = true;

            stats.reduceStamina(45 * deltaTime);
            // Customizable offsets for three particles
            Vector2 offset1 = new Vector2(50, 50); // First particle (center)
            Vector2 offset2 = new Vector2(40, 50); // Second particle (left)
            Vector2 offset3 = new Vector2(60, 50); // Third particle (right)

            // Adjust offsets dynamically based on lastDirection
            if (lastDirection.x > 0) { // Moving right
                offset1.set(-10, 10);
                offset2.set(-20, 10);
                offset3.set(0, 10);
            } else if (lastDirection.x < 0) { // Moving left
                offset1.set(110, 10);
                offset2.set(120, 10);
                offset3.set(100, 10);
            } else if (lastDirection.y > 0) { // Moving up
                offset1.set(50, 10);
                offset2.set(40, 10);
                offset3.set(60, 10);
            } else if (lastDirection.y < 0) { // Moving down
                offset1.set(50, -10);
                offset2.set(40, -10);
                offset3.set(60, -10);
            }

            // Calculate spawn positions for each particle
            Vector2 spawnPosition1 = new Vector2(position).add(offset1);
            Vector2 spawnPosition2 = new Vector2(position).add(offset2);
            Vector2 spawnPosition3 = new Vector2(position).add(offset3);

            // Velocity for all particles (opposite to direction)
            Vector2 velocity = new Vector2(-direction.x * 30, -direction.y * 30);

            // Add particles with custom positions
            sprintParticles.add(new Particle(spawnPosition1, velocity, 0.5f, Color.GRAY, particleTexture));
            sprintParticles.add(new Particle(spawnPosition2, velocity, 0.5f, Color.GRAY, particleTexture));
            sprintParticles.add(new Particle(spawnPosition3, velocity, 0.5f, Color.GRAY, particleTexture));
        } else {
            isSprinting = false;

            stats.rechargeStamina(20 * deltaTime);

            sprintParticles.clear();
        }
        if (stats.getStamina() <= 0) {
            stats.reduceStamina(0); // Clamp stamina to 0
            isSprinting = false;   // Stop sprinting
            sprintParticles.clear();
        }

    }

    private void handleBoostLogic(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!isBoosting && stats.getStamina() == 100) {
                isBoosting = true;
                boostTimer = boostDuration;
            }
        }

        if (isBoosting) {
            stats.reduceStamina(250 * deltaTime); // Stamina reduces faster during boost
            boostTimer -= deltaTime;

            // Stop boosting if stamina is depleted or boost duration ends
            if (stats.getStamina() <= 0 || boostTimer <= 0) {
                isBoosting = false;
            } else {
                // Add particles while boosting
                Vector2 offset4 = new Vector2(50, 50); // First particle (center)
                Vector2 offset5 = new Vector2(40, 50); // Second particle (left)
                Vector2 offset6 = new Vector2(60, 50); // Third particle (right)

                // Adjust offsets dynamically based on lastDirection
                if (lastDirection.x > 0) { // Moving right
                    offset4.set(-10, 50);
                    offset5.set(-20, 50);
                    offset6.set(0, 50);
                } else if (lastDirection.x < 0) { // Moving left
                    offset4.set(110, 50);
                    offset5.set(120, 50);
                    offset6.set(100, 50);
                } else if (lastDirection.y > 0) { // Moving up
                    offset4.set(50, 50);
                    offset5.set(40, 50);
                    offset6.set(60, 50);
                } else if (lastDirection.y < 0) { // Moving down
                    offset4.set(50, -10);
                    offset5.set(40, -10);
                    offset6.set(60, -10);
                }

                // Calculate spawn positions for each particle
                Vector2 spawnPosition1 = new Vector2(position).add(offset4);
                Vector2 spawnPosition2 = new Vector2(position).add(offset5);
                Vector2 spawnPosition3 = new Vector2(position).add(offset6);

                // Velocity for all particles (opposite to direction)
                Vector2 velocity = new Vector2(-direction.x * 50, -direction.y * 50); // Boost particles are faster

                // Add particles with custom positions
                boostParticles.add(new Particle(spawnPosition1, velocity, 0.3f, Color.WHITE, particleTexture)); // Boost particles are red
                boostParticles.add(new Particle(spawnPosition2, velocity, 0.3f, Color.WHITE, particleTexture));
                boostParticles.add(new Particle(spawnPosition3, velocity, 0.3f, Color.WHITE, particleTexture));
            }
        }

        // Clear particles when boost ends
        if (!isBoosting) {
            boostParticles.clear();
        }
    }

    private boolean handleAttack(float deltaTime) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Check for left mouse button click
            stats.takeDamage(10); // Reduce player's health by 10
            System.out.println("Player attacked! Health reduced by 10. Current health: " + stats.getHealth());
            return true;
        }
        return false;
    }

    private boolean handleMovement(float deltaTime) {
        boolean isMoving = false;
        Texture[] activeFrames = null;

        // Handle diagonal movement
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.set(1, 1);
            lastDirection.set(1, 1);
            activeFrames = walkingRightFrames;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.set(-1, 1);
            lastDirection.set(-1, 1);
            activeFrames = walkingLeftFrames;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.set(1, -1);
            lastDirection.set(1, -1);
            activeFrames = walkingRightFrames;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.set(-1, -1);
            lastDirection.set(-1, -1);
            activeFrames = walkingLeftFrames;
            isMoving = true;
        }

        // Handle cardinal movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;
            lastDirection.set(0, 1);
            activeFrames = walkingUpFrames;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;
            lastDirection.set(0, -1);
            activeFrames = walkingDownFrames;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;
            lastDirection.set(-1, 0);
            activeFrames = walkingLeftFrames;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;
            lastDirection.set(1, 0);
            activeFrames = walkingRightFrames;
            isMoving = true;
        }

        if (isMoving && activeFrames != null) {
            updateAnimation(activeFrames, deltaTime);
        }

        return isMoving;
    }

    private void updateAnimation(Texture[] frames, float deltaTime) {
        animationTimer += deltaTime;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0f;
            currentFrameIndex = (currentFrameIndex + 1) % frames.length;
        }

        renderer.setTexture(frames[currentFrameIndex]);
    }

    private void resetToIdleState() {
        if (lastDirection.y > 0) renderer.setTexture(standingBack);
        else if (lastDirection.y < 0) renderer.setTexture(standingFront);
        else if (lastDirection.x < 0) renderer.setTexture(standingLeft);
        else if (lastDirection.x > 0) renderer.setTexture(standingRight);
    }

    private void movePlayer(float deltaTime) {
        float currentSpeed = isBoosting ? speed * boostMultiplier : (isSprinting ? speed * sprintMultiplier : speed);

        direction.nor();

        Vector2 newPos = new Vector2(position).add(direction.scl(currentSpeed * deltaTime));

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
        for (Particle particle : sprintParticles) {
            particle.render(batch);
        }
        for (Particle particle : boostParticles) {
            particle.render(batch);
        }
        renderer.render(batch, position);
    }

    private Texture createParticleTexture() {
        Pixmap pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888); // 4x4 pixel size
        pixmap.setColor(1, 1, 1, 1); // Set to white
        pixmap.fillRectangle(0, 0, 4, 4); // Fill the pixmap with a rectangle
        Texture texture = new Texture(pixmap);
        pixmap.dispose(); // Free Pixmap memory
        return texture;
    }


    public void dispose() {
        renderer.dispose();
        for (Texture frame : walkingUpFrames) frame.dispose();
        for (Texture frame : walkingDownFrames) frame.dispose();
        for (Texture frame : walkingLeftFrames) frame.dispose();
        for (Texture frame : walkingRightFrames) frame.dispose();

        standingBack.dispose();
        standingFront.dispose();
        standingLeft.dispose();
        standingRight.dispose();

        particleTexture.dispose(); // Dispose of particle texture
    }
}
