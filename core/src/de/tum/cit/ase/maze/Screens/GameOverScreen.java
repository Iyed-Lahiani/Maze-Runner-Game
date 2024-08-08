package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * The GameOverScreen class is responsible for displaying the game over screen of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the game over screen.
 */
public class GameOverScreen implements Screen {
    private final Stage stage;
    private static String causeOfDeath;
    private Sprite bgSprite;
    private SpriteBatch spriteBatch;

    /**
     * Constructor for GameOverScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game  The main game class, used to access global resources and methods.
     * @param level The level of the game.
     */
    public GameOverScreen(MazeRunnerGame game, int level) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        spriteBatch = new SpriteBatch();
        bgSprite = new Sprite(new Texture("assets/Lose BG.png"));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(new Label("Game Over !", game.getSkin(), "title")).padBottom(80).row();
        table.add(new Label(MenuScreen.getPlayerName() + causeOfDeath, game.getSkin(), "bold")).padBottom(50).row();
        TextButton goToMenuButton = new TextButton("Go to Menu", game.getSkin());
        TextButton again = new TextButton("Play again", game.getSkin());
        table.add(again).width(300).row();
        table.add(goToMenuButton).width(300).row();
        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                MazeRunnerGame.getButtonPressed().play(0.08f);
                game.goToMenu();
            }
        });
        again.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                MazeRunnerGame.getGameOver().stop();
                MazeRunnerGame.getButtonPressed().play(0.08f);
                game.setScreen(new GameScreen(game, level, MenuScreen.getPlayerName()));
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.5f, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        spriteBatch.begin();
        bgSprite.draw(spriteBatch);
        spriteBatch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw();
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
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    //Getters & Setters
    public static String getCauseOfDeath() {
        return causeOfDeath;
    }

    public static void setCauseOfDeath(String causeOfDeath) {
        GameOverScreen.causeOfDeath = causeOfDeath;
    }
}
