package com.poultryfarm.birds;


import com.poultryfarm.habitat.Habitat;

import java.util.List;

public class NestlingAI extends BaseAI {
    public NestlingAI(Habitat habitat, long period, List<Bird> list) {
        super(habitat, period, list);
    }

    @Override
    void move() {
        long currentTime = System.currentTimeMillis();
        synchronized (list) {
            for (Bird b : list) {
                if (b instanceof Nestling) b.move();
            }
        }
    }

    @Override
    public synchronized void add(Bird bird) {
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
