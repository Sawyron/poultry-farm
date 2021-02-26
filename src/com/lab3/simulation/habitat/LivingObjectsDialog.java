package com.lab3.simulation.habitat;

import com.lab3.simulation.Bird;

import javax.swing.*;
import java.util.Map;

public class LivingObjectsDialog extends JDialog {
    private Habitat habitat;

    public LivingObjectsDialog(Habitat habitat) {
        this.habitat = habitat;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        this.add(mainPanel);
        StringBuilder msg = new StringBuilder();
        Map<Bird, Long> livingObjects = habitat.getLivingObjects();
        mainPanel.add(new JLabel("Живые объекты (" + livingObjects.size() + "):", SwingConstants.CENTER));
        livingObjects.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(x -> mainPanel.add(new JLabel(x.getKey().toString() + " Время рождения: " + x.getValue() / 1000.0)));
        if (livingObjects.isEmpty()) setSize(200, 300);
        else pack();
    }
}
