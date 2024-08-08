package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Objects.*;
import de.tum.cit.ase.maze.Screens.GameOverScreen;
import de.tum.cit.ase.maze.Screens.GameScreen;

/**
 * The Player class is responsible for the logic behind player movement, collision and animation.
 * It handles the textures of the player and the game logic for movement.
 */
public class Player {
    private static final TextureRegion lookingUpStanding = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 0, 69, 16, 23);
    private static final TextureRegion lookingUpWalking1 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 16, 70, 16, 23);
    private static final TextureRegion lookingUpWalking2 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 48, 70, 16, 23);
    private static final TextureRegion lookingDownStanding = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 1, 6, 16, 23);
    private static final TextureRegion lookingDownWalking1 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 17, 7, 16, 23);
    private static final TextureRegion lookingDownWalking2 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 49, 7, 16, 23);
    private static final TextureRegion lookingRightStanding = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 2, 38, 16, 23);
    private static final TextureRegion lookingRightWalking1 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 18, 39, 16, 23);
    private static final TextureRegion lookingRightWalking2 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 50, 39, 16, 23);
    private static final TextureRegion lookingLeftStanding = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 1, 102, 16, 23);
    private static final TextureRegion lookingLeftWalking1 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 17, 103, 16, 23);
    private static final TextureRegion lookingLeftWalking2 = new TextureRegion(new Texture(Gdx.files.internal("assets/character.png")), 49, 103, 16, 23);
    private final TextureRegion[] framesUp = {lookingUpStanding, lookingUpWalking1, lookingUpWalking2};
    private final TextureRegion[] framesDown = {lookingDownStanding, lookingDownWalking1, lookingDownWalking2};
    private final TextureRegion[] framesRight = {lookingRightStanding, lookingRightWalking1, lookingRightWalking2};
    private final TextureRegion[] framesLeft = {lookingLeftStanding, lookingLeftWalking1, lookingLeftWalking2};
    private final Animation<TextureRegion> animationUp = new Animation<>(0.1f, framesUp);
    private final Animation<TextureRegion> animationDown = new Animation<>(0.1f, framesDown);
    private final Animation<TextureRegion> animationLeft = new Animation<>(0.1f, framesLeft);
    private final Animation<TextureRegion> animationRight = new Animation<>(0.1f, framesRight);
    private String name;
    private int livesRemaining;
    private Key key;
    private float x;
    private float y;
    private float speed = 50f;
    private float stateTime; //Keep track of elapsed time
    private boolean isMoving;
    private boolean isProtected;
    private Maze maze;
    private Direction currentDirection;
    private Rectangle boundingBox; //To avoid collisions
    MazeRunnerGame game;
    private Sound pickUpSound;
    private Sound hit;
    private boolean isVulnerable; // Flag to indicate whether the player is currently vulnerable
    private float vulnerabilityDuration; // Duration of vulnerability after each collision
    private float currentVulnerabilityTime; // Time remaining in th
    private boolean isExtraSpeedActive;
    private float extraSpeedDuration = 2f; // Duration of extra speed
    private float currentExtraSpeedTime; // Time remaining for extra speed
    private float extraSpeedCooldown = 8f; // Cooldown before using extra speed again
    private float currentCooldownTime; // Time remaining for cooldown
    private float currentProtectionTime;

    /**
     * Constructor for Player. Sets up starting values for the player.
     *
     * @param x    Coordinate x where to draw the player.
     * @param y    Coordinate x where to draw the player.
     * @param name Player's name.
     * @param game The main game class, used to access global resources and methods.
     * @param maze The maze class, used to access resources and methods.
     */
    public Player(float x, float y, String name, MazeRunnerGame game, Maze maze) {
        this.name = name;
        livesRemaining = 3;
        key = null;
        this.x = x;
        this.y = y;
        isMoving = false;
        isProtected = false;
        stateTime = 0f;
        currentDirection = Direction.DOWN;
        this.boundingBox = new Rectangle(x+2, y, 12, 12);
        this.game = game;
        this.maze = maze;
        hit = Gdx.audio.newSound(Gdx.files.internal("assets/Soundtrack/404747__owlstorm__retro-video-game-sfx-ouch.wav"));
        pickUpSound = Gdx.audio.newSound(Gdx.files.internal("assets/Soundtrack/646671__sounddesignforyou__coin-pickup-sfx-3.mp3"));
        isVulnerable = true; // Initialize the player as vulnerable
        vulnerabilityDuration = 1f;
        currentVulnerabilityTime = 0f;


    }

    /**
     * Start moving the player and specify the direction.
     *
     * @param direction Direction in which the player is moving.
     */
    public void startMoving(Direction direction) {
        isMoving = true;
        currentDirection = direction;
    }

    /**
     * Stop moving the player.
     */
    public void stopMoving() {
        isMoving = false;
        stateTime = 0f;
    }

    /**
     * Updates the player animation
     *
     * @param delta Used to determine when to update the animation.
     */
    public void update(float delta) {
        if (isMoving) {
            stateTime += delta;
            move(delta);
        }
        updateVulnerability(delta);
        extraSpeed(delta);
        checkCollisionWithProtection(GameScreen.getProtections(), delta);
        if (!isProtected) {
            checkCollisionWithEnemy(GameScreen.getEnemies());
        }
    }

    /**
     * Updates the value if the player is vulnerable.
     *
     * @param delta Used to determine when the player is vulnerable.
     */
    private void updateVulnerability(float delta) {
        if (!isVulnerable) {
            currentVulnerabilityTime = Math.max(0f, currentVulnerabilityTime - delta);

            if (currentVulnerabilityTime == 0f) {
                isVulnerable = true;
            }
        }
    }

    /**
     * Gives a player a temporary boost of speed for 2 seconds.
     *
     * @param delta Used to determine when the player activated boost.
     */
    public void extraSpeed(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!isExtraSpeedActive && currentCooldownTime <= 0f) {
                isExtraSpeedActive = true;
                currentExtraSpeedTime = extraSpeedDuration;
                speed = 100f; // Set speed to extra speed value
            }
        }

        if (isExtraSpeedActive) {
            currentExtraSpeedTime = Math.max(0f, currentExtraSpeedTime - delta);

            if (currentExtraSpeedTime == 0f) {
                isExtraSpeedActive = false;
                currentCooldownTime = extraSpeedCooldown;
                speed = 50f; // Reset speed to normal value
            }
        }

        if (currentCooldownTime > 0f) {
            currentCooldownTime = Math.max(0f, currentCooldownTime - delta);
        }
    }

    /**
     * Adjust the player's position based on the animation speed and direction.
     *
     * @param delta Used to determine when to update the position of the player.
     */
    private void move(float delta) {
        float previousX = x;
        float previousY = y;
        switch (currentDirection) {
            case LEFT:
                if (!isProtected) {
                    checkCollisionWithTraps(GameScreen.getTraps());
                }
                checkCollisionWithKey(GameScreen.getKeys());
                checkCollisionWithLife(GameScreen.getLives());
                x -= speed * delta;
                boundingBox.setX(x+2);
                checkCollisionWithExit(previousX, previousY, GameScreen.getExits());
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setX(x+2);
                }
                break;
            case RIGHT:
                if (!isProtected) {
                    checkCollisionWithTraps(GameScreen.getTraps());
                }
                checkCollisionWithKey(GameScreen.getKeys());
                checkCollisionWithLife(GameScreen.getLives());
                x += speed * delta;
                boundingBox.setX(x+2);
                checkCollisionWithExit(previousX, previousY, GameScreen.getExits());
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setX(x+2);
                }
                break;
            case UP:
                if (!isProtected) {
                    checkCollisionWithTraps(GameScreen.getTraps());
                }
                checkCollisionWithKey(GameScreen.getKeys());
                checkCollisionWithLife(GameScreen.getLives());
                y += speed * delta;
                boundingBox.setY(y);
                checkCollisionWithExit(previousX, previousY, GameScreen.getExits());
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setY(y);
                }
                break;
            case DOWN:
                if (!isProtected) {
                    checkCollisionWithTraps(GameScreen.getTraps());
                }
                checkCollisionWithKey(GameScreen.getKeys());
                checkCollisionWithLife(GameScreen.getLives());
                y -= speed * delta;
                boundingBox.setY(y);
                checkCollisionWithExit(previousX, previousY, GameScreen.getExits());
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setY(y);
                }
                break;
        }
    }

    /**
     * Gets the animation according to the direction player is moving.
     *
     * @return Current frame of the Player according to his direction.
     */
    public TextureRegion getCurrentFrame() {
        switch (currentDirection) {
            case LEFT:
                return animationLeft.getKeyFrame(stateTime, true);
            case UP:
                return animationUp.getKeyFrame(stateTime, true);
            case DOWN:
                return animationDown.getKeyFrame(stateTime, true);
            case RIGHT:
                return animationRight.getKeyFrame(stateTime, true);
            default:
                return animationDown.getKeyFrame(stateTime, true);
        }
    }

    /**
     * Checks if the player collided with a wall.
     *
     * @param walls Used to get bounding box of walls.
     * @return Return true if player collides with wall
     */
    public boolean checkCollisionWithWall(Array<Wall> walls) {
        for (Wall wall : walls) {
            if (boundingBox.overlaps(wall.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player collided with a key.
     * Plays a sound if the key has not been collected before.
     * Marks the key as collected.
     *
     * @param keys Used to get bounding box of keys.
     */
    public void checkCollisionWithKey(Array<Key> keys) {
        for (Key key : keys) {
            if (boundingBox.overlaps(key.getBoundingBox())) {
                this.key = key;

                if (!key.isPlayerPickedUp()) {
                    long id = pickUpSound.play();
                    pickUpSound.setVolume(id, 0.1F);
                    pickUpSound.setLooping(id, false);
                }
                key.setPlayerPickedUp(true);
            }
        }
    }

    /**
     * Checks if the player collided with an exit.
     * If the player collides with an exit, and he has a key, game goes to win screen.
     * Otherwise, moves player back to previous coordinates.
     *
     * @param previousX Saves the x coordinate before the player moved.
     * @param previousY Saves the y coordinate before the player moved.
     * @param exits     Used to get bounding boxes of exits.
     */
    public void checkCollisionWithExit(float previousX, float previousY, Array<Exit> exits) {
        for (Exit exit : exits) {
            if (boundingBox.overlaps(exit.getBoundingBox())) {
                if (key != null) { // If player collides with exit and has a key
                    game.goToWin(maze.getLevel()); //Go to win screen
                    break;
                } else {
                    x = previousX;
                    y = previousY;
                    break;
                }
            }
        }
    }

    /**
     * Checks if a player collided with a trap.
     * If a player collided with a trap, game plays hit sound and reduces player lives by calling reduce method.
     *
     * @param traps Used to get traps bounding boxes.
     */
    public void checkCollisionWithTraps(Array<Trap> traps) {
        if (isVulnerable) {
            for (Trap trap : traps) {
                if (boundingBox.overlaps(trap.getBoundingBox())) {
                    reduce();
                    hit.play();
                    isVulnerable = false;
                    currentVulnerabilityTime = vulnerabilityDuration;
                    if (livesRemaining == 0) {
                        GameOverScreen.setCauseOfDeath(" fell into a trap!");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Checks if a player collided with an enemy.
     * If a player collided with an enemy, game plays hit sound and reduces player lives by calling reduce method.
     *
     * @param enemies Used to get enemies bounding boxes.
     */
    public void checkCollisionWithEnemy(Array<Enemy> enemies) {
        if (isVulnerable) {
            for (Enemy enemy : enemies) {
                if (boundingBox.overlaps(enemy.getBoundingBox())) {
                    reduce();
                    hit.play();
                    isVulnerable = false;
                    currentVulnerabilityTime = vulnerabilityDuration;
                    if (livesRemaining == 0) {
                        GameOverScreen.setCauseOfDeath(" was killed by a Ghost!");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Checks if a player collided with an extra life.
     * If a player collided with an extra life, game plays pick up sound and adds and life to the player if he has less than three.
     *
     * @param lives Used to get extra lives bounding boxes.
     */
    public void checkCollisionWithLife(Array<Life> lives) {
        Life pickedUpLife = null;
        for (Life life : lives) {
            if (boundingBox.overlaps(life.getBoundingBox())) {
                if (livesRemaining < 3) {
                    livesRemaining++;
                    pickedUpLife = life;
                    long id = pickUpSound.play();
                    pickUpSound.setVolume(id, 0.1F);
                    pickUpSound.setLooping(id, false);
                }
            }
        }
        if (pickedUpLife != null) {
            lives.removeValue(pickedUpLife, false);
        }
    }

    /**
     * Checks if a player collided with an entry.
     * If entry has the same texture as walls, player is inside the maze.
     *
     * @param entry Used to get entries bounding box.
     * @return true if player collides with an entry.
     * @return true if player gets out of maze bounds.
     */
    public boolean checkCollisionWIthEntry(Entry entry) {
        if (GameScreen.getEntry().getTexture() == Entry.getWALL()) {
            if (boundingBox.overlaps(GameScreen.getEntry().getBoundingBox())) {
                // Player is inside the maze
                return true; // Return true if player collides with entry
            }
        } else {
            if (x < 0 || y < 0 || x > maze.getxMax() * 16 || y > maze.getyMax() * 16) {
                return true; // Return true if player gets out of the maze bounds
            }
        }
        return false;
    }

    /**
     * Checks if a player collided with a shield object.
     * If a player collided with a shield object, he has three second protection against all types of enemies.
     *
     * @param protections Used to get shield bounding box.
     * @param delta       Used to determine when the player has protection.
     */
    public void checkCollisionWithProtection(Array<Protection> protections, float delta) {
        Protection pickedUpProtection = null;
        for (Protection protection : protections) {
            if (boundingBox.overlaps(protection.getBoundingBox())) {
                if (!isProtected) {
                    pickedUpProtection = protection;
                    long id = pickUpSound.play();
                    pickUpSound.setVolume(id, 0.1F);
                    pickUpSound.setLooping(id, false);
                }
            }
        }
        if (pickedUpProtection != null) {
            protections.removeValue(pickedUpProtection, false);
            isProtected = true;
            currentProtectionTime = 3f;
        }

        if (isProtected) {
            currentProtectionTime = Math.max(0f, currentProtectionTime - delta);
            if (currentProtectionTime <= 0f) {
                isProtected = false;
            }
        }
    }

    /**
     * Reduces players lives.
     */
    public void reduce() {
        livesRemaining--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getLivesRemaining() {
        return livesRemaining;
    }

    public void setLivesRemaining(int livesRemaining) {
        this.livesRemaining = livesRemaining;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Animation<TextureRegion> getAnimationUp() {
        return animationUp;
    }

    public Animation<TextureRegion> getAnimationDown() {
        return animationDown;
    }

    public Animation<TextureRegion> getAnimationLeft() {
        return animationLeft;
    }

    public Animation<TextureRegion> getAnimationRight() {
        return animationRight;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public boolean isExtraSpeedActive() {
        return isExtraSpeedActive;
    }

    public void setExtraSpeedActive(boolean extraSpeedActive) {
        isExtraSpeedActive = extraSpeedActive;
    }

    public float getExtraSpeedCooldown() {
        return extraSpeedCooldown;
    }

    public void setExtraSpeedCooldown(float extraSpeedCooldown) {
        this.extraSpeedCooldown = extraSpeedCooldown;
    }

    public float getCurrentCooldownTime() {
        return currentCooldownTime;
    }

    public void setCurrentCooldownTime(float currentCooldownTime) {
        this.currentCooldownTime = currentCooldownTime;
    }

    public float getCurrentExtraSpeedTime() {
        return currentExtraSpeedTime;
    }

    public void setCurrentExtraSpeedTime(float currentExtraSpeedTime) {
        this.currentExtraSpeedTime = currentExtraSpeedTime;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }
}

