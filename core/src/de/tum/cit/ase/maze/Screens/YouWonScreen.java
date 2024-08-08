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
 * The YouWonScreen class is responsible for displaying the win screen of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the won game.
 */
public class YouWonScreen implements Screen {
    private final Stage stage;
    private Sprite bgSprite;
    private SpriteBatch spriteBatch;

    /**
     * Constructor for YouWonScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game  The main game class, used to access global resources and methods.
     * @param level Used to determine game level.
     */

    public YouWonScreen(MazeRunnerGame game, int level) {


        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        spriteBatch = new SpriteBatch();
        bgSprite = new Sprite(new Texture("assets/Win BG.png"));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!MenuScreen.getPlayerName().equals("Player")) {
            table.add(new Label("Congrats " + MenuScreen.getPlayerName() + "! You won!", game.getSkin(), "title")).padBottom(80).row();
        } else {
            table.add(new Label("Congrats! You won!", game.getSkin(), "title")).padBottom(80).row();
        }
        TextButton goToMenuButton = new TextButton("Go to Menu", game.getSkin());
        TextButton nextLevel = new TextButton("Next level", game.getSkin());
        if (level < MapScreen.getPaths().size) {
            table.add(new Label(MenuScreen.getPlayerName() + " advances to level " + (level + 1) + "!", game.getSkin(), "bold")).padBottom(50).row();
        } else {
            table.add(new Label(MenuScreen.getPlayerName() + " completed the game!", game.getSkin(), "bold")).padBottom(50).row();
        }
        table.add(nextLevel).width(300).row();
        table.add(goToMenuButton).width(300).row();
        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                MazeRunnerGame.getButtonPressed().play(0.08f);
                game.goToMenu();
            }
        });
        nextLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                MazeRunnerGame.getWin().stop();
                MazeRunnerGame.getButtonPressed().play(0.08f);
                int newLevel = 0;
                if (level < MapScreen.getPaths().size) {
                    newLevel = level + 1;
                } else {
                    newLevel = 1;
                }
                game.setScreen(new GameScreen(game, newLevel, MenuScreen.getPlayerName()));
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0.5f, 0, 1);

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
}
