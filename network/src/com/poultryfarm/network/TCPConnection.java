package com.poultryfarm.network;

import java.io.*;
import java.net.Socket;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener eventListener, String address, int port) throws IOException {
        this(eventListener, new Socket(address, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventListener.onConnection(TCPConnection.this);
                try {
                    while (rxThread.isInterrupted()) {
                        String msg = in.readLine();
                        if (msg != null) eventListener.onReceiveString(TCPConnection.this, msg);
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public void start() {

    }

    @Override
    public String toString() {
        return "TCP Connection: " + socket.getInetAddress() + ":" + socket.getPort();
    }

    public synchronized void sendSting(String value) {
        try {
            out.write(value);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            eventListener.onException(this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(this, e);
            e.printStackTrace();
        }
    }
}
