package com.lab3.simulation.birds;

import com.lab3.simulation.habitat.Warden;

import java.util.List;

public class AdultBirdAI extends BaseAI{
    public AdultBirdAI(List<Bird> list, Warden WARDEN) {
        super(list, WARDEN);
    }

    @Override
    void move() {
        synchronized (list){
            for (Bird b : list){
                b.move();
            }
        }
    }
}
