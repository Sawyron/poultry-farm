package com.poultryfarm.habitat;

import com.poultryfarm.habitat.services.Console;
import com.poultryfarm.habitat.services.OBJChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MainMenuBar extends JMenuBar {

    private final Habitat habitat;
    private SettingsDialog settingsDialog;
    private final JMenuItem startMenuItem = new JMenuItem("Старт");
    private final JMenuItem stopMenuItem = new JMenuItem("Стоп");
    private final JMenuItem pauseMenuItem = new JMenuItem("Поставить на паузу");
    private final JMenuItem timeVisibleMenuItem = new JMenuItem("Показывать время");
    private final JMenuItem livingObjectDialogMenuItem = new JMenuItem("Список живых объектов");
    private final JMenuItem threadDialogMenuItem = new JMenuItem("Управление потоками");
    private final JMenuItem consoleMenuItem = new JMenuItem("Консоль");
    private final JMenuItem saveConfigMenuItem = new JMenuItem("Сохранить");
    private final JMenuItem loadConfigMenuItem = new JMenuItem("Загрузить");

    public MainMenuBar(Habitat habitat) {
        this.habitat = habitat;
        JMenu mainMenu = new JMenu("Меню");
        JMenu settingsMenu = new JMenu("Настройки");
        pauseMenuItem.setEnabled(false);
        JMenuItem paramsDialogMenuItem = new JMenuItem("Параметры");
        JMenuItem exitMenuItem = new JMenuItem("Выход");
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!habitat.getRunning()) {
                    habitat.startSimulation();
                    setRunningState(true);
                }
            }
        });
        stopMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (habitat.getRunning()) {
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
        paramsDialogMenuItem.addActionListener(new ActionListener() {
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
        threadDialogMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThreadDialog threadDialog = new ThreadDialog(habitat);
                threadDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                threadDialog.setLocationRelativeTo(null);
                threadDialog.setVisible(true);
            }
        });
        consoleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsoleCommandReaderThread consoleCommandReaderThread = new ConsoleCommandReaderThread(habitat);
                Console console = new Console(consoleCommandReaderThread);
            }
        });

        saveConfigMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.pauseIfRunning();
                JFileChooser chooser = new OBJChooser() {
                    @Override
                    public File getSelectedFile() {
                        File file = super.getSelectedFile();
                        if (file != null && !file.getName().endsWith(".obj")) {
                            file = new File(file.getAbsolutePath() + ".obj");
                        }
                        return file;
                    }

                };
                int res = chooser.showSaveDialog(habitat);
                System.out.println(chooser.getSelectedFile());
                if (res == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    habitat.saveBirds(chooser.getSelectedFile());
                }
                habitat.switchSimulationState();
            }
        });

        loadConfigMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new OBJChooser();
                int res = chooser.showOpenDialog(habitat);
                System.out.println(chooser.getSelectedFile());
                if (res == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    habitat.loadBirds(chooser.getSelectedFile());
                }
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
        mainMenu.add(saveConfigMenuItem);
        mainMenu.add(loadConfigMenuItem);
        mainMenu.addSeparator();
        mainMenu.add(exitMenuItem);
        settingsMenu.add(paramsDialogMenuItem);
        settingsMenu.add(threadDialogMenuItem);
        settingsMenu.add(consoleMenuItem);
        add(mainMenu);
        add(settingsMenu);
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
