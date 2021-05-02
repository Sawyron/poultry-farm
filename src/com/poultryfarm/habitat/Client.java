package com.poultryfarm.habitat;

import com.poultryfarm.network.TCPConnection;
import com.poultryfarm.network.TCPConnectionListener;

import java.io.IOException;
import java.util.TreeMap;

public class Client implements TCPConnectionListener {

    public static void main(String[] args) {
        new Client();
    }

    private TCPConnection connection;

    private Client() {
        try {
            connection = new TCPConnection(this, "localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
            connection.sendSting(TCPConnectionListener.DISCONNECT);
            connection.disconnect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnection(TCPConnection tcpConnection) {
        System.out.println("Connected");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Disconnected");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}
