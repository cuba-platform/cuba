/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Enables us to show screens based on {@link AbstractWindow} as a <b>frame</b>.
 */
@Deprecated
public class LegacyFragmentAdapter extends AbstractFrame {
    private AbstractWindow screen;

    @SuppressWarnings("ReassignmentInjectVariable")
    public LegacyFragmentAdapter(AbstractWindow legacyScreen) {
        this.screen = legacyScreen;
        this.messages = AppBeans.get(Messages.NAME);
    }

    /**
     * @return wrapper screen
     */
    public AbstractWindow getRealScreen() {
        return screen;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        screen.init(params);
    }

    @Override
    public WindowManager getWindowManager() {
        return getFrame().getWindowManager();
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        super.setDsContext(dsContext);

        screen.setDsContext(dsContext);
    }

    @Override
    public Component getOwnComponent(String id) {
        return screen.getOwnComponent(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return screen.getComponent(id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return screen.getOwnComponents();
    }

    @Override
    public Stream<Component> getOwnComponentsStream() {
        return screen.getOwnComponentsStream();
    }

    @Override
    public Collection<Component> getComponents() {
        return screen.getComponents();
    }

    @Override
    public Object getComponent() {
        return screen.getComponent();
    }

    @Override
    public Object getComposition() {
        return screen.getComposition();
    }

    @Override
    public void add(Component... childComponents) {
        screen.add(childComponents);
    }

    @Override
    public void remove(Component... childComponents) {
        screen.remove(childComponents);
    }

    @Nonnull
    @Override
    public Component getComponentNN(String id) {
        return screen.getComponentNN(id);
    }
}