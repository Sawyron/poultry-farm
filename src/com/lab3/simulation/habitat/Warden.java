package com.lab3.simulation.habitat;

public class Warden {
    private volatile boolean finish = false;
    private volatile boolean running = false;
    private volatile Boolean pause = false;

    public boolean getAdultBirdAIActive() {
        return adultBirdAIActive;
    }

    public void setAdultBirdAIActive(boolean adultBirdAIActive) {
        this.adultBirdAIActive = adultBirdAIActive;
        notify();
    }

    private volatile boolean adultBirdAIActive = true;

    public synchronized boolean isFinish() {
        return finish;
    }

    public synchronized void setFinish(boolean finish) {
        this.finish = finish;
        notifyAll();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
        notifyAll();
    }

    public synchronized boolean isPause() {
        return pause;
    }

    public synchronized void setPause(boolean pause) {
        this.pause = pause;
        notifyAll();
    }
}
