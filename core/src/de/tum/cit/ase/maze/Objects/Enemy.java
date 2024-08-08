package de.tum.cit.ase.maze.Objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.*;
import de.tum.cit.ase.maze.Screens.GameScreen;

import java.util.Random;

/**
 * The Enemy class is responsible for the logic behind enemy movement, collision and animation.
 * It handles the textures of the enemy and the game logic for movement.
 */
public class Enemy extends GameObject {
    private static final TextureRegion lookingDownUp = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 98, 64, 12, 16);
    private static final TextureRegion lookingDownDown = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 114, 65, 12, 15);
    private static final TextureRegion lookingUpUp = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 98, 112, 12, 16);
    private static final TextureRegion lookingUpDown = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 114, 113, 12, 15);
    private static final TextureRegion lookingRightUp = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 98, 96, 12, 16);
    private static final TextureRegion lookingRightDown = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 114, 97, 12, 15);
    private static final TextureRegion lookingLeftUp = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 98, 80, 12, 16);
    private static final TextureRegion lookingLeftDown = new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 114, 81, 12, 15);
    //Arrays of the different frames of the animation
    private final TextureRegion[] framesUp = {lookingUpUp, lookingUpDown};
    private final TextureRegion[] framesDown = {lookingDownUp, lookingDownDown};
    private final TextureRegion[] framesRight = {lookingRightUp, lookingRightDown};
    private final TextureRegion[] framesLeft = {lookingLeftUp, lookingLeftDown};
    //Animations
    private final Animation<TextureRegion> animationUp = new Animation<>(0.5f, framesUp);
    private final Animation<TextureRegion> animationDown = new Animation<>(0.5f, framesDown);
    private final Animation<TextureRegion> animationLeft = new Animation<>(0.5f, framesLeft);
    private final Animation<TextureRegion> animationRight = new Animation<>(0.5f, framesRight);
    private String name;
    private float moveTimer; // Timer to control movement updates
    private float moveInterval; // Time interval for movement updates

    private Rectangle boundingBox;
    private float x;
    private float y;
    private Direction currentDirection;
    public int actionLockCounter;
    private boolean allowedMove;
    private float moveableDuration;
    private float currentMoveTime;

    /**
     * Constructor for Enemy class. Sets up starting values for the player.
     *
     * @param x    Coordinate x where to draw the enemy.
     * @param y    Coordinate y where to draw the enemy.
     * @param name Enemies name.
     */
    public Enemy(float x, float y, String name) {
        super(new TextureRegion(new Texture(Gdx.files.internal("assets/mobs.png")), 114, 65, 12, 15), x, y, 16, 16);
        ;
        this.name = name;
        this.x = x;
        this.y = y;
        this.moveTimer = 0f; // Initialize the timer
        this.moveInterval = 2f; // Set the time interval for movement updates (adjust as needed)

        this.boundingBox = new Rectangle(x, y, 16, 16);
        allowedMove = true;
        moveableDuration = 0.5f;
        currentMoveTime = 0f;
        currentDirection = Direction.UP;
    }

    /**
     * Updates the enemies ability to move if enough time has passed.
     *
     * @param delta Used to determine if enough time had passed.
     */
    private void updateMoveability(float delta) {
        if (!allowedMove) {
            currentMoveTime = Math.max(0f, currentMoveTime - delta);

            if (currentMoveTime == 0f) {
                allowedMove = true;
            }
        }
    }

    /**
     * Adjust the enemies position based on if the move is allowed and its animation.
     *
     * @param delta Used to determine when to update enemies location.
     */
    public void update(float delta) {
        float previousX = x;
        float previousY = y;
        updateMoveability(delta);
        move();
        switch (currentDirection) {
            case LEFT:
                x -= 0.5F;
                boundingBox.setX(x);
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry()) || this.checkCollisionWithExit(GameScreen.getExits())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setX(x);
                }
                break;
            case RIGHT:
                x += 0.5F;
                boundingBox.setX(x);
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry()) || this.checkCollisionWithExit(GameScreen.getExits())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setX(x);
                }
                break;
            case UP:
                y += 0.5F;
                boundingBox.setY(y);
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry()) || this.checkCollisionWithExit(GameScreen.getExits())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setY(y);
                }
                break;
            case DOWN:
                y -= 0.5F;
                boundingBox.setY(y);
                if (this.checkCollisionWithWall(GameScreen.getWalls()) || this.checkCollisionWIthEntry(GameScreen.getEntry()) || this.checkCollisionWithExit(GameScreen.getExits())) {
                    x = previousX;
                    y = previousY;
                    boundingBox.setY(y);
                }
                break;
        }
    }

    /**
     * Checks if the enemy collided with a wall.
     *
     * @param walls Used to get bounding box of walls.
     * @return Return true if enemy collides with wall.
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
     * Checks if the enemy collided with an entry.
     *
     * @param entry Used to get bounding box of entry.
     * @return Return true if enemy collides with entry.
     */
    public boolean checkCollisionWIthEntry(Entry entry) {

        if (boundingBox.overlaps(entry.getBoundingBox())) {
            return true;
        }
        return false;
    }

    /**
     * Gets the animation according to the direction enemy is moving.
     *
     * @return Current frame of the Enemy according to his direction.
     */
    public TextureRegion getCurrentFrame() {
        switch (currentDirection) {
            case LEFT:
                return animationLeft.getKeyFrame(currentMoveTime, true);
            case UP:
                return animationUp.getKeyFrame(currentMoveTime, true);
            case DOWN:
                return animationDown.getKeyFrame(currentMoveTime, true);
            case RIGHT:
                return animationRight.getKeyFrame(currentMoveTime, true);
            default:
                return animationDown.getKeyFrame(currentMoveTime, true);
        }
    }

    /**
     * Checks if the enemy collided with an exit.
     *
     * @param exits Used to get bounding box of exit.
     * @return Return true if enemy collides with an exit.
     */
    public boolean checkCollisionWithExit(Array<Exit> exits) {
        for (Exit exit : exits) {
            if (boundingBox.overlaps(exit.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Randomizes in what direction enemy is going to move using random number generator.
     */
    private void move() {
        actionLockCounter++;

        if (allowedMove) {
            Random random1 = new Random();
            int i = random1.nextInt(100) + 1;
            if (i < 25) {
                currentDirection = Direction.LEFT;
            }
            if (i > 25 && i <= 50) {
                currentDirection = Direction.RIGHT;
            }
            if (i > 50 && i <= 75) {
                currentDirection = Direction.UP;
            }
            if (i > 75 && i <= 100) {
                currentDirection = Direction.DOWN;

            }
            currentMoveTime = moveableDuration;
            allowedMove = false;
        }
        actionLockCounter = 0;
    }


    @Override
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
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
}
