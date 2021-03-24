package com.lab3.simulation.birds;

import com.lab3.simulation.habitat.Warden;

import java.util.List;

public class AdultBirdAI extends BaseAI {
    public AdultBirdAI(List<Bird> list, Warden WARDEN) {
        super(list, WARDEN);
        active = WARDEN.getAdultBirdAIActive();
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
                b.move();
            }
        }
    }

    @Override
    public void add(Bird bird) {
        bird.setVelocity(vX, vY);
        list.add(bird);
    }
}
