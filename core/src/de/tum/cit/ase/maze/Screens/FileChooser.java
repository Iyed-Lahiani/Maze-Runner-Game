package de.tum.cit.ase.maze.Screens;

import java.awt.Desktop;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {
    public static boolean OpenFileE() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Map files", "properties");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("."));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());


            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return false;
    }

}
