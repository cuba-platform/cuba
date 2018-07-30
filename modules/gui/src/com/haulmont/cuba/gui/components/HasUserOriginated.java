package com.haulmont.cuba.gui.components;

import java.io.Serializable;

public interface HasUserOriginated extends Serializable {
    /**
     * Returns whether this event was triggered by user interaction or programmatically.
     *
     * @return {@code true} if this event originates by user interaction, {@code false} otherwise.
     */
    public boolean isUserOriginated();
}
