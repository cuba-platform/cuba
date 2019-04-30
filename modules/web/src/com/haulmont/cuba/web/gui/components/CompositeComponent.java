/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.google.common.base.Preconditions;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.web.AppUI;
import org.apache.commons.lang3.StringUtils;

import java.util.EventObject;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class CompositeComponent<T extends Component>
        implements Component, Component.BelongToFrame, AttachNotifier, HasDebugId {

    protected String id;
    protected T root;
    protected Frame frame;

    // private, lazily initialized
    private EventHub eventHub = null;

    protected EventHub getEventHub() {
        if (eventHub == null) {
            eventHub = new EventHub();
        }
        return eventHub;
    }

    public T getComposition() {
        return root;
    }

    protected T getCompositionNN() {
        Preconditions.checkState(root != null, "Composition root is not initialized");
        return root;
    }

    @SuppressWarnings("unchecked")
    protected <C> C getInnerComponent(String id) {
        return (C) getInnerComponentOptional(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("Not found component with id '%s'", id)));
    }

    @SuppressWarnings("unchecked")
    protected <C> Optional<C> getInnerComponentOptional(String id) {
        Preconditions.checkState(getCompositionNN() instanceof ComponentContainer,
                "Composition can't contain inner components");

        return (Optional<C>) Optional.ofNullable(((ComponentContainer) getCompositionNN()).getComponent(id));
    }

    protected void setComposition(T composition) {
        Preconditions.checkState(root == null, "Composition root is already initialized");
        this.root = composition;
    }

    protected <E> void fireEvent(Class<E> eventType, E event) {
        getEventHub().publish(eventType, event);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        if (!Objects.equals(this.id, id)) {
            if (frame != null) {
                ((FrameImplementation) frame).unregisterComponent(this);
            }

            this.id = id;

            AppUI ui = AppUI.getCurrent();
            if (ui != null) {
                if (root != null && ui.isTestMode()) {
                    com.vaadin.ui.Component vComponent = root.unwrap(com.vaadin.ui.Component.class);
                    if (vComponent != null) {
                        vComponent.setCubaId(id);
                    }
                }
            }

            assignDebugId();

            if (frame != null) {
                ((FrameImplementation) frame).registerComponent(this);
            }
        }
    }

    protected void assignDebugId() {
        AppUI ui = AppUI.getCurrent();
        if (ui == null) {
            return;
        }

        if (root == null
                || frame == null
                || StringUtils.isEmpty(frame.getId())) {
            return;
        }

        if (ui.isPerformanceTestMode() && getDebugId() == null) {
            String fullFrameId = ComponentsHelper.getFullFrameId(frame);
            TestIdManager testIdManager = ui.getTestIdManager();

            String alternativeId = id != null ? id : getClass().getSimpleName();
            String candidateId = fullFrameId + "." + alternativeId;

            setDebugId(testIdManager.getTestId(candidateId));
        }
    }

    @Override
    public String getDebugId() {
        return ((HasDebugId) getCompositionNN()).getDebugId();
    }

    @Override
    public void setDebugId(String id) {
        ((HasDebugId) getCompositionNN()).setDebugId(id);
    }

    @Override
    public Component getParent() {
        return getCompositionNN().getParent();
    }

    @Override
    public void setParent(Component parent) {
        if (getCompositionNN().getParent() != parent) {
            if (isAttached()) {
                detached();
            }

            getCompositionNN().setParent(parent);

            if (isAttached()) {
                attached();
            }
        }
    }

    @Override
    public boolean isAttached() {
        Component current = getCompositionNN().getParent();
        while (current != null) {
            if (current instanceof Window) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    @Override
    public void attached() {
        ((AttachNotifier) getCompositionNN()).attached();
        getEventHub().publish(AttachEvent.class, new AttachEvent(this));
    }

    @Override
    public void detached() {
        ((AttachNotifier) getCompositionNN()).detached();
        getEventHub().publish(DetachEvent.class, new DetachEvent(this));
    }

    @Override
    public Subscription addAttachListener(Consumer<AttachEvent> listener) {
        return getEventHub().subscribe(AttachEvent.class, listener);
    }

    @Override
    public Subscription addDetachListener(Consumer<DetachEvent> listener) {
        return getEventHub().subscribe(DetachEvent.class, listener);
    }

    @Override
    public boolean isEnabled() {
        return getCompositionNN().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getCompositionNN().setEnabled(enabled);
    }

    @Override
    public boolean isResponsive() {
        return getCompositionNN().isResponsive();
    }

    @Override
    public void setResponsive(boolean responsive) {
        getCompositionNN().setResponsive(responsive);
    }

    @Override
    public boolean isVisible() {
        return getCompositionNN().isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getCompositionNN().setVisible(visible);
    }

    @Override
    public boolean isVisibleRecursive() {
        return getCompositionNN().isVisibleRecursive();
    }

    @Override
    public boolean isEnabledRecursive() {
        return getCompositionNN().isEnabledRecursive();
    }

    @Override
    public float getHeight() {
        return getCompositionNN().getHeight();
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return getCompositionNN().getHeightSizeUnit();
    }

    @Override
    public void setHeight(String height) {
        getCompositionNN().setHeight(height);
    }

    @Override
    public float getWidth() {
        return getCompositionNN().getWidth();
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return getCompositionNN().getWidthSizeUnit();
    }

    @Override
    public void setWidth(String width) {
        getCompositionNN().setWidth(width);
    }

    @Override
    public Alignment getAlignment() {
        return getCompositionNN().getAlignment();
    }

    @Override
    public void setAlignment(Alignment alignment) {
        getCompositionNN().setAlignment(alignment);
    }

    @Override
    public String getStyleName() {
        return getCompositionNN().getStyleName();
    }

    @Override
    public void setStyleName(String styleName) {
        getCompositionNN().setStyleName(styleName);
    }

    @Override
    public void addStyleName(String styleName) {
        getCompositionNN().addStyleName(styleName);
    }

    @Override
    public void removeStyleName(String styleName) {
        getCompositionNN().removeStyleName(styleName);
    }

    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        return getCompositionNN().unwrap(internalComponentClass);
    }

    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        return getCompositionNN().unwrapComposition(internalCompositionClass);
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame = frame;

        if (frame instanceof FrameImplementation) {
            ((FrameImplementation) frame).registerComponent(this);
        }

        if (getComposition() instanceof BelongToFrame) {
            ((BelongToFrame) getComposition()).setFrame(frame);
        }

        if (getDebugId() == null) {
            assignDebugId();
        }
    }

    public static class CreateEvent extends EventObject {

        public CreateEvent(CompositeComponent source) {
            super(source);
        }

        @Override
        public CompositeComponent getSource() {
            return (CompositeComponent) super.getSource();
        }
    }
}
