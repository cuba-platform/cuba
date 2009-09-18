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

import org.jboss.varia.scheduler.Schedulable;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Singleton providing "heartbeat" functionality.<br>
 * Notifies registered listeners on each beat. Beat frequency is set through
 * <code>haulmont.cuba:service=HeartbeatScheduler</code> scheduling MBean.
 */
public class Heartbeat {

    /**
     * Listener of the heartbeat.<br>
     * Implementations of <code>beat()</code> method must return as soon as possible,
     * do not use long operations (database, etc.)
     */
    public interface Listener {
        void beat();
    }

    public static class Starter implements Schedulable {
        public void perform(Date date, long l) {
            Heartbeat.getInstance().beat();
        }
    }

    private static final Heartbeat instance = new Heartbeat();

    private static final Map<Listener, Integer> listeners = new Hashtable<Listener, Integer>();

    private int current = 0;

    public static Heartbeat getInstance() {
        return instance;
    }

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

    private void beat() {
        current++;
        for (Map.Entry<Listener, Integer> entry : listeners.entrySet()) {
            Integer factor = entry.getValue();
            if (current % factor == 0)
                entry.getKey().beat();
        }
    }
}
