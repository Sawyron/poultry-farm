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
    private Thread thread;
    private PipeChanel pipeChanel = new PipeChanel();
    private final ConsoleCommandReaderThread consoleCommandReaderThread;

    public Console(ConsoleCommandReaderThread consoleCommandReaderThread) {
        this.consoleCommandReaderThread = consoleCommandReaderThread;
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
        try {
            pipeChanel.refresh();
            consoleCommandReaderThread.getPipeChanel().refresh();
            consoleCommandReaderThread.connectPipe(pipeChanel);
            pipeChanel.send(commands.get(0));
            pipeChanel.refreshWriter();
            thread = new Thread(consoleCommandReaderThread);
            thread.setDaemon(true);
            thread.setPriority(7);
            thread.start();
            String response;
            synchronized (consoleCommandReaderThread) {
                try {
                    consoleCommandReaderThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (pipeChanel.isReady()) {
                response = pipeChanel.receive();
                printMessage(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (commands.get(0).equals("clear")) {
            clear();
        }
    }

    public void connectPipe(PipeChanel pipeChanel) throws IOException {
        this.pipeChanel.connect(pipeChanel);
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
