package com.lab3.simulation.habitat;

import com.lab3.simulation.birds.Bird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class ThreadDialog extends JDialog {
    private Habitat habitat;
    private Thread updateThread;
    private Thread paintThread;
    private Thread adultBirdAIThread;
    private final Warden WARDEN;
    private JComboBox<Integer> updateThreadComboBox = new JComboBox();
    private JComboBox<Integer> paintThreadComboBox = new JComboBox();
    private JComboBox<Integer> adultBirdAIThreadComboBox = new JComboBox();
    private int updateThreadPriority;
    private int paintThreadPriority;
    private int adultBirdAIThreadPriority;
    private JButton submitButton = new JButton("Принять");

    ThreadDialog(Habitat habitat) {
        this.habitat = habitat;
        WARDEN = habitat.getWARDEN();
        this.setTitle("Управление потоками");
        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        for (int i = 1; i < 11; i++) {
            updateThreadComboBox.addItem(i);
            paintThreadComboBox.addItem(i);
            adultBirdAIThreadComboBox.addItem(i);
        }
        updateThreadPriority = habitat.getUpdateThreadPriority();
        paintThreadPriority = habitat.getPaintThreadPriority();
        adultBirdAIThreadPriority = habitat.getAdultBirdAIThreadPriority();
        updateThreadComboBox.setSelectedIndex(updateThreadPriority - 1);
        paintThreadComboBox.setSelectedIndex(paintThreadPriority - 1);
        adultBirdAIThreadComboBox.setSelectedIndex(adultBirdAIThreadPriority - 1);
        addListeners();
        Component[] comboBoxes = {updateThreadComboBox, paintThreadComboBox, adultBirdAIThreadComboBox};
        String[] labelTexts = {"Поток обновления", "Поток отрисовки", "Поток интеллекта"};
        for (int i = 0; i < comboBoxes.length; i++) {
            JPanel subPanel = new JPanel();
            JLabel label = new JLabel(labelTexts[i]);
            subPanel.add(comboBoxes[i]);
            subPanel.add(label);
            label.setLabelFor(comboBoxes[i]);
            mainPanel.add(subPanel);
            mainPanel.add(Box.createVerticalGlue());
        }
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(submitButton, BorderLayout.SOUTH);
        this.pack();
    }


    private void addListeners() {
        updateThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateThreadPriority = updateThreadComboBox.getSelectedIndex() + 1;
            }
        });
        paintThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintThreadPriority = paintThreadComboBox.getSelectedIndex() + 1;
            }
        });
        adultBirdAIThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adultBirdAIThreadPriority = adultBirdAIThreadComboBox.getSelectedIndex() + 1;
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setUpdateThreadPriority(updateThreadPriority);
                habitat.setAdultBirdAIThreadPriority(adultBirdAIThreadPriority);
                habitat.setPaintThreadPriority(paintThreadPriority);
            }
        });
    }
}
