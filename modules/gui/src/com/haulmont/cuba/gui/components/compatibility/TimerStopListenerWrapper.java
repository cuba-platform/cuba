package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Timer;

import java.util.function.Consumer;

@Deprecated
public class TimerStopListenerWrapper implements Consumer<Timer.TimerStopEvent> {

    protected final Timer.StopListener listener;

    public TimerStopListenerWrapper(Timer.StopListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimerStopListenerWrapper that = (TimerStopListenerWrapper) o;

        return listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }

    @Override
    public void accept(Timer.TimerStopEvent timerStopEvent) {
        listener.timerStopped(timerStopEvent.getSource());
    }
}
