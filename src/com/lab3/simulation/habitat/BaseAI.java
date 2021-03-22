package com.lab3.simulation.habitat;

import com.lab3.simulation.Bird;

import java.util.List;

public abstract class BaseAI implements Runnable{
    public BaseAI(List<Bird> list, Warden WARDEN) {
        this.list = list;
        this.WARDEN = WARDEN;
    }

    private static long period = 1_000;
    private Thread thread;
    private List<Bird> list;
    private static Bird LEADER_BIRD;
    private final Warden WARDEN;

    private void move(){

    }
}
