/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.ui.Window;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AppTimers {

    protected App app;

    protected Map<Window, WindowTimers> windowTimers = new HashMap<>();
    protected Map<Timer, Window> timerWindow = new HashMap<>();

    protected boolean stopTimers = false;

    public AppTimers(App app) {
        this.app = app;
    }

    /**
     * Adds a timer on the application level
     * @param timer new timer
     */
    public void add(final Timer timer) {
        if (!timerWindow.containsKey(timer))
            add(timer, null, app.getCurrentWindow());
    }

    /**
     * Adds a timer for the defined window
     * @param timer new timer
     * @param owner component that owns a timer
     */
    public void add(final Timer timer, com.haulmont.cuba.gui.components.Window owner) {
        if (!timerWindow.containsKey(timer))
            add(timer, owner, app.getCurrentWindow());
    }

    /**
     * Do not use this method in application code
     */
    public void add(final Timer timer, Window mainWindow) {
        if (!timerWindow.containsKey(timer))
            add(timer, null, mainWindow);
    }

    /**
     * Do not use this method in application code
     */
    public void add(final Timer timer, com.haulmont.cuba.gui.components.Window owner, Window mainWindow) {
        WindowTimers wt = windowTimers.get(mainWindow);
        if (wt == null) {
            wt = new WindowTimers();
            windowTimers.put(mainWindow, wt);
        }

        if (wt.timers.add(timer)) {
            timerWindow.put(timer, mainWindow);

            timer.addListener(new Timer.Listener() {
                @Override
                public void onTimer(Timer timer) {
                }

                @Override
                public void onStopTimer(Timer timer) {
                    Window window = timerWindow.remove(timer);
                    if (window != null) {
                        WindowTimers wt = windowTimers.get(window);
                        if (wt != null) {
                            wt.timers.remove(timer);
                            if (timer instanceof WebTimer.WebTimerImpl) {
                                wt.idTimers.remove(timer.getDebugId());
                            }
                        }
                    }

                    timer.removeListener(this);
                }
            });

            if (timer instanceof WebTimer.WebTimerImpl) {
                // do not add new timer listener if timer already has owner
                if (owner != null && timer.getOwner() == null) {
                    timer.setOwner(owner);
                    owner.addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
                        @Override
                        public void windowClosed(String actionId) {
                            timer.stop();
                        }
                    });
                }
                if (timer.getDebugId() != null) {
                    wt.idTimers.put(timer.getDebugId(), timer);
                }
            }
        }
    }

    public void stopAll() {
        Set<Timer> timers = new HashSet<>(timerWindow.keySet());
        for (final Timer timer : timers) {
            if (timer != null && !timer.isStopped()) {
                timer.stop();
            }
        }
        stopTimers = true;
    }

    /**
     * Returns a timer by id
     * @param id timer id
     * @return timer or <code>null</code>
     */
    public Timer getTimer(String id) {
        Window currentWindow = app.getCurrentWindow();
        WindowTimers wt = windowTimers.get(currentWindow);
        if (wt != null) {
            return wt.idTimers.get(id);
        } else {
            return null;
        }
    }

    /**
     * Call after remove window
     * @param window removing window
     */
    public void removeWindow(Window window) {
        if (windowTimers.containsKey(window)) {
            for (Timer timer : windowTimers.get(window).timers) {
                timerWindow.remove(timer);
            }
            windowTimers.remove(window);
        }
    }

    /**
     * Do not use this method in application code
     * @param currentWindow current window
     * @return collection of timers that applied for the current window
     */
    public Collection<Timer> getAll(Window currentWindow) {
        if (stopTimers) {
            try {
                return Collections.unmodifiableSet(timerWindow.keySet());
            } finally {
                stopTimers = false;
            }
        } else {
            WindowTimers wt = windowTimers.get(currentWindow);
            if (wt != null) {
                return Collections.unmodifiableSet(wt.timers);
            } else {
                return Collections.emptySet();
            }
        }
    }

    protected static class WindowTimers {
        protected Map<String, Timer> idTimers = new HashMap<>();
        protected Set<Timer> timers = new HashSet<>();
    }
}