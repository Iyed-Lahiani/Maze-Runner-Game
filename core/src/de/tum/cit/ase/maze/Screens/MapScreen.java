package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;

import java.io.IOException;
import java.util.Arrays;

/**
 * The MapScreen class is responsible for displaying the map loader screen of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the map loader.
 */
public class MapScreen implements Screen {
    private final Stage stage;
    private static Array<String> paths;
    private Array<TextButton> levelButtons;
    private Sprite bgSprite;
    private SpriteBatch spriteBatch;

    /**
     * Constructor for MapScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MapScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        paths=new Array<>();
        levelButtons=new Array<>();

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        spriteBatch = new SpriteBatch();
        bgSprite = new Sprite(new Texture("assets/Unknown-2.jpg"));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage


        // Add a label as a title
        table.add(new Label("Choose a Level", game.getSkin(), "title")).padBottom(80).row();
        FileHandle[] mapFiles = Gdx.files.internal("maps").list();
        Arrays.sort(mapFiles,(file1,file2) -> file1.name().compareToIgnoreCase(file2.name()));
        for (FileHandle file :mapFiles){
            if(!file.isDirectory() && file.extension().equalsIgnoreCase("properties")){
                paths.add(file.path());
            }
        }
        for (String path : paths){
            levelButtons.add(new TextButton("Level "+(paths.indexOf(path,false)+1),game.getSkin()));
        }
        for (TextButton button : levelButtons){
            table.add(button).width(300).row();
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    game.setScreen(new GameScreen(game, levelButtons.indexOf(button,false)+1,MenuScreen.getPlayerName()));
                    MazeRunnerGame.getButtonPressed().play(0.08f);
                }
            });
        }


        TextButton goToMenuButton = new TextButton("Go To Menu", game.getSkin());
        table.add(goToMenuButton).width(300).padTop(20).row();
        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MazeRunnerGame.getButtonPressed().play(0.08f);
                game.goToMenu();
            }
        });

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        spriteBatch.begin();
        bgSprite.draw(spriteBatch);
        spriteBatch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
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

    public static Array<String> getPaths() {
        return paths;
    }
}
