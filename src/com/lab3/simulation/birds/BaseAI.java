package com.lab3.simulation.birds;

import com.lab3.simulation.habitat.Habitat;
import com.lab3.simulation.habitat.Service;
import com.lab3.simulation.habitat.Warden;

import java.util.List;

public abstract class BaseAI extends Service {

    protected static long period = 5_000;
    protected long lastVelocityChange = 0;
    protected boolean firstRun = true;

    public BaseAI(Habitat habitat, long period, List<Bird> list) {
        super(habitat, period);
        WARDEN = habitat.getWARDEN();
        this.list = list;
    }

    public Thread getThread() {
        return thread;
    }

    private Thread thread = new Thread(this);
    protected final List<Bird> list;
    protected final Warden WARDEN;
    protected int vX = (int) (Math.random() * (10 + 1) + -5) + 1;
    protected int vY = (int) (Math.random() * (10 + 1) + -5) + 1;


    protected void setVelocity() {
        synchronized (list) {
            for (Bird b : list) {
                if (!b.isDead()) {
                    b.setVelocity(vX, vY);
                }
            }
        }
    }

    abstract void move();
    public abstract void add(Bird bird);
}
