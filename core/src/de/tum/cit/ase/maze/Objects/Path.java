package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.GameObject;

/**
 * The Path class is responsible for adding texture to the Path object of the maze.
 * It extends the GameObject class and uses its constructor.
 */
public class Path extends GameObject {

    /**
     * Constructor for Path class.
     *
     * @param x Coordinate x where to draw the path.
     * @param y Coordinate y where to draw the path.
     */
    public Path(float x, float y) {
        super(new TextureRegion(new Texture(Gdx.files.internal("assets/basictiles.png")), 0, 128, 16, 16), x, y, 16f, 16f);
    }
}
