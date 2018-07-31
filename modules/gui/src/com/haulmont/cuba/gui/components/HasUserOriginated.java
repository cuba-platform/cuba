package com.haulmont.cuba.gui.components;

public interface HasUserOriginated {
    /**
     * Returns whether this event was triggered by user interaction or programmatically.
     *
     * @return {@code true} if this event originates by user interaction, {@code false} otherwise.
     */
    boolean isUserOriginated();
}
