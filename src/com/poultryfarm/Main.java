package com.poultryfarm;

import com.poultryfarm.habitat.Habitat;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Habitat h = new Habitat();
        h.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        h.setSize(800, 700);
        h.setLocationRelativeTo(null);
        h.setVisible(true);
    }
}
