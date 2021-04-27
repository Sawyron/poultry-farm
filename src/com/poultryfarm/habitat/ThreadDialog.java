package com.poultryfarm.habitat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThreadDialog extends JDialog {
    private Habitat habitat;
    private Thread updateThread;
    private Thread paintThread;
    private Thread adultBirdAIThread;
    private final Warden WARDEN;
    private JPanel mainPanel = new JPanel();
    private JComboBox<Integer> updateThreadComboBox = new JComboBox();
    private JComboBox<Integer> paintThreadComboBox = new JComboBox();
    private JComboBox<Integer> adultBirdAIThreadComboBox = new JComboBox();
    private JComboBox<Integer> nestlingAIThreadComboBox = new JComboBox();
    private int updateThreadPriority;
    private int paintThreadPriority;
    private int adultBirdAIThreadPriority;
    private int nestlingAIThreadPriority;
    private JButton submitButton = new JButton("Принять");

    ThreadDialog(Habitat habitat) {
        this.habitat = habitat;
        WARDEN = habitat.getWARDEN();
        this.setTitle("Управление потоками");
        this.setLayout(new BorderLayout());

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        for (int i = 1; i < 11; i++) {
            updateThreadComboBox.addItem(i);
            paintThreadComboBox.addItem(i);
            adultBirdAIThreadComboBox.addItem(i);
            nestlingAIThreadComboBox.addItem(i);
        }
        updateThreadPriority = habitat.getUpdateThreadPriority();
        paintThreadPriority = habitat.getPaintThreadPriority();
        adultBirdAIThreadPriority = habitat.getAdultBirdAIThreadPriority();
        nestlingAIThreadPriority = habitat.getNestlingAIThreadPriority();
        updateThreadComboBox.setSelectedIndex(updateThreadPriority - 1);
        paintThreadComboBox.setSelectedIndex(paintThreadPriority - 1);
        adultBirdAIThreadComboBox.setSelectedIndex(adultBirdAIThreadPriority - 1);
        nestlingAIThreadComboBox.setSelectedIndex(nestlingAIThreadPriority - 1);
        addListeners();
        Component[] commonThreadComboBoxes = {updateThreadComboBox, paintThreadComboBox};
        String[] commonThreadLabelTexts = {"Поток обновления", "Поток отрисовки"};
        for (int i = 0; i < commonThreadComboBoxes.length; i++) {
            commonThreadComboBoxes[i].setFocusable(false);
            JPanel subPanel = new JPanel();
            JLabel label = new JLabel(commonThreadLabelTexts[i]);
            subPanel.add(commonThreadComboBoxes[i]);
            subPanel.add(label);
            label.setLabelFor(commonThreadComboBoxes[i]);
            mainPanel.add(subPanel);
            mainPanel.add(Box.createVerticalGlue());
        }
        addAIThreadComponents();
        this.add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void addAIThreadComponents() {
        Component[] AIThreadComboBoxes = {adultBirdAIThreadComboBox, nestlingAIThreadComboBox};
        String[] AIThreadLabelTexts = {"Поток движения взрослых птиц", "Поток движения птенцов"};
        ButtonGroup adultBirdAIButtonGroup = new ButtonGroup();
        ButtonGroup nestlingAIButtonGroup = new ButtonGroup();
        ButtonGroup[] buttonGroups = {adultBirdAIButtonGroup, nestlingAIButtonGroup};
        ActionListener adultBirdAIActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WARDEN.setAdultBirdAIActive(!WARDEN.isAdultBirdAIActive());
            }
        };
        ActionListener nestlingAIActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WARDEN.setNestlingAIActive(!WARDEN.isNestlingAIActive());
            }
        };
        ActionListener[] actionListeners = {adultBirdAIActionListener, nestlingAIActionListener};
        boolean[] statuses = {WARDEN.isAdultBirdAIActive(), WARDEN.isNestlingAIActive()};
        for (int i = 0; i < AIThreadComboBoxes.length; i++) {
            AIThreadComboBoxes[i].setFocusable(false);
            JPanel subPanel = new JPanel();
            JLabel label = new JLabel(AIThreadLabelTexts[i]);
            subPanel.add(AIThreadComboBoxes[i]);
            subPanel.add(label);
            label.setLabelFor(AIThreadComboBoxes[i]);
            JRadioButton onButton = new JRadioButton("on");
            JRadioButton offButton = new JRadioButton("off");
            onButton.setFocusable(false);
            offButton.setFocusable(false);
            buttonGroups[i].add(onButton);
            buttonGroups[i].add(offButton);
            if (statuses[i]) onButton.setSelected(true);
            else offButton.setSelected(true);
            onButton.addActionListener(actionListeners[i]);
            offButton.addActionListener(actionListeners[i]);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(onButton);
            buttonPanel.add(offButton);
            subPanel.add(buttonPanel);
            mainPanel.add(subPanel);
        }
    }


    private void addListeners() {
        updateThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setUpdateThreadPriority(updateThreadComboBox.getSelectedIndex() + 1);
            }
        });
        paintThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setPaintThreadPriority(paintThreadComboBox.getSelectedIndex() + 1);
            }
        });
        adultBirdAIThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setAdultBirdAIThreadPriority(adultBirdAIThreadComboBox.getSelectedIndex() + 1);
            }
        });
        nestlingAIThreadComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setNestlingAIThreadPriority(nestlingAIThreadComboBox.getSelectedIndex() + 1);
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setUpdateThreadPriority(updateThreadPriority);
                habitat.setAdultBirdAIThreadPriority(adultBirdAIThreadPriority);
                habitat.setPaintThreadPriority(paintThreadPriority);
                habitat.setNestlingAIThreadPriority(nestlingAIThreadPriority);
            }
        });
    }
}
