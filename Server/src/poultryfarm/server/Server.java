package poultryfarm.server;

import com.poultryfarm.network.TCPConnection;
import com.poultryfarm.network.TCPConnectionListener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server().start();
    }

    private final Random random = new Random();
    private final Map<TCPConnection, Long> connections = new HashMap<TCPConnection, Long>();


    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true) {
                new TCPConnection(this, serverSocket.accept()).connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onConnection(TCPConnection tcpConnection) {
        long id = 0;
        do {
            id = Math.abs(random.nextLong());
        } while (connections.containsValue(id));
        for (TCPConnection connection : connections.keySet()) {
            connection.sendSting(TCPConnectionListener.USER_CONNECT + ":" + id);
        }
        System.out.println("User connected: " + id + " " + tcpConnection);
        for (Long userID : connections.values()) {
            tcpConnection.sendSting(TCPConnectionListener.USER_CONNECT + ":" + userID);
        }
        connections.put(tcpConnection, id);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        String[] request = value.split(":");
        if (request[0].equals(TCPConnectionListener.DISCONNECT)) tcpConnection.disconnect();
        if (TCPConnectionListener.HASH_BIRDS.equals(request[0])) {
            for (Map.Entry<TCPConnection, Long> entry : connections.entrySet()) {
                if (entry.getValue().equals(Long.parseLong(request[1]))) {
                    entry.getKey().sendSting(value);
                    break;
                }
            }
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        long id = connections.get(tcpConnection);
        connections.remove(tcpConnection);
        System.out.println("User disconnected: " + tcpConnection + " " + id);
        for (TCPConnection connection : connections.keySet()) {
            connection.sendSting(TCPConnectionListener.USER_DISCONNECT + ":" + id);
        }
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {

    }
}

