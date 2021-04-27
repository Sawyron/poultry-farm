package com.poultryfarm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private boolean active;

    public static void main(String[] args) {
        Client client = new Client();
        client.launch();
    }

    public void launch(){
        try (Socket socket = new Socket("localhost", 8000);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            active = true;
            Thread readerThread = new Thread(new ResponseReader(reader));
            readerThread.start();
            writer.write("123512");
            writer.newLine();
            writer.flush();
            Thread.sleep(20);
            active = false;
            readerThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ResponseReader implements Runnable{
        BufferedReader reader;

        public ResponseReader(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            String response;
            while (active){
                try {
                    response = reader.readLine();
                    if (response!=null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
