/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.settings.Settings;
import org.dom4j.Element;

import java.util.List;

/**
 * Base class for simple screen controllers.
 *
 * @author Abramov
 * @version $Id$
 */
public class AbstractWindow extends AbstractFrame 
        implements Window, Component.HasXmlDescriptor, Window.Wrapper, Component.SecuredActionsHolder {

    public AbstractWindow() {
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
    public WindowContext getContext() {
        return (WindowContext) frame.getContext();
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
        addCloseListener(listener);
    }

    @Override
    public void removeListener(CloseListener listener) {
        removeCloseListener(listener);
    }

    @Override
    public void addCloseListener(CloseListener listener) {
        ((Window) frame).addCloseListener(listener);
    }

    @Override
    public void removeCloseListener(CloseListener listener) {
        ((Window) frame).removeCloseListener(listener);
    }

    /**
     * @return screen caption which is set in XML or via {@link #setCaption(String)}
     */
    @Override
    public String getCaption() {
        return ((Window) frame).getCaption();
    }

    /**
     * Set the screen caption. If called in {@link #init(java.util.Map)}, overrides the value from XML.
     * @param caption   caption
     */
    @Override
    public void setCaption(String caption) {
        ((Window) frame).setCaption(caption);
    }

    /**
     * Screen description is used by the framework to show some specified information, e.g. current filter or folder
     * name. We don't recommend to use it in application code.
     */
    @Override
    public String getDescription() {
        return ((Window) frame).getDescription();
    }

    /**
     * Screen description is used by the framework to show some specified information, e.g. current filter or folder
     * name. We don't recommend to use it in application code.
     */
    @Override
    public void setDescription(String description) {
        ((Window) frame).setDescription(description);
    }

    /** For internal use only. Don't call from application code. */
    @Override
    public Window getWrappedWindow() {
        return (Window) frame;
    }

    @Override
    public void applySettings(Settings settings) {
        ((Window) frame).applySettings(settings);
    }

    @Override
    public void saveSettings() {
        ((Window) frame).saveSettings();
    }

    @Override
    public void deleteSettings() {
        ((Window) frame).deleteSettings();
    }

    @Override
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

    @Override
    public boolean validate(List<Validatable> fields) {
        return ((Window) frame).validate(fields);
    }

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification. This method also calls {@link #postValidate(ValidationErrors)} hook to
     * support additional validation.
     * <p>You should override this method in subclasses ONLY if you want to completely replace the validation process,
     * otherwise use {@link #postValidate(ValidationErrors)}.
     * @return true if the validation was successful, false if there were any problems
     */
    @Override
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
     * Hook to be implemented in subclasses. <br/>
     * Called by the framework after the screen is fully initialized and opened. <br/>
     * Override this method and put custom initialization logic here.
     */
    public void ready() {
    }

    /**
     * Hook to be implemented in subclasses. Called by {@link #validateAll()} at the end of standard validation.
     * @param errors the list of validation errors. Caller fills it by errors found during the default validation.
     * Overridden method should add into it errors found by custom validation.
     */
    protected void postValidate(ValidationErrors errors) {
    }

    /**
     * Hook to be implemented in subclasses. Called by the framework before closing the screen.
     * @param actionId  a string that is passed to one of {@link #close} methods by calling code to identify itself.
     *                  Can be an {@link Action} ID, or a constant like {@link #COMMIT_ACTION_ID} or
     *                  {@link #CLOSE_ACTION_ID}.
     * @return          true to proceed with closing, false to interrupt and leave the screen open
     */
    protected boolean preClose(String actionId) {
        return true;
    }

    /**
     * Close the screen.
     * <p/> If the screen has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * the confirmation dialog will be shown.
     * <p/> Don't override this method in subclasses, use hook {@link #preClose(String)}
     *
     * @param actionId action ID that will be propagated to attached {@link CloseListener}s.
     *                 Use {@link #COMMIT_ACTION_ID} if some changes have just been committed, or
     *                 {@link #CLOSE_ACTION_ID} otherwise. These constants are recognized by various mechanisms of the
     *                 framework.
     */
    @Override
    public boolean close(String actionId) {
        return ((Window) frame).close(actionId);
    }

    /** Close the screen.
     * <p/> If the window has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * and force=false, the confirmation dialog will be shown.
     * <p/> Don't override this method in subclasses, use hook {@link #preClose(String)}
     *
     * @param actionId action ID that will be propagated to attached {@link CloseListener}s.
     *                 Use {@link #COMMIT_ACTION_ID} if some changes have just been committed, or
     *                 {@link #CLOSE_ACTION_ID} otherwise. These constants are recognized by various mechanisms of the
     *                 framework.
     * @param force    if true, no confirmation dialog will be shown even if the screen has uncommitted changes
     */
    @Override
    public boolean close(String actionId, boolean force) {
        return ((Window) frame).close(actionId, force);
    }

    /** For internal use only. Don't call or override in application code. */
    @Override
    public void closeAndRun(String actionId, Runnable runnable) {
        ((Window) frame).closeAndRun(actionId, runnable);
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        if (frame instanceof SecuredActionsHolder) {
            return ((SecuredActionsHolder) frame).getActionsPermissions();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}