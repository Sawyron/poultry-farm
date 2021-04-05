package com.lab3.simulation.habitat;

public abstract class Command {
    protected Habitat habitat;
    abstract void execute(String... args);
}
