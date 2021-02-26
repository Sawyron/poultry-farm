package com.lab3.simulation.habitat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainMenuBar extends JMenuBar {

    private final Habitat habitat;
    private SettingsDialog settingsDialog;
    private final JMenuItem startMenuItem = new JMenuItem("Старт");
    private final JMenuItem stopMenuItem = new JMenuItem("Стоп");
    private final JMenuItem pauseMenuItem = new JMenuItem("Поставить на паузу");
    private final JMenuItem timeVisibleMenuItem = new JMenuItem("Показывать время");
    private final JMenuItem livingObjectDialogMenuItem = new JMenuItem("Список живых объектов");

    public MainMenuBar(Habitat habitat) {
        this.habitat = habitat;
        JMenu mainMenu = new JMenu("Меню");
        pauseMenuItem.setEnabled(false);
        JMenuItem settingsDialogMenuItem = new JMenuItem("Настройки");
        JMenuItem exitMenuItem = new JMenuItem("Выход");
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!habitat.getStartStop()) {
                    habitat.startSimulation();
                    setRunningState(true);
                }
            }
        });
        stopMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (habitat.getStartStop()) {
                    habitat.prepareSimulationStop();
                    setRunningState(false);
                }
            }
        });
        stopMenuItem.setEnabled(false);
        pauseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.switchSimulationState();
            }
        });
        timeVisibleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.setTimeVisibility(!habitat.isTimeVisible());
            }
        });
        settingsDialogMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsDialog settingsDialog = new SettingsDialog(habitat);
                settingsDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                settingsDialog.setLocationRelativeTo(null);
                settingsDialog.setVisible(true);
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        livingObjectDialogMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.switchSimulationState();
                LivingObjectsDialog livingObjectsDialog = new LivingObjectsDialog(habitat);
                //livingObjectsDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                livingObjectsDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        habitat.switchSimulationState();
                        livingObjectsDialog.dispose();
                    }
                });
                livingObjectsDialog.setLocationRelativeTo(null);
                livingObjectsDialog.setVisible(true);
            }
        });


        mainMenu.add(startMenuItem);
        mainMenu.add(pauseMenuItem);
        mainMenu.add(stopMenuItem);
        mainMenu.addSeparator();
        mainMenu.add(timeVisibleMenuItem);
        mainMenu.addSeparator();
        mainMenu.add(livingObjectDialogMenuItem);
        mainMenu.addSeparator();
        mainMenu.add(settingsDialogMenuItem);
        mainMenu.addSeparator();
        mainMenu.add(exitMenuItem);
        this.add(mainMenu);
    }

    void setRunningState(boolean flag) {
        startMenuItem.setEnabled(!flag);
        stopMenuItem.setEnabled(flag);
        pauseMenuItem.setEnabled(flag);
    }

    void setTimeVisibleMenuItemState(boolean flag) {
        if (!flag) {
            timeVisibleMenuItem.setText("Показывать время");
        } else {
            timeVisibleMenuItem.setText("Скрывать время");
        }
    }

    void setPauseMenuItemState(boolean flag) {
        if (flag) {
            pauseMenuItem.setText("Поставить на паузу");
        } else {
            pauseMenuItem.setText("Возобновить");
        }
    }


}
