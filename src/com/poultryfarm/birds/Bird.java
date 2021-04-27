package com.poultryfarm.birds;

import com.poultryfarm.IBehaviour;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public abstract class Bird implements IBehaviour, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int x = 0;
    private int y = 0;
    protected int vX = (int) (Math.random() * (10 + 1) + -5) + 1;
    protected int vY = (int) (Math.random() * (10 + 1) + -5) + 1;
    private final static Set<Long> ID_SET = new TreeSet<>();

    public void setVelocity(int vX, int vY) {
        this.vX = vX;
        this.vY = vY;
        stopped = false;
    }

    private static Random random = new Random();
    private long ID;
    private boolean isDead = false;
    private boolean stopped = false;
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
        if ((x < 0 || (x >= g.getClipBounds().width - ImageW)) && !isDead && !stopped) {
            outOfX = true;
            getStopped();
        } else outOfX = false;
        if ((y < 0 || y >= g.getClipBounds().height - ImageH) && !isDead && !stopped) {
            outOfY = true;
            getStopped();
        } else outOfY = false;
        g.drawImage(imageIcon.getImage(), x, y, ImageW, ImageH, null);
    }

    @Override
    public String toString() {
        return "Bird{" +
                "x=" + x +
                ", y=" + y +
                ", ID=" + ID +
                '}';
    }

    private void getStopped() {
        vX *= -1;
        vY *= -1;
        move();
        stopped = true;
        vX = vY = 0;
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
