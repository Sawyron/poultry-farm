package com.lab3.simulation.habitat;

import com.lab3.simulation.Bird;
import com.lab3.simulation.IBehaviour;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ImagePanel extends JPanel {
    List<Bird> elements;

    ImagePanel() {

    }

    ImagePanel(List<Bird> lst) {
        this.elements = lst;
    }

    public void setElements(List<Bird> lst) {
        this.elements = lst;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (this.elements != null) {
            synchronized (elements) {
                for (IBehaviour b : elements) {
                    b.paint(g);
                }
            }
        }
    }
}
