package com.lab3.simulation.habitat;

import com.lab3.simulation.birds.AdultBird;
import com.lab3.simulation.birds.Nestling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private Habitat habitat = null;
    private JTextField adultBirdSpawnFrequencyTextField = new JTextField(6),
            nestlingSpawnFrequencyTextField = new JTextField(6),
            adultBirdLifeTimeTextField = new JTextField(6),
            nestlingLifeTimeTextField = new JTextField(6);
    private JComboBox<Integer> adultBirdSpawnProbabilityComboBox = new JComboBox();
    private JComboBox<Integer> nestlingMinFractionComboBox = new JComboBox();

    private SettingsDialog() {
        this.setTitle("Настройки");
        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


        adultBirdSpawnProbabilityComboBox.setFocusable(false);
        nestlingMinFractionComboBox.setFocusable(false);
        for (int i = 0; i < 11; i++) {
            adultBirdSpawnProbabilityComboBox.addItem(i * 10);
            nestlingMinFractionComboBox.addItem(i * 10);
        }
        adultBirdSpawnProbabilityComboBox.setSelectedIndex(5);
        nestlingMinFractionComboBox.setSelectedIndex(5);

        JLabel adultBirdSpawnFrequencyLabel = new JLabel("Частота появления взрослой птицы(мс): "),
                adultBirdSpawnProbabilityLabel = new JLabel("Вероятность появления взрослой птицы(%): "),
                nestlingSpawnFrequencyLabel = new JLabel("Частота появления птенца(мс): "),
                nestlingMinFractionTLabel = new JLabel("Минимальная доля птенцов(%): "),
                adultBirdLifeTimeLabel = new JLabel("Период жизни врослой птицы"),
                nestlingLifeTimeLabel = new JLabel("Период жизни птенца");

        JLabel[] labels = {adultBirdSpawnFrequencyLabel,
                adultBirdSpawnProbabilityLabel,
                nestlingSpawnFrequencyLabel,
                nestlingMinFractionTLabel,
                adultBirdLifeTimeLabel,
                nestlingLifeTimeLabel
        };


        Component[] textFields = {adultBirdSpawnFrequencyTextField,
                adultBirdSpawnProbabilityComboBox,
                nestlingSpawnFrequencyTextField,
                nestlingMinFractionComboBox,
                adultBirdLifeTimeTextField,
                nestlingLifeTimeTextField
        };
        for (int i = 0; i < labels.length; i++) {
            JPanel subPanel = new JPanel();
            JLabel label = labels[i];
            subPanel.add(label);
            Component component = textFields[i];
            label.setLabelFor(component);
            subPanel.add(component);
            mainPanel.add(subPanel);
            mainPanel.add(Box.createVerticalGlue());
        }
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.add(mainPanel, BorderLayout.CENTER);

        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adultBirdFrequencyText = adultBirdSpawnFrequencyTextField.getText();
                String nestlingFrequencyText = nestlingSpawnFrequencyTextField.getText();
                int adultBirdFrequency = 0;
                int nestlingFrequency = 0;
                boolean correctInputFlag = true;
                String errorMessage = "";

                try {
                    adultBirdFrequency = Integer.parseInt(adultBirdFrequencyText);
                } catch (NumberFormatException exception) {
                    correctInputFlag = false;
                    errorMessage += "\nНеверно введенна частота появления взрослой птицы";
                    adultBirdSpawnFrequencyTextField.setText(Integer.toString(habitat.getAdultBirdSpawnFrequency()));
                }
                try {
                    Integer adultBirdFrequencyComboBoxValue = (Integer) adultBirdSpawnProbabilityComboBox.getSelectedItem();
                    habitat.setAdultBirdSpawnProbability(adultBirdFrequencyComboBoxValue / 100.0);
                    adultBirdSpawnProbabilityComboBox.setSelectedIndex(5);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                try {
                    nestlingFrequency = Integer.parseInt(nestlingFrequencyText);
                } catch (NumberFormatException exception) {
                    correctInputFlag = false;
                    errorMessage += "\nНеверно введенна частота появления птенца";
                    nestlingSpawnFrequencyTextField.setText(Integer.toString(habitat.getNestlingSpawnFrequency()));
                }

                try {
                    Integer nestlingFractionComboBoxValue = (Integer) nestlingMinFractionComboBox.getSelectedItem();
                    habitat.setNestlingMinFraction(nestlingFractionComboBoxValue / 100.0);
                } catch (Exception exception) {
                    correctInputFlag = false;
                    errorMessage += "\nНеверно введенна доля птенцов";
                    nestlingMinFractionComboBox.setSelectedIndex(5);
                }

                Long adultBirdLifeTime = 0l;
                try {
                    adultBirdLifeTime = Long.parseLong(adultBirdLifeTimeTextField.getText());
                    //AdultBird.setLifeTime(adultBirdLifeTime);
                } catch (Exception exception) {
                    correctInputFlag = false;
                    adultBirdLifeTimeTextField.setText(Long.toString(AdultBird.getLifeTimeStatic()));
                    exception.printStackTrace();
                    errorMessage += "\nНеверно введен период жизни взрослой птицы";
                }

                Long nestlingLifeTime = 0l;
                try {
                    nestlingLifeTime = Long.parseLong(nestlingLifeTimeTextField.getText());
                    //Nestling.setLifeTime(nestlingLifeTime);

                } catch (Exception exception) {
                    correctInputFlag = false;
                    errorMessage += "\nНеверно введен период жизни птенцов";
                    nestlingLifeTimeTextField.setText(Long.toString(AdultBird.getLifeTimeStatic()));
                }


                if (correctInputFlag) {
                    try {
                        habitat.setAdultBirdSpawnFrequency(adultBirdFrequency);
                    } catch (Exception exception) {
                        errorMessage += "\n" + "Частота появления взрослых птиц: " + exception.getMessage();
                    }
                    try {
                        habitat.setNestlingSpawnFrequency(nestlingFrequency);
                    } catch (Exception exception) {
                        errorMessage += "\n" + "Частота появления птенцов: " + exception.getMessage();
                    }
                    try {
                        AdultBird.setLifeTime(adultBirdLifeTime);
                    } catch (Exception exception) {
                        errorMessage += "\n" + "Период жизни взрослых птиц: " + exception.getMessage();
                    }
                    try {
                        Nestling.setLifeTime(nestlingLifeTime);
                    } catch (Exception exception) {
                        errorMessage += "\n" + "Период жизни птенцов: " + exception.getMessage();
                    }
                    System.out.println(habitat.toString());
                }
                if (correctInputFlag) dispose();
                else JOptionPane.showMessageDialog(null, errorMessage, "Ошибка ввода", JOptionPane.PLAIN_MESSAGE);
            }
        });
        JButton resetButton = new JButton("По умолчанию");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setDefaultValues();
                setDefaultHabitatValues();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
    }

    public SettingsDialog(Habitat habitat) {
        this();
        this.habitat = habitat;
        adultBirdSpawnFrequencyTextField.setText(Integer.toString(habitat.getAdultBirdSpawnFrequency()));
        nestlingSpawnFrequencyTextField.setText(Integer.toString(habitat.getNestlingSpawnFrequency()));
        adultBirdSpawnProbabilityComboBox.setSelectedIndex((int) (habitat.getAdultBirdSpawnProbability() * 10));
        nestlingMinFractionComboBox.setSelectedIndex((int) (habitat.getNestlingMinFraction() * 10));
        adultBirdLifeTimeTextField.setText(Long.toString(AdultBird.getLifeTimeStatic()));
        nestlingLifeTimeTextField.setText(Long.toString(Nestling.getLifeTimeStatic()));
    }

    private void setDefaultHabitatValues() {
        adultBirdSpawnFrequencyTextField.setText(Integer.toString(Habitat.DEFAULT_ADULT_BIRD_SPAWN_FREQUENCY));
        nestlingSpawnFrequencyTextField.setText(Integer.toString(Habitat.DEFAULT_NESTLING_SPAWN_FREQUENCY));
        adultBirdSpawnProbabilityComboBox.setSelectedIndex(5);
        nestlingMinFractionComboBox.setSelectedIndex(5);
    }
}
