package com.poultryfarm.birds;

import com.poultryfarm.habitat.Habitat;

import java.util.List;

public class AdultBirdAI extends BaseAI {

    public AdultBirdAI(Habitat habitat, long period, List<Bird> list) {
        super(habitat, period, list);
    }

    @Override
    void move() {
        long currentTime = System.currentTimeMillis();
        if (firstRun) {
            lastVelocityChange = currentTime;
            firstRun = false;
        }
        if (currentTime - lastVelocityChange > period) {
            vX = (int) (Math.random() * (10 + 1) + -5) + 1;
            vY = (int) (Math.random() * (10 + 1) + -5) + 1;
            setVelocity();
            lastVelocityChange = currentTime;
        }
        synchronized (list) {
            for (Bird b : list) {
                if (b instanceof AdultBird) b.move();
            }
        }
    }

    @Override
    public synchronized void add(Bird bird) {
        bird.setVelocity(vX, vY);
        list.add(bird);
    }

    @Override
    public boolean isRunning() {
        return (!WARDEN.isPause() && WARDEN.isRunning() && WARDEN.isAdultBirdAIActive());
    }

    @Override
    public void execute() {
        move();
    }
}
