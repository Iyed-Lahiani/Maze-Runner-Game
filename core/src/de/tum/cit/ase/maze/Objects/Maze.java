package de.tum.cit.ase.maze.Objects;

import de.tum.cit.ase.maze.Screens.MapScreen;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * The Maze class is responsible fot the logic behind choosing which map to load.
 * It also determines the entry position and the maximum x, y coordinates.
 */
public class Maze {
    private int[][] maze;
    //Position of the entry
    private float entryX;
    private float entryY;
    //Size of the maze
    private int xMax;
    private int yMax;
    private int level;


    /**
     * Constructor of Maze. It creates a 2D Array of type int as a maze by calling the method loadLevel.
     *
     * @param level Used to determine what map to load.
     */
    public Maze(int level) {
        this.level = level;
        loadLevel(MapScreen.getPaths().get(level-1));
    }

    /**
     * It creates a 2D Array of type int as a maze.
     *
     * @param path Used to determine which level file to read.
     */
    private void loadLevel(String path) {
        Properties properties = new Properties();
        List<Integer> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();
        try {
            //read the level file
            InputStream levelFile = new FileInputStream(path);
            properties.load(levelFile);
            List<String> keys = properties.stringPropertyNames().stream().toList(); //List of all the keys
            keys.stream().forEach(key -> {
                xList.add(Integer.parseInt(key.split(",")[0])); //List of all x positions
                yList.add(Integer.parseInt(key.split(",")[1])); //List of all y positions
            });
            //Determine the maximal x and y
            xMax = Collections.max(xList);
            yMax = Collections.max(yList);
            //Create a maze using the maximal x and y
            int[][] mazeArray = new int[xMax + 1][yMax + 1];
            //Iterate through the maze and assign the value of each case
            for (int row = 0; row <= yMax; row++) {
                for (int col = 0; col <= xMax; col++) {
                    if (properties.containsKey(col + "," + row)) {
                        mazeArray[row][col] = Integer.parseInt(properties.getProperty(col + "," + row));

                        if (mazeArray[row][col] == 1) {
                            // Set entryX and entryY with the coordinates of the entry point

                            entryX = col * 16; // Assuming each cell is 16 units wide
                            entryY = row * 16; // Assuming each cell is 16 units high


                        }
                    } else {
                        mazeArray[row][col] = 7;
                    }
                }
            }
            this.maze = mazeArray; //Set the maze attribute to the 2D Array created object
        } catch (IOException e) {
            e.printStackTrace(); //Print the error in case of an Exception
        }
    }


    public int[][] getMaze() {
        return maze;
    }

    public float getEntryX() {
        System.out.println(entryX);
        return entryX;
    }

    public void setEntryX(float entryX) {
        this.entryX = entryX;
    }

    public float getEntryY() {
        return entryY;
    }

    public void setEntryY(float entryY) {
        this.entryY = entryY;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }
}
