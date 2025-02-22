package com.poultryfarm.habitat;


import com.poultryfarm.habitat.services.Service;

public class Painter extends Service {

    public Painter(Habitat habitat, long period) {
        super(habitat, period);
    }

    @Override
    public boolean isRunning() {
        return (!WARDEN.isPause() && WARDEN.isRunning());
    }

    @Override
    public void execute() {
        habitat.repaint();
    }
}
