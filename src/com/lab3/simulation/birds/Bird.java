package com.lab3.simulation.birds;

import com.lab3.simulation.IBehaviour;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public abstract class Bird implements IBehaviour {
    private int x = 0;
    private int y = 0;
    private int vX = (int) (Math.random() * (10 + 1) + -5) + 1;
    private int vY = (int) (Math.random() * (10 + 1) + -5) + 1;
    private final static Set<Long> ID_SET = new TreeSet<>();

    public void setVX(int vX) {
        this.vX = vX;
    }

    public void setVY(int vY) {
        this.vY = vY;
    }

    private static Random random = new Random();
    private long ID;
    private boolean isDead = false;
    private ImageIcon imageIcon;
    private final static ImageIcon DeadImageIcon = new ImageIcon(Bird.class.getResource("/kfc.png"));
    private static long deadTime = 5_000;
    protected int ImageH = 50;
    protected int ImageW = 50;

    public boolean isOutOfX() {
        return outOfX;
    }

    public boolean isOutOfY() {
        return outOfY;
    }

    private boolean outOfX = false;
    private boolean outOfY = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bird bird = (Bird) o;
        return ID == bird.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public long getID() {
        return ID;
    }

    public Bird() {
        do {
            ID = random.nextInt(Integer.MAX_VALUE);
        } while (ID_SET.contains(ID));
        ID_SET.add(ID);
    }

    public Bird(Dimension frameSize, Dimension imageSize) {
        this();
        setImageSize(imageSize);
        Random rng = new Random();
        x = rng.nextInt(frameSize.width - ImageW);
        y = rng.nextInt(frameSize.height - ImageH);
    }

    public Bird(Dimension frameSize, ImageIcon imageIcon, Dimension imageSize) {
        this(frameSize, imageSize);
        this.imageIcon = imageIcon;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void setImageSize(Dimension imageSize) {
        this.ImageH = imageSize.height;
        this.ImageW = imageSize.width;
    }

    public abstract long getLifeTime();

    public boolean isDead() {
        return isDead;
    }

    public void getKilled() {
        vX = 0;
        vY = 0;
        setImageSize(new Dimension(50, 65));
        isDead = true;
        setImageIcon(DeadImageIcon);
    }


    public long getDeadTime() {
        return deadTime;
    }

    public static void IDsRemoveAll(Collection<Long> c) {
        ID_SET.removeAll(c);
    }

    @Override
    public void paint(Graphics g) {
        if (x < 0 || (x >= g.getClipBounds().width - ImageW)) {
            outOfX = true;
            vX *=-1;
        } else outOfX = false;
        if (y < 0 || y >= g.getClipBounds().height - ImageH) {
            outOfY = true;
            vY*=-1;
        } else outOfY = false;
        g.drawImage(imageIcon.getImage(), x, y, ImageW, ImageH, null);
    }

    public void move() {
        x += vX;
        y += vY;
    }

    public int getVX() {
        return vX;
    }

    public int getVY() {
        return vY;
    }
}
