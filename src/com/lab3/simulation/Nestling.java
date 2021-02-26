package com.lab3.simulation;

import javax.swing.*;
import java.awt.*;

public class Nestling extends Bird {
    private static long lifeTime = 10_000;

    public Nestling(Dimension frameSize, ImageIcon imageIcon) {
        super(frameSize, imageIcon, new Dimension(35,60));
    }

    @Override
    public long getLifeTime() {
        return lifeTime;
    }

    public static long getLifeTimeStatic(){
        return lifeTime;
    }

    public static void setLifeTime(long value) throws Exception {
        if (value < 0) throw new Exception("Некорректное значение времени рождения");
        lifeTime = value;
    }

}
