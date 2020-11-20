package com.google.ar.core.examples.java.Model;

import java.io.Serializable;

public class GameItem implements Serializable {

    private String description;
    private int imageDrawable;
    private boolean lock;

    public GameItem(String description, int imageDrawable, boolean lock) {
        this.description = description;
        this.imageDrawable = imageDrawable;
        this.lock = lock;
    }

    public boolean getLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }
}
