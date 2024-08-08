package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.Changeable;
import de.tum.cit.ase.maze.GameObject;
import de.tum.cit.ase.maze.Player;

/**
 * The Entry class is responsible for setting the texture to the entry object.
 * It extends the GameObject class and implements the Changeable interface.
 */
public class Entry extends GameObject implements Changeable {
    private Player player;
    private float x;
    private float y;
    private static TextureRegion WALL = new TextureRegion(new Texture(Gdx.files.internal("assets/basictiles.png")), 0, 0, 16, 16);
    private static TextureRegion BLACK = new TextureRegion(new Texture(Gdx.files.internal("assets/basictiles.png")), 96, 32, 16, 16);
    private final Sound closedDoor;
    private boolean changed;


    /**
     * Constructor for Entry class.
     *
     * @param x      Coordinate x where to draw the entry.
     * @param y      Coordinate y where to draw the entry.
     * @param player Initializes the player.
     */
    public Entry(float x, float y, Player player) {
        super(BLACK, x, y, 16, 16);
        this.player = player;
        this.x = x;
        this.y = y;
        changed = false;
        closedDoor = Gdx.audio.newSound(Gdx.files.internal("assets/Soundtrack/doorslam-90782.mp3"));
    }

    /**
     * Implemented method from Changeable interface.
     * Changes the texture of the entry if the player's location is not the same as entries.
     * Plays a sound after changing the texture.
     */
    public void changeTexture() {
        if (Math.abs(player.getX() - x) >= 16 || Math.abs(player.getY() - y) >= 16) {
            setTexture(WALL);
            if (!changed) {
                closedDoor.play(0.5f);
                changed = true;
            }
        }
    }

    //Getters
    public static TextureRegion getWALL() {
        return WALL;
    }

    public static TextureRegion getBLACK() {
        return BLACK;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Sound getClosedDoor() {
        return closedDoor;
    }
}
