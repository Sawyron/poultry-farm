package com.lab3.simulation.habitat;

public class Updater extends Service {

    private long startTime;

    public Updater(Habitat habitat, long period) {
        super(habitat, period);
    }

    @Override
    public boolean isRunning() {
        return WARDEN.isRunning();
    }

    @Override
    public void execute() {
        long elapsed;
        if (habitat.isFirstRun()) {
            habitat.setFirstRun(false);
            startTime = System.currentTimeMillis();
        } else {
            elapsed = System.currentTimeMillis() - startTime;
            habitat.updateTime(elapsed);
        }
    }
}
