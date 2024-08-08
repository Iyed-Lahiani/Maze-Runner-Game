package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Screens.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private Music backgroundMusic;
    private static Sound gameOver;
    private static Sound buttonPressed;
    private static Sound win;
    private SpriteBatch spriteBatch;
    private Skin skin;
    private Animation<TextureRegion> characterDownAnimation;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        //Initialize sounds
        gameOver = Gdx.audio.newSound((Gdx.files.internal("assets/Soundtrack/gameOver.wav")));
        buttonPressed = Gdx.audio.newSound((Gdx.files.internal("assets/Soundtrack/Button pressed.mp3")));
        win = Gdx.audio.newSound((Gdx.files.internal("assets/Soundtrack/winsquare-6993.mp3")));

        // Play some background music
        // Background sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }


    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        gameOver.stop();
        backgroundMusic.play();
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this, 1,MenuScreen.getPlayerName())); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Switches to the maps screen.
     */
    public void goToMaps() {
        this.setScreen(new MapScreen(this));
        if (menuScreen != null) {
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    /**
     * Switches to the win screen.
     * @param level Used to determine the game level.
     */
    public void goToWin(int level) {
        backgroundMusic.stop();
        GameScreen.getBackgroundGame().stop();
        this.setScreen(new YouWonScreen(this, level));
        win.play(0.5f);
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    /**
     * Switches to the loss screen.
     * @param level Used to determine the game level.
     */
    public void goToLose(int level){
        backgroundMusic.stop();
        GameScreen.getBackgroundGame().stop();
        this.setScreen(new GameOverScreen(this,level));
        gameOver.play(0.08f);
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }
    

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    public Skin getSkin() {
        return skin;
    }
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public static Sound getButtonPressed() {
        return buttonPressed;
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public static Sound getGameOver() {
        return gameOver;
    }

    public static Sound getWin() {
        return win;
    }
}
