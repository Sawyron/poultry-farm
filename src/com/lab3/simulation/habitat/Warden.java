package com.lab3.simulation.habitat;

public class Warden {
    private volatile boolean finish = false;
    private volatile boolean running = false;
    private volatile boolean pause = false;
    private volatile boolean adultBirdAIActive = true;
    private volatile boolean nestlingAIActive = true;

    public synchronized boolean isNestlingAIActive() {
        return nestlingAIActive;
    }

    public synchronized void setNestlingAIActive(boolean nestlingAIActive) {
        notify();
        this.nestlingAIActive = nestlingAIActive;
    }

    public synchronized boolean isAdultBirdAIActive() {
        return adultBirdAIActive;
    }

    public synchronized void setAdultBirdAIActive(boolean adultBirdAIActive) {
        notify();
        this.adultBirdAIActive = adultBirdAIActive;
    }

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
