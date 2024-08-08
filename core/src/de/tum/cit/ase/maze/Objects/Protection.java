package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.GameObject;
/**
 * The Protection class is responsible for setting the texture to the shield object.
 * It extends the GameObject class.
 */
public class Protection extends GameObject {
    /**
     * Constructor for Protection class.
     * @param x Used for the x position of the shield in game.
     * @param y Used for the y position of the shield in game.
     */
    public Protection(float x, float y) {
        super(new TextureRegion(new Texture(Gdx.files.internal("assets/shield.png"))), x, y, 16, 16);
    }
}
