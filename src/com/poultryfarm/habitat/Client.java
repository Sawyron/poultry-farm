package com.poultryfarm.habitat;

import com.poultryfarm.network.TCPConnection;
import com.poultryfarm.network.TCPConnectionListener;

import javax.swing.*;
import java.io.IOException;
import java.util.TreeMap;

public class Client implements TCPConnectionListener {

    private TCPConnection connection;
    private boolean isConnected = false;
    private DefaultListModel<Long> users = new DefaultListModel<>();

    public boolean isConnected() {
        return isConnected;
    }


    public Client() throws IOException {
        connection = new TCPConnection(this, "localhost", 8000);
    }

    public void connect() throws IOException {
        connection.connect();
        isConnected = true;
    }

    public void disconnect() {
        connection.sendSting(TCPConnectionListener.DISCONNECT);
        connection.disconnect();
        isConnected = false;
    }

    public DefaultListModel<Long> getUsersListModel() {
        return users;
    }

    @Override
    public void onConnection(TCPConnection tcpConnection) {
        System.out.println("Connected");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println(value);
        String[] command = value.split(":");
        switch (command[0]) {
            case TCPConnectionListener.USER_CONNECT -> {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        users.addElement(Long.parseLong(command[1]));
                    }
                });
            }
            case TCPConnectionListener.USER_DISCONNECT -> {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        users.removeElement(Long.parseLong(command[1]));
                    }
                });
            }
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Disconnected");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}
