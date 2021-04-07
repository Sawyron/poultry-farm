package com.lab3.simulation.habitat;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleCommandReaderThread implements Runnable {
    private PipedReader pipedReader = new PipedReader();
    private PipedWriter pipedWriter = new PipedWriter();
    private Habitat habitat;

    public ConsoleCommandReaderThread(Habitat habitat) {
        this.habitat = habitat;
    }

    public PipedReader getPipedReader() {
        return pipedReader;
    }

    public PipedWriter getPipedWriter() {
        return pipedWriter;
    }

    public void refreshPipe(){
        try {
            pipedWriter.close();
            pipedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pipedReader = new PipedReader();
        pipedWriter = new PipedWriter();
    }

    @Override
    public void run() {
        int i = 0;
        StringBuilder inputMessage = new StringBuilder();
        try {
            while ((i = pipedReader.read()) != -1) {
                inputMessage.append((char) i);
            }
            System.out.println("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> commands = new ArrayList<>(Arrays.asList(inputMessage.toString().split(" ")));
        switch (commands.get(0)) {
            case "clear":
                String s = "sad";
                try {
                    pipedWriter.write(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "red":
                System.out.println(21);
                break;
        }
    }
}
