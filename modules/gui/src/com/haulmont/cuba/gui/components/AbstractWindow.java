/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 28.01.2009 10:20:22
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.settings.Settings;
import org.dom4j.Element;

public class AbstractWindow extends AbstractFrame 
        implements Window, Component.HasXmlDescriptor, Window.Wrapper {

    public AbstractWindow() {
    }

    /**
     * DEPRECATED - use default constructor!
     */
    @Deprecated
    public AbstractWindow(IFrame frame) {
        super(frame);
    }

    @Override
    public Element getXmlDescriptor() {
        return ((HasXmlDescriptor) frame).getXmlDescriptor();
    }

    @Override
    public void setXmlDescriptor(Element element) {
        ((HasXmlDescriptor) frame).setXmlDescriptor(element);
    }

    @Override
    public DsContext getDsContext() {
        return frame.getDsContext();
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        frame.setDsContext(dsContext);
    }

    @Override
    public void addListener(CloseListener listener) {
        ((Window) frame).addListener(listener);
    }

    @Override
    public void removeListener(CloseListener listener) {
        ((Window) frame).removeListener(listener);
    }

    @Override
    public String getCaption() {
        return ((Window) frame).getCaption();
    }

    @Override
    public void setCaption(String caption) {
        ((Window) frame).setCaption(caption);
    }

    @Override
    public String getDescription() {
        return ((Window) frame).getDescription();
    }

    @Override
    public void setDescription(String description) {
        ((Window) frame).setDescription(description);
    }

    @Override
    public <T extends Window> T getWrappedWindow() {
        return (T) frame;
    }

    @Override
    public void applySettings(Settings settings) {
        ((Window) frame).applySettings(settings);
    }

    public void saveSettings() {
        ((Window) frame).saveSettings();
    }

    public void setFocusComponent(String componentId) {
        ((Window) frame).setFocusComponent(componentId);
    }

    @Override
    public String getFocusComponent() {
        return ((Window) frame).getFocusComponent();
    }

    @Override
    public Settings getSettings() {
        return ((Window) frame).getSettings();
    }

    @Override
    public void addTimer(Timer timer) {
        ((Window) frame).addTimer(timer);
    }

    @Override
    public Timer getTimer(String id) {
        return ((Window) frame).getTimer(id);
    }

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification. This method also calls {@link #postValidate(ValidationErrors)} hook to
     * support additional validation.
     * <p>You should override this method in subclasses ONLY if you want to completely replace the validation process,
     * otherwise use {@link #postValidate(ValidationErrors)}.
     * @return true if the validation was succesful, false if there were any problems
     */
    public boolean validateAll() {
        return ((Window) frame).validateAll();
    }

    @Override
    public WindowManager getWindowManager() {
        return ((Window) frame).getWindowManager();
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        ((Window) frame).setWindowManager(windowManager);
    }

    /**
     * Hook to be implemented in subclasses. Called by {@link #validateAll()} at the end of standard validation.
     * @param errors the list of validation errors. Caller fills it by errors found during the default validation.
     * Overridden method should add into it errors found by custom validation.
     */
    protected void postValidate(ValidationErrors errors) {
    }

}
