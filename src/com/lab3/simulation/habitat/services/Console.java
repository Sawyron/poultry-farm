package com.lab3.simulation.habitat.services;

import com.lab3.simulation.habitat.ConsoleCommandReaderThread;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Console {
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JTextPane console = new JTextPane();
    private JScrollPane scrollPane = new JScrollPane(console);
    private StyledDocument document;
    private int currentPosition = 0;
    private int startInput;
    private int endInput;
    private Filter filter = new Filter();
    private PipedWriter pipedWriter = new PipedWriter();
    private PipedReader pipedReader = new PipedReader();
    private Thread thread;

    public Console(ConsoleCommandReaderThread consoleCommandReaderThread) {
        this();
        this.consoleCommandReaderThread = consoleCommandReaderThread;
    }

    private void initPipe() {
        try {
            pipedWriter.close();
            pipedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pipedWriter = new PipedWriter();
        pipedReader = new PipedReader();
        consoleCommandReaderThread.refreshPipe();
        try {
            pipedReader.connect(consoleCommandReaderThread.getPipedWriter());
            pipedWriter.connect(consoleCommandReaderThread.getPipedReader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(consoleCommandReaderThread);
        thread.setDaemon(true);
        thread.setPriority(2);
        thread.start();
    }

    private ConsoleCommandReaderThread consoleCommandReaderThread;

    private class Filter extends DocumentFilter {
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset >= currentPosition) {
                super.remove(fb, offset, length);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (offset >= currentPosition) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (offset >= currentPosition) {
                super.replace(fb, offset, length, text, attrs);
                if (text.equalsIgnoreCase("\n")) {
                    startInput = currentPosition;
                    currentPosition = console.getCaretPosition();
                    endInput = currentPosition - 1;
                    String s = console.getText(startInput, endInput - startInput);
                    executeCommand(s);
                }
            }
        }
    }

    private Console() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Console");
        frame.setSize(600, 400);
        frame.add(mainPanel);
        document = console.getStyledDocument();
        console.setBackground(Color.DARK_GRAY);
        console.setForeground(Color.green);
        console.setCaretColor(Color.green);
        console.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 16));
        ((AbstractDocument) document).setDocumentFilter(filter);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        addMenu();
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Меню");
        JMenuItem helpMenuItem = new JMenuItem("Справка");
        JMenuItem exitMenuItem = new JMenuItem("Закрыть");
        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "clear - отчистка консоли";
                JOptionPane.showMessageDialog(null, msg, "Команды", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        menu.add(helpMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void executeCommand(String s) {
        List<String> commands = new ArrayList<>(Arrays.asList(s.split(" ")));
        System.out.println(commands.get(0));
        if (commands.get(0).equals("clear")) {
            clear();
        }
        if (commands.get(0).equals("lorem")) printMessage("lorem ipsum");
        if (commands.get(0).equals("red")) printMessage("21");
        initPipe();
        try {
            pipedWriter.write(s);
            pipedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        currentPosition = 0;
        console.setText(null);
    }

    private void printMessage(String message) {
        try {
            document.insertString(currentPosition, message + "\n", null);
            currentPosition += message.length() + 1;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
