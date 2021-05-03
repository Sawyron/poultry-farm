package com.poultryfarm.habitat.services;

import com.poultryfarm.habitat.Client;
import com.poultryfarm.habitat.Habitat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NetworkDialog extends JDialog {
    private Client client;
    private DefaultListModel<Long> listModel;

    public NetworkDialog(Client client) {
        this.client = client;
        listModel = client.getUsersListModel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JButton connectButton = new JButton("Connect");
        mainPanel.add(new JList<Long>(listModel), BorderLayout.CENTER);
        mainPanel.add(connectButton, BorderLayout.SOUTH);
        JLabel infoLabel = new JLabel();
        mainPanel.add(infoLabel, BorderLayout.NORTH);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!client.isConnected()) client.connect();
                    infoLabel.setText(null);
                } catch (IOException ioException) {
                    infoLabel.setText("connection error...");
                }
            }
        });
        add(mainPanel);
        setVisible(true);
        setLocationRelativeTo(null);
        setSize(400, 300);
    }

    public void disconnect() {
        client.disconnect();
    }

}
