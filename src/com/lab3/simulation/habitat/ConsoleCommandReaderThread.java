package com.lab3.simulation.habitat;

import com.lab3.simulation.habitat.services.PipeChanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleCommandReaderThread implements Runnable {
    private PipeChanel pipeChanel = new PipeChanel();
    private Habitat habitat;

    public ConsoleCommandReaderThread(Habitat habitat) {
        this.habitat = habitat;
    }

    public PipeChanel getPipeChanel() {
        return pipeChanel;
    }

    public void connectPipe(PipeChanel pipeChanel) throws IOException {
        this.pipeChanel.connect(pipeChanel);
        pipeChanel.connect(this.pipeChanel);
    }

    @Override
    public void run() {
        String message = "";
        try {
            message = pipeChanel.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> commands = new ArrayList<>(Arrays.asList(message.split(" ")));
        switch (commands.get(0)) {
            case "sad":
                try {
                    pipeChanel.send("asd");
                    pipeChanel.refreshWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    synchronized (this) {
                        notify();
                    }
                }
                break;
            default:
                synchronized (this) {
                    notify();
                }
                break;
        }
    }
}
