package com.lab3.simulation.habitat;


public class Painter implements Runnable {
    private final Habitat habitat;
    private final Warden WARDEN;

    public Painter(Habitat habitat) {
        this.habitat = habitat;
        this.WARDEN = habitat.getWARDEN();
    }

    @Override
    public void run() {
        while (!WARDEN.isFinish()) {
            if (!WARDEN.isPause() && WARDEN.isRunning()) {
                habitat.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                synchronized (WARDEN){
                    try {
                        WARDEN.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
