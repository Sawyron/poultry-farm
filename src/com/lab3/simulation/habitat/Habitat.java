package com.lab3.simulation.habitat;

import com.lab3.simulation.birds.AdultBird;
import com.lab3.simulation.birds.AdultBirdAI;
import com.lab3.simulation.birds.Bird;
import com.lab3.simulation.birds.Nestling;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Habitat extends JFrame {
    private boolean startStop = false;
    private boolean timeVisible = false;
    private boolean isFirstRun = true;
    private final Warden WARDEN = new Warden();

    private long time = 0;
    private int adultBirdTotalCounter = 0;
    private int adultBirdCounter = 0;
    private int nestlingTotalCounter = 0;
    private int nestlingCounter = 0;
    public static final int DEFAULT_ADULT_BIRD_SPAWN_FREQUENCY = 1000;
    public static final int DEFAULT_NESTLING_SPAWN_FREQUENCY = 1000;
    public static final double DEFAULT_ADULT_BIRD_SPAWN_PROBABILITY = 0.5;
    public static final double DEFAULT_NESTLING_MIN_FRACTION = 0.5;
    private int adultBirdSpawnFrequency = DEFAULT_ADULT_BIRD_SPAWN_FREQUENCY;
    private double adultBirdSpawnProbability = DEFAULT_ADULT_BIRD_SPAWN_PROBABILITY;
    private int nestlingSpawnFrequency = DEFAULT_NESTLING_SPAWN_FREQUENCY;
    private double nestlingMinFraction = DEFAULT_NESTLING_MIN_FRACTION;
    private long lastSpawnAdultCheck = 0;
    private long lastSpawnNestling = 0;

    public int getAdultBirdSpawnFrequency() {
        return adultBirdSpawnFrequency;
    }

    public double getAdultBirdSpawnProbability() {
        return adultBirdSpawnProbability;
    }

    public int getNestlingSpawnFrequency() {
        return nestlingSpawnFrequency;
    }

    public double getNestlingMinFraction() {
        return nestlingMinFraction;
    }

    private final List<Bird> birds = new LinkedList<>();
    private final Map<Long, Long> lifeTime = new HashMap<>();

    private final ImageIcon adultBirdImageIcon = new ImageIcon(getClass().getResource("/giphy.png"));
    private final ImageIcon nestlingImageIcon = new ImageIcon(getClass().getResource("/rero_rero.gif"));
    private final JPanel topPanel = new JPanel();
    private final ImagePanel middlePanel = new ImagePanel(birds);
    private final JPanel bottomPanel = new JPanel();
    private final JLabel timeLabel = new JLabel();
    private final JRadioButton buttonTimeVisibilityOn = new JRadioButton("Показывать время симуляции");
    private final JRadioButton buttonTimeVisibilityOff = new JRadioButton("Скрывать время симуляции", true);
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JCheckBox infoCheckBox = new JCheckBox("Показывать информацию");
    private final MainMenuBar mainMenuBar = new MainMenuBar(this);

    private Thread paintThread;
    private Thread updateThread;
    private Thread adultBirdAIThread;


    boolean getStartStop() {
        return startStop;
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
    }

    void updateTime(long elapsedTime) {
        if (elapsedTime - lastSpawnAdultCheck >= adultBirdSpawnFrequency) {
            if (Math.random() >= adultBirdSpawnProbability) {
                Bird currentBird = new AdultBird(middlePanel.getSize(), adultBirdImageIcon);
                synchronized (birds) {
                    birds.add(currentBird);
                }
                lifeTime.put(currentBird.getID(), elapsedTime);
                adultBirdTotalCounter++;
                adultBirdCounter++;
            }
            lastSpawnAdultCheck = elapsedTime;
        }
        if (nestlingCounter < (int) (adultBirdCounter * nestlingMinFraction)) {
            if (elapsedTime - lastSpawnNestling >= nestlingSpawnFrequency) {
                Bird currentBird = new Nestling(middlePanel.getSize(), nestlingImageIcon);
                synchronized (birds) {
                    birds.add(currentBird);
                }
                lifeTime.put(currentBird.getID(), elapsedTime);
                nestlingTotalCounter++;
                nestlingCounter++;
                lastSpawnNestling = elapsedTime;
            }
        }
        checkLifeTime(elapsedTime);
        time = elapsedTime;
    }

    private void checkLifeTime(long elapsedTime) {
        Set<Long> toDelete = new TreeSet<>();
        for (Map.Entry<Long, Long> entry : lifeTime.entrySet()) {
            for (Bird b : birds) {
                if (b.getID() == entry.getKey()) {
                    if (!b.isDead()) {
                        if (elapsedTime - entry.getValue() >= b.getLifeTime()) {
                            b.getKilled();
                            entry.setValue(elapsedTime);
                        }
                        break;
                    } else {
                        if (elapsedTime - entry.getValue() >= b.getDeadTime()) toDelete.add(entry.getKey());
                    }
                }
            }
        }
        lifeTime.keySet().removeAll(toDelete);
        Bird.IDsRemoveAll(toDelete);
        synchronized (birds) {
            birds.forEach(b -> {
                if (toDelete.contains(b.getID())) {
                    if (b instanceof AdultBird) adultBirdCounter--;
                    if (b instanceof Nestling) nestlingCounter--;
                }
            });
            birds.removeIf(b -> toDelete.contains(b.getID()));
        }
    }

    Map<Bird, Long> getLivingObjects() {
        Map<Bird, Long> livingObjects = new HashMap<>();
        for (Map.Entry<Long, Long> entry : lifeTime.entrySet()) {
            for (Bird b : birds) {
                if (b.getID() == entry.getKey()) {
                    livingObjects.put(b, entry.getValue());
                    break;
                }
            }
        }
        return livingObjects;
    }

    void setDefaultValues() {
        adultBirdSpawnFrequency = DEFAULT_ADULT_BIRD_SPAWN_FREQUENCY;
        adultBirdSpawnProbability = DEFAULT_ADULT_BIRD_SPAWN_PROBABILITY;
        nestlingSpawnFrequency = DEFAULT_NESTLING_SPAWN_FREQUENCY;
        nestlingMinFraction = DEFAULT_NESTLING_MIN_FRACTION;
    }

    @Override
    public String toString() {
        return "Habitat{" +
                "adultBirdSpawnFrequency=" + adultBirdSpawnFrequency +
                ", adultBirdSpawnProbability=" + adultBirdSpawnProbability +
                ", nestlingSpawnFrequency=" + nestlingSpawnFrequency +
                ", nestlingMinFraction=" + nestlingMinFraction +
                ", adultBirdLifeTime=" + AdultBird.getLifeTimeStatic() +
                ", nestlingMinFraction=" + Nestling.getLifeTimeStatic() +
                '}';
    }

    public void setAdultBirdSpawnFrequency(int adultBirdSpawnFrequency) throws Exception {
        if (adultBirdSpawnFrequency < 0) throw new Exception("Некорректное значение");
        this.adultBirdSpawnFrequency = adultBirdSpawnFrequency;
    }

    public void setAdultBirdSpawnProbability(double adultBirdSpawnProbability) throws Exception {
        if (adultBirdSpawnProbability < 0 || adultBirdSpawnProbability > 1)
            throw new Exception("Некорректное значение");
        this.adultBirdSpawnProbability = adultBirdSpawnProbability;
    }

    public void setNestlingSpawnFrequency(int nestlingSpawnFrequency) throws Exception {
        if (nestlingSpawnFrequency < 0) throw new Exception("Некорректное значение");
        this.nestlingSpawnFrequency = nestlingSpawnFrequency;
    }

    public void setNestlingMinFraction(double nestlingMinFraction) throws Exception {
        if (nestlingMinFraction < 0 || nestlingMinFraction > 1) throw new Exception("Некорректное значение");
        this.nestlingMinFraction = nestlingMinFraction;
    }

    public Habitat() {
        this.setFocusable(true);
        this.requestFocusInWindow();

        updateThread = new Thread(new Updater(this));
        updateThread.start();
        paintThread = new Thread(new Painter(this));
        paintThread.start();
        adultBirdAIThread = new Thread(new AdultBirdAI(birds,WARDEN).getThread());
        adultBirdAIThread.start();


        KeyAdapter keyListener = new MainKeyListener();
        this.addKeyListener(keyListener);

        this.setLayout(new BorderLayout());
        timeLabel.setOpaque(true);
        timeLabel.setBorder(new LineBorder(Color.BLUE, 3));
        timeLabel.setVisible(timeVisible);
        setTopPanel();
        setMiddlePanel();
        setBottomPanel();
        this.setJMenuBar(mainMenuBar);
        addComponents();

        // On close operation
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WARDEN.setFinish(true);
                try {
                    paintThread.join();
                    updateThread.join();
                    adultBirdAIThread.join();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    private void setBottomPanel() {
        bottomPanel.setLayout(new FlowLayout());

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareSimulationStop();
            }
        });
        infoCheckBox.setFocusable(false);
        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        stopButton.setEnabled(false);

        ButtonGroup timeShowButtonGroup = new ButtonGroup();
        timeShowButtonGroup.add(buttonTimeVisibilityOn);
        timeShowButtonGroup.add(buttonTimeVisibilityOff);
        ItemListener timeShowGroupItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (buttonTimeVisibilityOn.isSelected()) {
                    setTimeVisibility(true);
                }
                if (buttonTimeVisibilityOff.isSelected()) {
                    setTimeVisibility(false);
                }
            }
        };
        buttonTimeVisibilityOff.addItemListener(timeShowGroupItemListener);
        buttonTimeVisibilityOn.addItemListener(timeShowGroupItemListener);
        buttonTimeVisibilityOn.setFocusable(false);
        buttonTimeVisibilityOff.setFocusable(false);

        bottomPanel.setBorder(new LineBorder(Color.ORANGE, 3));
    }

    private void setMiddlePanel() {
        JLabel paintingLabel = new JLabel();
        paintingLabel.setOpaque(true);
        paintingLabel.setBackground(Color.GREEN);
        middlePanel.setBorder(new LineBorder(Color.CYAN, 3));
        middlePanel.add(paintingLabel);
        middlePanel.setLayout(new GridLayout());
    }

    private void setTopPanel() {
        String infoText = "B - старт\nE - стоп\nT - скрыть\\показать таймер\nP - пауза";
        JLabel infoLabel = new JLabel("<html>" + infoText.replaceAll("\n", "<br>") + "</html>");
        infoLabel.setBorder(new LineBorder(Color.BLUE, 3));
        topPanel.add(infoLabel);
        topPanel.setBorder(new LineBorder(Color.BLACK, 3));
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void addComponents() {
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        topPanel.add(timeLabel);
        add(middlePanel, BorderLayout.CENTER);
        bottomPanel.add(startButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(infoCheckBox);
        bottomPanel.add(buttonTimeVisibilityOn);
        bottomPanel.add(buttonTimeVisibilityOff);
    }

    public Warden getWARDEN() {
        return WARDEN;
    }

    private class MainKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_B:
                    if (isFirstRun) {
                        startSimulation();
                    }
                    break;
                case KeyEvent.VK_E: {
                    if (!isFirstRun) {
                        prepareSimulationStop();
                    }
                    break;
                }
                case KeyEvent.VK_T:
                    setTimeVisibility(!timeVisible);
                    break;
                case KeyEvent.VK_P:
                    if (!isFirstRun) {
                        switchSimulationState();
                    }
                    break;
            }
        }
    }

    public boolean isTimeVisible() {
        return timeVisible;
    }

    void switchSimulationState() {
        startStop = !startStop;
        mainMenuBar.setPauseMenuItemState(startStop);
        WARDEN.setPause(!startStop);

    }

    void setTimeVisibility(boolean flag) {
        buttonTimeVisibilityOn.setSelected(flag);
        mainMenuBar.setTimeVisibleMenuItemState(flag);
        timeVisible = flag;
        timeLabel.setVisible(flag);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        timeLabel.setText("Время: " + time / 1000.0);
    }

    void prepareSimulationStop() {
        startStop = false;
        WARDEN.setPause(true);
        if (infoCheckBox.isSelected()) {
            JDialog dialogInfo = new JDialog();
            JPanel dialogInfoPanel = new JPanel(new BorderLayout());
            dialogInfo.setTitle("Результат симуляции");
            String info = "Время: " + time / 1000.0 + "\n";
            info += "Количесво созданных объектов: " + (adultBirdTotalCounter + nestlingTotalCounter) + "\n";
            info += "Взрослых птиц: " + adultBirdTotalCounter + "\n";
            info += "Птенцов: " + nestlingTotalCounter + "\n";
            JTextArea infoTextArea = new JTextArea(info);
            infoTextArea.setEditable(false);
            JButton buttonContinue = new JButton("Ok");
            JButton buttonCancel = new JButton("Отмена");
            buttonContinue.setFocusable(false);
            buttonCancel.setFocusable(false);
            buttonCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startStop = true;
                    WARDEN.setPause(false);
                    dialogInfo.dispose();
                }
            });
            buttonContinue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopSimulation();
                    dialogInfo.dispose();
                }
            });
            dialogInfo.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    startStop = true;
                    dialogInfo.dispose();
                }
            });
            dialogInfoPanel.add(infoTextArea, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(buttonContinue);
            buttonPanel.add(buttonCancel);
            dialogInfoPanel.add(buttonPanel, BorderLayout.SOUTH);
            dialogInfo.add(dialogInfoPanel);
            dialogInfo.setLocationRelativeTo(Habitat.this);
            dialogInfo.pack();
            dialogInfo.setVisible(true);

        } else stopSimulation();
    }

    private void stopSimulation() {
        WARDEN.setRunning(false);
        WARDEN.setPause(true);
        startStop = false;
        isFirstRun = true;
        repaint();
        birds.clear();
        adultBirdTotalCounter = 0;
        adultBirdCounter = 0;
        nestlingTotalCounter = 0;
        nestlingCounter = 0;
        lastSpawnAdultCheck = 0;
        lastSpawnNestling = 0;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        mainMenuBar.setRunningState(false);
    }

    void startSimulation() {
        WARDEN.setRunning(true);
        WARDEN.setPause(false);
        time = 0;
        startStop = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        mainMenuBar.setRunningState(true);
    }

}
