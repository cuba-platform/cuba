/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.WebAbstractFacet;
import com.haulmont.cuba.web.widgets.CubaTimer;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.Component;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class WebTimer extends WebAbstractFacet implements Timer {

    private static final Logger log = LoggerFactory.getLogger(WebTimer.class);

    protected CubaTimer timerImpl;

    public WebTimer() {
        timerImpl = new CubaTimer();
    }

    @Override
    public void start() {
        timerImpl.start();
    }

    @Override
    public void stop() {
        timerImpl.stop();
    }

    @Override
    public boolean isRepeating() {
        return timerImpl.isRepeating();
    }

    @Override
    public void setRepeating(boolean repeating) {
        timerImpl.setRepeating(repeating);
    }

    @Override
    public int getDelay() {
        return timerImpl.getDelay();
    }

    @Override
    public void setDelay(int delay) {
        timerImpl.setDelay(delay);
    }

    @Override
    public Subscription addTimerActionListener(Consumer<TimerActionEvent> listener) {
        Consumer<CubaTimer> wrapper = new CubaTimerActionListenerWrapper(listener);
        timerImpl.addActionListener(wrapper);
        return () -> timerImpl.removeActionListener(wrapper);
    }

    @Override
    @Deprecated
    public void removeTimerActionListener(Consumer<TimerActionEvent> listener) {
        timerImpl.removeActionListener(new CubaTimerActionListenerWrapper(listener));
    }

    @Override
    public Subscription addTimerStopListener(Consumer<TimerStopEvent> listener) {
        Consumer<CubaTimer> wrapper = new CubaTimerStopListenerWrapper(listener);
        timerImpl.addStopListener(wrapper);
        return () -> timerImpl.removeStopListeners(wrapper);
    }

    @Override
    @Deprecated
    public void removeTimerStopListener(Consumer<TimerStopEvent> listener) {
        timerImpl.removeStopListeners(new CubaTimerStopListenerWrapper(listener));
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        timerImpl.setTimerId(id);
    }

    @Override
    public void setOwner(Frame owner) {
        super.setOwner(owner);

        if (owner != null) {
            registerInUI(owner);
        }
    }

    protected void registerInUI(Frame owner) {
        Component ownerComponent = owner.unwrap(com.vaadin.ui.Component.class);

        if (ownerComponent.isAttached()) {
            attachTimerToUi(ownerComponent);
        } else {
            registerOnAttach(ownerComponent);
        }

        addDetachListener(owner);
    }

    private void addDetachListener(Frame owner) {
        Screen screen = UiControllerUtils.getScreen(owner.getFrameOwner());
        UiControllerUtils.addAfterDetachListener(screen,
                event -> detachTimerExtension()
        );
    }

    protected void detachTimerExtension() {
        if (timerImpl.getParent() != null) {
            timerImpl.remove();
        }
        log.trace("Timer '{}' unregistered from UI ", WebTimer.this.getId());
    }

    protected void registerOnAttach(Component ownerComponent) {
        ownerComponent.addAttachListener(new ClientConnector.AttachListener() {
            @Override
            public void attach(ClientConnector.AttachEvent event) {
                attachTimerToUi((Component) event.getConnector());
                // execute attach listener only once
                event.getConnector().removeAttachListener(this);
            }
        });
    }

    protected void attachTimerToUi(Component ownerComponent) {
        AppUI appUI = (AppUI) ownerComponent.getUI();
        appUI.addTimer(timerImpl);

        log.trace("Timer '{}' registered in UI ", getId());
    }

    protected class CubaTimerActionListenerWrapper implements Consumer<CubaTimer> {

        private final Consumer<TimerActionEvent> listener;

        public CubaTimerActionListenerWrapper(Consumer<TimerActionEvent> listener) {
            this.listener = listener;
        }

        @Override
        public void accept(CubaTimer sender) {
            try {
                listener.accept(new TimerActionEvent(WebTimer.this));
            } catch (RuntimeException e) {
                int reIdx = ExceptionUtils.indexOfType(e, RemoteException.class);
                if (reIdx > -1) {
                    RemoteException re = (RemoteException) ExceptionUtils.getThrowableList(e).get(reIdx);
                    for (RemoteException.Cause cause : re.getCauses()) {
                        if (cause.getThrowable() instanceof NoUserSessionException) {
                            log.warn("NoUserSessionException in timer {}, timer will be stopped", id != null ? id : "<noid>");
                            stop();
                            return;
                        }
                    }
                } else if (ExceptionUtils.indexOfThrowable(e, NoUserSessionException.class) > -1) {
                    log.warn("NoUserSessionException in timer {}, timer will be stopped", id != null ? id : "<noid>");
                    stop();
                    return;
                }

                throw new RuntimeException("Exception on timer action", e);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            CubaTimerActionListenerWrapper that = (CubaTimerActionListenerWrapper) obj;

            return this.listener.equals(that.listener);
        }

        @Override
        public int hashCode() {
            return listener.hashCode();
        }
    }

    protected class CubaTimerStopListenerWrapper implements Consumer<CubaTimer> {

        private final Consumer<TimerStopEvent> listener;

        public CubaTimerStopListenerWrapper(Consumer<TimerStopEvent> listener) {
            this.listener = listener;
        }

        @Override
        public void accept(CubaTimer sender) {
            listener.accept(new TimerStopEvent(WebTimer.this));
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            CubaTimerStopListenerWrapper that = (CubaTimerStopListenerWrapper) obj;

            return this.listener.equals(that.listener);
        }

        @Override
        public int hashCode() {
            return listener.hashCode();
        }
    }
}