package com.lab3.simulation.birds;

import com.lab3.simulation.habitat.Warden;

import java.util.List;

public abstract class BaseAI implements Runnable {
    public BaseAI(List<Bird> list, Warden WARDEN) {
        this.list = list;
        this.WARDEN = WARDEN;
    }

    protected static long period = 5_000;
    protected long lastVelocityChange = 0;
    protected boolean firstRun = true;
    protected Boolean active;

    public Thread getThread() {
        return thread;
    }

    private Thread thread = new Thread(this);
    protected final List<Bird> list;
    protected static Bird leaderBird;
    private final Warden WARDEN;
    protected int vX = (int) (Math.random() * (10 + 1) + -5) + 1;
    protected int vY = (int) (Math.random() * (10 + 1) + -5) + 1;

    @Override
    public void run() {
        while (!WARDEN.isFinish()) {
            if (!WARDEN.isPause() && WARDEN.isRunning() && active) {
                if (leaderBird == null) findLeader();
                else {
                    maintenanceLeader();
                    move();
                    try {
                        thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

    private void maintenanceLeader() {
        if (leaderBird.isDead()) {
            findLeader();
        }
    }

    private void findLeader() {
        synchronized (list) {
            for (Bird b : list) {
                if (b instanceof AdultBird && !b.isDead()) {
                    leaderBird = b;
                    break;
                }
            }
        }
    }

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
