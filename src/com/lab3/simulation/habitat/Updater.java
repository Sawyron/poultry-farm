package com.lab3.simulation.habitat;

public class Updater implements Runnable {
    private final Habitat HABITAT;
    private final Warden WARDEN;
    private long startTime;

    public Updater(Habitat HABITAT) {
        this.HABITAT = HABITAT;
        this.WARDEN = HABITAT.getWARDEN();
    }

    @Override
    public void run() {
        while (!WARDEN.isFinish()) {
            if (WARDEN.isRunning()) {
                long elapsed;
                if (HABITAT.isFirstRun()) {
                    HABITAT.setFirstRun(false);
                    startTime = System.currentTimeMillis();
                } else {
                    elapsed = System.currentTimeMillis() - startTime;
                    HABITAT.updateTime(elapsed);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                synchronized (WARDEN) {
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
