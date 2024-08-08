package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * The GameObject class is responsible for setting the textures and bounding boxes of elements.
 * It handles the graphics of objects.
 */
public class GameObject {
    private TextureRegion texture;
    private Rectangle boundingBox;

    /**
     * Constructor for GameObject.
     *
     * @param texture The texture element is supposed to load.
     * @param x       coordinate x for bounding box.
     * @param y       coordinate y for bounding box.
     * @param width   width for the bounding box.
     * @param height  height for the bounding box.
     */
    public GameObject(TextureRegion texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.boundingBox = new Rectangle(x, y, width, height);
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }


}
