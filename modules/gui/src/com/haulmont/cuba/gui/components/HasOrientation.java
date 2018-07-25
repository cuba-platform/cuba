package com.haulmont.cuba.gui.components;

public interface HasOrientation {

    Orientation getOrientation();
    void setOrientation(Orientation orientation);

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }
}
