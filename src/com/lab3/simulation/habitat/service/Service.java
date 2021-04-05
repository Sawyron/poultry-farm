package com.lab3.simulation.habitat.service;

import com.lab3.simulation.habitat.Habitat;
import com.lab3.simulation.habitat.Warden;

public abstract class Service implements Runnable {
    protected final Habitat habitat;
    protected final Warden WARDEN;
    private long period;

    public Service(Habitat habitat, long period) {
        this.habitat = habitat;
        this.period = period;
        WARDEN = habitat.getWARDEN();
    }

    @Override
    public void run() {
        while (!WARDEN.isFinish()) {
            if (isRunning()) {
                execute();
                try {
                    Thread.sleep(period);
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

    public abstract boolean isRunning();
    public abstract void execute();
}
