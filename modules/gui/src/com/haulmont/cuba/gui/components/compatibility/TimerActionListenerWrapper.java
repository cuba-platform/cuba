package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Timer;

import java.util.function.Consumer;

@Deprecated
public class TimerActionListenerWrapper implements Consumer<Timer.TimerActionEvent> {

    protected final Timer.ActionListener listener;

    public TimerActionListenerWrapper(Timer.ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimerActionListenerWrapper that = (TimerActionListenerWrapper) o;

        return listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }

    @Override
    public void accept(Timer.TimerActionEvent timerActionEvent) {
        listener.timerAction(timerActionEvent.getSource());
    }
}
