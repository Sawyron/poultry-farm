package com.lab3.simulation.habitat;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Console {
    private Habitat habitat;
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JTextArea console = new JTextArea();
    private JTextField input = new JTextField();
    private JScrollPane scrollPane = new JScrollPane(console);

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

        console.setEditable(false);
        input.setBackground(Color.DARK_GRAY);
        input.setForeground(Color.green);
        input.setCaretColor(Color.green);
        input.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 16));
        console.setBackground(Color.DARK_GRAY);
        console.setForeground(Color.green);
        console.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 16));

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(input, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = input.getText();
                console.append(msg + "\n");
                console.setCaretPosition(console.getDocument().getLength());
                input.setText(null);
                executeCommand(msg);
            }
        });
    }
    private void executeCommand(String s){
        List<String> commands = new ArrayList<>(Arrays.asList(s.split(" ")));
        if (commands.get(0).equalsIgnoreCase("clear")){
            clear();
        }
    }

    private void clear() {
        console.setText(null);
    }
}
