package com.poultryfarm.habitat;

import com.poultryfarm.habitat.services.Service;

public class Updater extends Service {

    private long startTime;
    private long awaitBegin;
    private long awaitEnd;
    private boolean isRunning = false;

    public Updater(Habitat habitat, long period) {
        super(habitat, period);
    }

    @Override
    public boolean isRunning() {
        if (!habitat.isFirstRun()) {
            if (WARDEN.isPause() && isRunning) {
                awaitBegin = System.currentTimeMillis();
                isRunning = false;
            }
            if (!WARDEN.isPause() && !isRunning){
                awaitEnd = System.currentTimeMillis();
                startTime += awaitEnd - awaitBegin;
                isRunning = true;
            }
        }
        return !WARDEN.isPause() && WARDEN.isRunning();
    }

    @Override
    public void execute() {
        long elapsed;
        if (habitat.isFirstRun()) {
            habitat.setFirstRun(false);
            startTime = System.currentTimeMillis();
            isRunning = true;
        } else {
            elapsed = System.currentTimeMillis() - startTime;
            habitat.updateTime(elapsed);
        }
    }
}
