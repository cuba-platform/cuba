/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.compatibility.TimerActionListenerWrapper;
import com.haulmont.cuba.gui.components.compatibility.TimerStopListenerWrapper;

import java.util.EventObject;
import java.util.function.Consumer;

public interface Timer extends Component.BelongToFrame {

    String NAME = "timer";

    boolean isRepeating();
    void setRepeating(boolean repeating);

    /**
     * @return delay in milliseconds.
     */
    int getDelay();

    /**
     * @param delayMs delay in milliseconds.
     */
    void setDelay(int delayMs);

    /**
     * Starts timer. If timer is already started call will be ignored.
     */
    void start();

    /**
     * Stops timer if it is running.
     */
    void stop();

    /**
     * Listener for timer events.
     */
    interface ActionListener {
        void timerAction(Timer timer);
    }

    /**
     * Listener for timer stop event.
     */
    interface StopListener {
        void timerStopped(Timer timer);
    }

    /**
     * @deprecated Use {@link #addTimerActionListener(Consumer)} instead
     */
    @Deprecated
    default void addActionListener(ActionListener listener) {
        addTimerActionListener(new TimerActionListenerWrapper(listener));
    }

    /**
     * @deprecated Use {@link #removeTimerActionListener(Consumer)} instead
     */
    @Deprecated
    default void removeActionListener(ActionListener listener) {
        removeTimerActionListener(new TimerActionListenerWrapper(listener));
    }

    /**
     * @deprecated Use {@link #addTimerStopListener(Consumer)} instead
     */
    @Deprecated
    default void addStopListener(StopListener listener) {
        addTimerStopListener(new TimerStopListenerWrapper(listener));
    }

    /**
     * @deprecated Use {@link #removeTimerStopListener(Consumer)} instead
     */
    @Deprecated
    default void removeStopListener(StopListener listener) {
        removeTimerStopListener(new TimerStopListenerWrapper(listener));
    }

    Subscription addTimerActionListener(Consumer<TimerActionEvent> listener);
    void removeTimerActionListener(Consumer<TimerActionEvent> listener);

    Subscription addTimerStopListener(Consumer<TimerStopEvent> listener);
    void removeTimerStopListener(Consumer<TimerStopEvent> listener);

    class TimerActionEvent extends EventObject {
        public TimerActionEvent(Timer source) {
            super(source);
        }

        @Override
        public Timer getSource() {
            return (Timer) super.getSource();
        }
    }

    class TimerStopEvent extends EventObject {
        public TimerStopEvent(Timer source) {
            super(source);
        }

        @Override
        public Timer getSource() {
            return (Timer) super.getSource();
        }
    }
}