package com.poultryfarm.habitat.services;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class PipeChanel {
    private PipedReader pipedReader = new PipedReader();
    private PipedWriter pipedWriter = new PipedWriter();

    public void connect(PipeChanel pipeChanel) throws IOException {
        pipedWriter.connect(pipeChanel.pipedReader);
    }

    public void close() throws IOException {
        pipedWriter.close();
        pipedReader.close();
    }

    public void refreshWriter() throws IOException {
        pipedWriter.close();
        pipedWriter = new PipedWriter();
    }

    public void refreshReader() throws IOException {
        pipedReader.close();
        pipedReader = new PipedReader();
    }

    public void send(String message) throws IOException {
        pipedWriter.write(message);
    }

    public void refresh() throws IOException {
        pipedWriter.close();
        pipedReader.close();
        pipedReader = new PipedReader();
        pipedWriter = new PipedWriter();
    }

    public String receive() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        while ((i = pipedReader.read()) != -1) {
            stringBuilder.append((char) i);
        }
        return stringBuilder.toString();
    }

    public boolean isReady() throws IOException {
        return pipedReader.ready();
    }
}
