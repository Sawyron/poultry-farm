package com.lab3.simulation.birds;


import com.lab3.simulation.habitat.Habitat;

import java.util.List;

public class NestlingAI extends BaseAI {
    public NestlingAI(Habitat habitat, long period, List<Bird> list) {
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
                if (b instanceof Nestling) b.move();
            }
        }
    }

    @Override
    public void add(Bird bird) {
        bird.setVelocity(vX / 2, vY / 2);
        list.add(bird);
    }

    @Override
    public boolean isRunning() {
        return (!WARDEN.isPause() && WARDEN.isRunning() && WARDEN.isNestlingAIActive());
    }

    @Override
    public void execute() {
        move();
    }
}
