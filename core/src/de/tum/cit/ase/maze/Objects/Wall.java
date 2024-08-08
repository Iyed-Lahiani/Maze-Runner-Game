package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.GameObject;

/**
 * The Wall class is responsible for adding texture to the Wall object of the maze.
 * It extends the GameObject class and uses its constructor.
 */
public class Wall extends GameObject {

    /**
     * Constructor for Wall class.
     *
     * @param x Coordinate x where to draw the wall.
     * @param y Coordinate y where to draw the wall.
     */
    public Wall(float x, float y) {
        super(new TextureRegion(new Texture(Gdx.files.internal("assets/basictiles.png")), 0, 0, 16, 16), x, y, 16f, 16f);
    }
}
