package poultryfarm.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private Set<Socket> activeClients = new HashSet<>();

    public void launch() {
        try (ServerSocket server = new ServerSocket(8000)) {
            System.out.println("Server started");
            Socket socket = server.accept();
            Thread thread = new Thread(new ClientWorker(socket));
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private synchronized void notifyClients() {
        for (Socket socket : activeClients) {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write(activeClients.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class ClientWorker implements Runnable {
        private Socket socket;

        public ClientWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println(socket.toString());
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("Thread started");
                activeClients.add(socket);
                notifyClients();
                String request = "";
                request = reader.readLine().strip();
                System.out.println(request);
                String response = "Received: " + request;
                writer.write(response);
                writer.newLine();
                writer.flush();
                socket.close();
                activeClients.remove(socket);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.launch();
    }
}
