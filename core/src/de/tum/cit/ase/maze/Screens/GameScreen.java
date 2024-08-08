package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.*;
import de.tum.cit.ase.maze.Objects.*;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private static Music backgroundGame;
    private Maze maze;
    private Sprite bgSprite;
    private SpriteBatch spriteBatch;
    //  private static TextureRegion UNLOCKED_DOOR = new TextureRegion(new Texture(Gdx.files.internal("assets/things.png")), 0, 32, 16, 16);
    private static TextureRegion HEART = new TextureRegion(new Texture(Gdx.files.internal("assets/objects.png")), 64, 0, 16, 16);
    private static TextureRegion LIFE = new TextureRegion(new Texture(Gdx.files.internal("assets/objects.png")), 0, 49, 16, 16);
    private static TextureRegion KEY = new TextureRegion(new Texture(Gdx.files.internal("assets/key_no_bg-2.png")), 0, 0, 16, 16);
    private static TextureRegion GREY_LIGHTNING = new TextureRegion(new Texture(Gdx.files.internal("assets/93-931402_lighting-bolt-pixel-art-hd-png-download-2_16x16.png")), 0, 0, 16, 16);
    private static Texture shield = new Texture(Gdx.files.internal("assets/shield.png"));

    private static TextureRegion BLUE_LIGHTNING = new TextureRegion(new Texture(Gdx.files.internal("assets/93-931402_lighting-bolt-pixel-art-hd-png-download_16x16.png")), 0, 0, 16, 16);
    private static Array<Wall> walls;
    private static Array<Exit> exits;
    private static Array<Key> keys;
    private static Array<Trap> traps;
    private static Array<Path> paths;
    private static Array<Enemy> enemies;
    private static Array<Life> lives;
    private static Array<Protection> protections;
    private static Entry entry;
    private Player player;
    private boolean isPaused = false;
    private TextButton menuButton;
    private Stage stage;
    private Vector3 originalCameraPosition = new Vector3();


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     * Create the maze
     *
     * @param game  The main game class, used to access global resources and methods.
     * @param level Used to determine which map to load.
     * @param name  Name of the player
     */
    public GameScreen(MazeRunnerGame game, int level, String name) {
        this.game = game;
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.zoom = 0.25f;
        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch());

        spriteBatch=new SpriteBatch();
        bgSprite = new Sprite(new Texture("assets/Unknown-2.jpg"));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Table table = new Table(); // Create a table for layout
        stage.addActor(table);
        table.add(new Label("Game is paused", game.getSkin(), "title")).padBottom(80).row();
        menuButton = new TextButton("Go To Menu", game.getSkin());
        table.add(menuButton).width(300).row();
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backgroundGame.stop();
                MazeRunnerGame.getButtonPressed().play(0.08f);
                game.goToMenu();
            }
        });


        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        // Initialize maze object
        maze = new Maze(level);
        // Initialize the player with the position of the entry point from the maze
        player = new Player(maze.getEntryX(), maze.getEntryY(), name, this.game, maze);

        // Play some background music
        // Background sound
        backgroundGame = Gdx.audio.newMusic(Gdx.files.internal("assets/Soundtrack/BackgroundGame.mp3"));
        backgroundGame.setLooping(true);
        backgroundGame.setVolume(0.02f);
        backgroundGame.play();

        // Initialize obstacles and enemies lists
        walls = new Array<>();
        exits = new Array<>();
        traps = new Array<>();
        keys = new Array<>();
        enemies = new Array<>();
        paths = new Array<>();
        lives = new Array<>();
        protections = new Array<>();
        game.getBackgroundMusic().stop();
        buildMaze();


    }

    /**
     * Screen interface methods with necessary functionality
     *
     * @param delta Used to determine when to update player and enemy.
     */
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }
        if (!isPaused) {
            // Store the original camera settings
            originalCameraPosition.set(camera.position);

        } else {
            // Restore the original camera settings
            camera.position.set(originalCameraPosition);

        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera.update(); // Update the camera

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.startMoving(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.startMoving(Direction.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.startMoving(Direction.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.startMoving(Direction.DOWN);
        } else {
            player.stopMoving();
        }
        stage.getViewport().setCamera(camera);

        if (!isPaused) {
            camera.zoom = 0.25f;

            float secondCamWidth = w / 11;
            float secondCamHeight = h / 11;
            if (((player.getX() > camera.position.x + (secondCamWidth)))) {
                camera.position.set(player.getX(), player.getY(), 0);
            }
            if ((player.getY() > camera.position.y + (secondCamHeight))) {
                camera.position.set(player.getX(), player.getY(), 0);
            }
            if (((player.getX() < camera.position.x - (secondCamWidth)) || (player.getY() < camera.position.y - (secondCamHeight)))) {
                camera.position.set(player.getX(), player.getY(), 0);
            }
            camera.update();

            player.update(delta);

            for (Enemy enemy : enemies) {
                enemy.update(delta);
            }
            // Set up and begin drawing with the sprite batch
            game.getSpriteBatch().setProjectionMatrix(camera.combined);

            game.getSpriteBatch().begin(); // Important to call this before drawing anything
            drawMaze();
            drawEnemy();
            drawPlayer();
            drawHeart();
            drawKey();
            drawLight();
            drawShield();
            game.getSpriteBatch().end();// Important to call this after drawing everything

        }
        if (isPaused) {
            camera.zoom = 1.5F;
            camera.position.set(w / 100, h / 100, 0); // Center the camera

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
            spriteBatch.begin();
            bgSprite.draw(spriteBatch);
            spriteBatch.end();
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
            stage.draw(); // Draw the stage


        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Reads from map properties to determine the location of elements inside the maze.
     */
    public void buildMaze() {
        int[][] mazeArray = maze.getMaze();
        for (int row = 0; row < mazeArray.length; row++) {
            for (int col = 0; col < mazeArray[row].length; col++) {
                float x = col * 16;  // Multiply by 16 to match the cell size in the game
                float y = row * 16;  // Multiply by 16 to match the cell size in the game
                //Draw each tile according to the assigned value in the maze object
                if (mazeArray[row][col] == 0) {
                    walls.add(new Wall(x, y));
                } else if (mazeArray[row][col] == 1) {
                    entry = new Entry(x, y, player);
                } else if (mazeArray[row][col] == 2) {
                    exits.add(new Exit(x, y));
                } else if (mazeArray[row][col] == 3) {
                    paths.add(new Path(x,y));
                    traps.add(new Trap(x, y));
                } else if (mazeArray[row][col] == 4) {
                    paths.add(new Path(x, y));
                    enemies.add(new Enemy(x, y, "Ghost"));
                } else if (mazeArray[row][col] == 5) {
                    keys.add(new Key(x, y));
                } else if (mazeArray[row][col] == 8) {
                    paths.add(new Path(x, y));
                    lives.add(new Life(LIFE, x, y));
                } else if (mazeArray[row][col] == 9) {
                    paths.add(new Path(x, y));
                    protections.add(new Protection(x, y));
                } else {
                    paths.add(new Path(x, y));
                }
            }
        }
    }

    /**
     * Draws the textures for previously read elements of the maze.
     */
    public void drawMaze() {
        for (Wall wall : walls) {
            if (isInFrustum(wall.getBoundingBox().getX(), wall.getBoundingBox().getY())) {
                game.getSpriteBatch().draw(wall.getTexture(), wall.getBoundingBox().getX(), wall.getBoundingBox().getY());
            }
        }
        for (Path path : paths) {
            if (isInFrustum(path.getBoundingBox().getX(), path.getBoundingBox().getY())) {
                game.getSpriteBatch().draw(path.getTexture(), path.getBoundingBox().getX(), path.getBoundingBox().getY());
            }
        }
        for (Trap trap : traps) {
            trap.changeTexture();
            if (isInFrustum(trap.getBoundingBox().getX(), trap.getBoundingBox().getY())) {
                game.getSpriteBatch().draw(trap.getTexture(), trap.getBoundingBox().getX(), trap.getBoundingBox().getY());
            }
        }
        for (Exit exit : exits) {
            if (player.getKey() != null) {
                exit.setTexture(Exit.getUnlockedDoor());
            }
            game.getSpriteBatch().draw(exit.getTexture(), exit.getBoundingBox().getX(), exit.getBoundingBox().getY());
        }
        for (Key key : keys) {
            if (player.getKey() != null && player.getKey().equals(key)) {
                key.setTexture(paths.first().getTexture());
            }
            game.getSpriteBatch().draw(key.getTexture(), key.getBoundingBox().getX(), key.getBoundingBox().getY());
        }
        for (Life life : lives) {
            life.changeTexture();
            if (isInFrustum(life.getBoundingBox().getX(), life.getBoundingBox().getY())) {
                game.getSpriteBatch().draw(life.getTexture(), life.getBoundingBox().getX(), life.getBoundingBox().getY());
            }
        }
        for (Protection protection : protections) {
            if (isInFrustum(protection.getBoundingBox().getX(), protection.getBoundingBox().getY())) {
                game.getSpriteBatch().draw(protection.getTexture(), protection.getBoundingBox().getX(), protection.getBoundingBox().getY());
            }
        }
        if (isInFrustum(entry.getX(), entry.getY())) {
            entry.changeTexture();
            game.getSpriteBatch().draw(entry.getTexture(), entry.getBoundingBox().getX(), entry.getBoundingBox().getY());
        }
    }

    /**
     * Draws the player on the screen.
     */
    public void drawPlayer() {
        if (isInFrustum(player.getX(), player.getY())) {
            TextureRegion currentFrame = player.getCurrentFrame();
            game.getSpriteBatch().draw(currentFrame, player.getX(), player.getY()); //Draw current frame in the player's position
        }
    }

    /**
     * Draws the enemies if they are in visible range from player.
     */
    public void drawEnemy() {
        for (Enemy enemy : enemies) {
            if (isInFrustum(enemy.getX(), enemy.getY())) {
                TextureRegion currentFrame = enemy.getCurrentFrame();
                game.getSpriteBatch().draw(currentFrame, enemy.getX(), enemy.getY());
            }
        }
    }

    /**
     * Draws the hearts depending on how many lives the player has in the HUD.
     */
    public void drawHeart() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float secondCam = w / 12;
        float secondCamH = h / 13;

        if (player.getLivesRemaining() == 3) {
            game.getSpriteBatch().draw(HEART, camera.position.x - secondCam - 10, camera.position.y + secondCamH);
            game.getSpriteBatch().draw(HEART, camera.position.x - secondCam + 10, camera.position.y + secondCamH);
            game.getSpriteBatch().draw(HEART, camera.position.x - secondCam + 30, camera.position.y + secondCamH);
        }
        if (player.getLivesRemaining() == 2) {
            game.getSpriteBatch().draw(HEART, camera.position.x - secondCam - 10, camera.position.y + secondCamH);
            game.getSpriteBatch().draw(HEART, camera.position.x - secondCam + 10, camera.position.y + secondCamH);
        }
        if (player.getLivesRemaining() == 1) {
            game.getSpriteBatch().draw(HEART, camera.position.x - secondCam - 10, camera.position.y + secondCamH);
        }
        if (player.getLivesRemaining() == 0) {
            game.goToLose(maze.getLevel());
        }
    }

    /**
     * Draws the key in the HUD if the player picked it up.
     */
    public void drawKey() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float secondCam = w / 12;
        float secondCamH = h / 13;

        if (player.getKey() != null) {
            game.getSpriteBatch().draw(KEY, camera.position.x - secondCam + 10, camera.position.y + secondCamH - 20);
        }
    }

    /**
     * Draws the extra speed object in HUD.
     */
    public void drawLight() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float secondCam = w / 12;
        float secondCamH = h / 13;

        if (player.getCurrentCooldownTime() == 0) {
            game.getSpriteBatch().draw(BLUE_LIGHTNING, camera.position.x - secondCam - 10, camera.position.y + secondCamH - 20);

        } else {
            if (player.getCurrentCooldownTime() != 0) {
                game.getSpriteBatch().draw(GREY_LIGHTNING, camera.position.x - secondCam - 10, camera.position.y + secondCamH - 20);
            }

        }
    }

    /**
     * Draws the shield object in HUD.
     */
    public void drawShield() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float secondCam = w / 12;
        float secondCamH = h / 13;

        if (player.isProtected()) {
            game.getSpriteBatch().draw(shield, camera.position.x - secondCam + 30, camera.position.y + secondCamH - 20);
        }
    }

    /**
     * Checks to see if a position is inside the camera's frustum.
     *
     * @param x coordinate for row
     * @param y coordinate for height
     * @return The location of the camera's frustum.
     */
    private boolean isInFrustum(float x, float y) {
        return camera.frustum.pointInFrustum(x, y, 0);
    }


    public static Array<Wall> getWalls() {
        return walls;
    }

    public static Array<Exit> getExits() {
        return exits;
    }

    public static Array<Key> getKeys() {
        return keys;
    }

    public static Array<Trap> getTraps() {
        return traps;
    }

    public static Array<Enemy> getEnemies() {
        return enemies;
    }

    public static Entry getEntry() {
        return entry;
    }

    public static TextureRegion getHEART() {
        return HEART;
    }

    public static Array<Life> getLives() {
        return lives;
    }

    public static Array<Protection> getProtections() {
        return protections;
    }

    public static Music getBackgroundGame() {
        return backgroundGame;
    }
}
