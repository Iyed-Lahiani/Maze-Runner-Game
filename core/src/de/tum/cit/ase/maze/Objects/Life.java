package de.tum.cit.ase.maze.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.Changeable;
import de.tum.cit.ase.maze.GameObject;

/**
 * The Life class is responsible for setting the texture to the extra life object.
 * It extends the GameObject class.
 */
public class Life extends GameObject implements Changeable {
    private static TextureRegion lifeFront = new TextureRegion(new Texture(Gdx.files.internal("assets/objects.png")), 0, 49, 16, 16);
    private static TextureRegion lifeTurning1 = new TextureRegion(new Texture(Gdx.files.internal("assets/objects.png")), 15, 49, 16, 16);
    private static TextureRegion lifeTurning2 = new TextureRegion(new Texture(Gdx.files.internal("assets/objects.png")), 33, 49, 16, 16);
    private static TextureRegion lifeTurning3 = new TextureRegion(new Texture(Gdx.files.internal("assets/objects.png")), 47, 49, 16, 16);
    private TextureRegion[] animationList = {lifeFront, lifeTurning1, lifeTurning2, lifeTurning3};
    Animation<TextureRegion> lifeAnimation = new Animation<>(0.27f, animationList);
    private float stateTime;

    /**
     * Constructor of the Life class.
     *
     * @param texture Used to load the texture of extra lives.
     * @param x       Used for the x position of the shield in game.
     * @param y       Used for the y position of the shield in game.
     */
    public Life(TextureRegion texture, float x, float y) {
        super(texture, x, y, 16, 16);
        stateTime = 0;
    }

    /**
     * Change the texture of the heart.
     */
    @Override
    public void changeTexture() {
        this.setTexture(lifeAnimation.getKeyFrame(stateTime, true));
        stateTime += Gdx.graphics.getDeltaTime();
    }
}
