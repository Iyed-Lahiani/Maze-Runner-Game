package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.Changeable;
import de.tum.cit.ase.maze.GameObject;

/**
 * The Key class is responsible for setting the texture to the Key object.
 * It extends the GameObject class and implements the Changeable interface.
 */
public class Key extends GameObject{
    private boolean playerPickedUp;

    /**
     * Constructor for Key class.
     *
     * @param x Coordinate x where to draw the key.
     * @param y Coordinate y where to draw the key.
     */
    public Key(float x, float y) {
        super(new TextureRegion(new Texture(Gdx.files.internal("assets/Key with bg.png"))), x, y, 16, 16);
        playerPickedUp = false;
    }


    public boolean isPlayerPickedUp() {
        return playerPickedUp;
    }

    /**
     * Sets the playerPickedUp boolean to the one of the players.
     *
     * @param playerPickedUp Boolean value used in player class.
     */
    public void setPlayerPickedUp(boolean playerPickedUp) {
        this.playerPickedUp = playerPickedUp;
    }
}
