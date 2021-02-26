package com.lab3.simulation;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class AdultBird extends Bird {
    private static long lifeTime = 20_000;

    public AdultBird(Dimension frameSize, ImageIcon imageIcon) {
        super(frameSize, imageIcon, new Dimension(70,110));
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

