package com.lab3.simulation.habitat;


public class Painter implements Runnable {
    private final Habitat habitat;
    private final Warden warden;

    public Painter(Habitat habitat) {
        this.habitat = habitat;
        this.warden = habitat.getWARDEN();
    }

    @Override
    public void run() {
        synchronized (warden) {
            try {
                warden.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (!warden.isFinish()) {
            if (!warden.isPause() && warden.isRunning()) {
                habitat.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                synchronized (warden){
                    try {
                        warden.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
