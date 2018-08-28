package com.haulmont.cuba.gui.components.sys;

import com.haulmont.bali.events.EventHub;

/**
 * Object that supports generics event subscription mechanism.
 */
public interface EventHubOwner {
    /**
     * @return events storage
     */
    EventHub getEventHub();
}