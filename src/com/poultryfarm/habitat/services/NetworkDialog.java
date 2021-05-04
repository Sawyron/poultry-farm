package com.poultryfarm.habitat.services;

import com.poultryfarm.habitat.Client;
import com.poultryfarm.habitat.Habitat;
import com.poultryfarm.habitat.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NetworkDialog extends JDialog {
    private Client client;
    private DefaultListModel<Long> listModel;
    private JTextField commandTextField = new JTextField(5);
    private JLabel infoLabel = new JLabel();
    private JButton refreshConnectionButton = new JButton("Обновить соединение");

    private final String CONNECTION_ERROR = "Не удалось установить соединение с сервером";
    private final String CONNECTION_READY = "Подключено к серверу";

    public NetworkDialog(Client client) {
        this.client = client;
        infoLabel.setText("Не подключено к серверу");
        listModel = client.getUsersListModel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JButton connectButton = new JButton("Send");
        JList<Long> users = new JList<>(listModel);
        mainPanel.add(users, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.add(refreshConnectionButton);
        controlPanel.add(connectButton);
        refreshConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!client.isConnected()) {
                    client.disconnect();
                    try {
                        client.connect();
                        infoLabel.setText(CONNECTION_READY);
                        refreshConnectionButton.setEnabled(false);
                    } catch (IOException ioException) {
                        infoLabel.setText(CONNECTION_ERROR);
                    }
                }
            }
        });
        controlPanel.add(commandTextField);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(infoLabel, BorderLayout.NORTH);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.isConnected()) {
                    try {
                        int value = getPercent();
                        Long id = users.getSelectedValue();
                        if (id != null) {
                            client.sendHashBirdsCommand(id, value);
                        }
                        refreshConnectionButton.setEnabled(false);
                    } catch (IOException ioException) {
                        infoLabel.setText(CONNECTION_ERROR);
                        refreshConnectionButton.setEnabled(true);
                    } catch (Exception exception) {
                        infoLabel.setText("Некорректный ввод");
                    }
                } else {
                    infoLabel.setText(CONNECTION_ERROR);
                    refreshConnectionButton.setEnabled(true);
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

    private int getPercent() throws Exception {
        int value = Integer.parseInt(commandTextField.getText());
        if (value < 0 || value > 100) throw new Exception();
        return value;
    }

}
