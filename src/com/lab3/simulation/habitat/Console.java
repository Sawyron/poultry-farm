package com.lab3.simulation.habitat;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Console {
    private Habitat habitat;
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JTextPane console = new JTextPane();
    private JScrollPane scrollPane = new JScrollPane(console);
    private int currentPosition = 0;
    private int startInput;
    private int endInput;
    private Filter filter = new Filter();

    private class Filter extends DocumentFilter{
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset >= currentPosition){
                super.remove(fb, offset, length);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (offset >= currentPosition){
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (offset >= currentPosition){
                super.replace(fb, offset, length, text, attrs);
                if (text.equalsIgnoreCase("\n")) {
                    startInput = currentPosition;
                    currentPosition = console.getCaretPosition();
                    endInput = currentPosition - 1;
                    String s = console.getText(startInput,endInput - startInput);
                    executeCommand(s);
                }
            }
        }
    }

    public Console(Habitat habitat) {
        this.habitat = habitat;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame.setLocationRelativeTo(habitat);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Console");
        frame.setSize(600, 400);
        frame.add(mainPanel);
        console.setBackground(Color.DARK_GRAY);
        console.setForeground(Color.green);
        console.setCaretColor(Color.green);
        console.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 16));
        ((AbstractDocument)console.getDocument()).setDocumentFilter(filter);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    private void executeCommand(String s){
        List<String> commands = new ArrayList<>(Arrays.asList(s.split(" ")));
        System.out.println(commands.get(0));
        if (commands.get(0).equalsIgnoreCase("clear")){
            clear();
        }
    }

    private void clear() {
        currentPosition = 0;
        console.setText(null);
    }
}
