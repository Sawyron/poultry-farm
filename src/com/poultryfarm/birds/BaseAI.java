package com.poultryfarm.birds;

import com.poultryfarm.habitat.Habitat;
import com.poultryfarm.habitat.Warden;
import com.poultryfarm.habitat.services.Service;

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
    protected static int vX = (int) (Math.random() * (10 + 1) + -5) + 1;
    protected static int vY = (int) (Math.random() * (10 + 1) + -5) + 1;


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
