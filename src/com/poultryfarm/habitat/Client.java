package com.poultryfarm.habitat;

import com.poultryfarm.network.TCPConnection;
import com.poultryfarm.network.TCPConnectionListener;

import javax.swing.*;
import java.io.IOException;

public class Client implements TCPConnectionListener {

    private Habitat habitat;
    private TCPConnection connection;
    private boolean isConnected = false;
    private DefaultListModel<Long> users = new DefaultListModel<>();

    public boolean isConnected() {
        return isConnected;
    }


    public Client(Habitat habitat) {
        this.habitat = habitat;
    }

    public void connect() throws IOException {
        connection = new TCPConnection(this, "localhost", 8000);
        connection.connect();
        isConnected = true;
    }

    public void disconnect() {
        if (isConnected) {
            connection.sendSting(TCPConnectionListener.CLIENT_DISCONNECT);
            connection.disconnect();
            isConnected = false;
        }
    }

    public void sendHashBirdsCommand(long id, int percent) {
        connection.sendSting(TCPConnectionListener.HASH_BIRDS + ":" + id + ":" + percent);
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
            case TCPConnectionListener.HASH_BIRDS -> {
                System.out.println(value);
                habitat.hashBirds(Integer.parseInt(command[2]));
            }
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Disconnected");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        isConnected = false;
    }
}
