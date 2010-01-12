/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 08.07.2009 15:13:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import javax.annotation.ManagedBean;
import java.util.Hashtable;
import java.util.Map;

/**
 * Bean providing "heartbeat" functionality.<br>
 * Notifies registered listeners on each beat. Beat frequency is set in cuba_Heartbeat schedulable (see cuba-beans.xml).
 */
@ManagedBean("cuba_Heartbeat")
public class Heartbeat {

    /**
     * Listener of the heartbeat.<br>
     * Implementations of <code>beat()</code> method must return as soon as possible,
     * do not use long operations (database, etc.)
     */
    public interface Listener {
        void beat();
    }

    private final Map<Listener, Integer> listeners = new Hashtable<Listener, Integer>();

    private int current = 0;

    /**
     * Register heartbeat listener
     */
    public void addListener(Listener listener, int factor) {
        if (factor <= 0)
            throw new IllegalArgumentException("factor must be positive integer");

        listeners.put(listener, factor);
    }

    /**
     * Unregister heartbeat listener
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void beat() {
        current++;
        for (Map.Entry<Listener, Integer> entry : listeners.entrySet()) {
            Integer factor = entry.getValue();
            if (current % factor == 0)
                entry.getKey().beat();
        }
    }
}
