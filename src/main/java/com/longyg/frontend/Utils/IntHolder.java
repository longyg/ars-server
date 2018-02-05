package com.longyg.frontend.Utils;

public class IntHolder {
    private int number;

    public IntHolder(int number) {
        this.number = number;
    }

    public void increase() {
        this.number++;
    }

    public void decrease() {
        this.number--;
    }

    public int get() {
        return this.number;
    }
}

