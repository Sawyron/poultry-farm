package com.poultryfarm.habitat.services;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class OBJChooser extends JFileChooser {
    public OBJChooser() {
        super();
        setCurrentDirectory(new File("").getAbsoluteFile());
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getPath().endsWith(".obj") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "OBJ";
            }
        });
    }
}
