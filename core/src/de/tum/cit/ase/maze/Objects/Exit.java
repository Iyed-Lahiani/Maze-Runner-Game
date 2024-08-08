package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.Changeable;
import de.tum.cit.ase.maze.GameObject;

/**
 * The Exit class is responsible for setting the texture to the exit object.
 * It extends the GameObject class and implements the Changeable interface.
 */
public class Exit extends GameObject{
    private static TextureRegion LOCKED_DOOR = new TextureRegion(new Texture(Gdx.files.internal("assets/things.png")), 0, 0, 16, 16);
    private static TextureRegion UNLOCKED_DOOR = new TextureRegion(new Texture(Gdx.files.internal("assets/things.png")), 0, 32, 16, 16);

    /**
     * Constructor for Exit class.
     *
     * @param x Coordinate x where to draw the exit.
     * @param y Coordinate y where to draw the exit.
     */
    public Exit(float x, float y) {
        super(LOCKED_DOOR, x, y, 16, 16);
    }

    public static TextureRegion getLockedDoor() {
        return LOCKED_DOOR;
    }

    public static TextureRegion getUnlockedDoor() {
        return UNLOCKED_DOOR;
    }
}
