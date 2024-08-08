package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.Changeable;
import de.tum.cit.ase.maze.GameObject;

/**
 * The Trap class is responsible for adding texture to the Trap object of the maze.
 * It extends the GameObject class and uses its constructor.
 */
public class Trap extends GameObject implements Changeable {
    private static TextureRegion trap1 = new TextureRegion(new Texture(Gdx.files.internal("assets/things.png")),3,64,10,16);
    private static TextureRegion trap2 = new TextureRegion(new Texture(Gdx.files.internal("assets/things.png")),19,64,10,16);
    private static TextureRegion trap3 = new TextureRegion(new Texture(Gdx.files.internal("assets/things.png")),35,64,10,16);
    private TextureRegion[] animationList = {trap1,trap2,trap3};
    Animation<TextureRegion> trapAnimation = new Animation<>(0.18f, animationList);
    private float stateTime;

    /**
     * Constructor for Trap class.
     *
     * @param x Coordinate x where to draw the trap.
     * @param y Coordinate y where to draw the trap.
     */

    public Trap(float x, float y) {
        super(trap1, x, y, 10, 13);
    }

    /**
     * Change the texture of the trap.
     */
    @Override
    public void changeTexture() {
        this.setTexture(trapAnimation.getKeyFrame(stateTime, true));
        stateTime += Gdx.graphics.getDeltaTime();
    }
}
