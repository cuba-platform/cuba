/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.gui.settings.Settings;
import org.dom4j.Element;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopWindow implements  Window, Component.Wrapper, Component.HasXmlDescriptor, WrappedWindow
{
    private static final long serialVersionUID = 1026363207247384464L;

    protected JPanel panel;

    protected String id;

    protected Window wrapper;

    protected Map<String, Component> allComponents = new HashMap<String, Component>();

    private DsContext dsContext;
    private WindowContext context;

    public DesktopWindow() {
        panel = new JPanel();
    }

    public Element getXmlDescriptor() {
        return null;
    }

    public void setXmlDescriptor(Element element) {
    }

    public void addListener(CloseListener listener) {
    }

    public void removeListener(CloseListener listener) {
    }

    public void applySettings(Settings settings) {
    }

    public void saveSettings() {
    }

    public Settings getSettings() {
        return null;
    }

    public boolean close(String actionId) {
        return false;
    }

    public boolean close(String actionId, boolean force) {
        return false;
    }

    public void closeAndRun(String actionId, Runnable runnable) {
    }

    public void addTimer(Timer timer) {
    }

    public Timer getTimer(String id) {
        return null;
    }

    public void addAction(Action action) {
    }

    public void removeAction(Action action) {
    }

    public Collection<Action> getActions() {
        return null;
    }

    public Action getAction(String id) {
        return null;
    }

    public String getCaption() {
        return null;
    }

    public void setCaption(String caption) {
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public WindowContext getContext() {
        return context;
    }

    public void setContext(WindowContext ctx) {
        context = ctx;
    }

    public DsContext getDsContext() {
        return dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public String getMessagesPack() {
        return null;
    }

    public void setMessagesPack(String name) {
    }

    public String getMessage(String key) {
        return null;
    }

    public void registerComponent(Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
    }

    public DialogParams getDialogParams() {
        return null;
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return null;
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        return null;
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return null;
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return null;
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return null;
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return null;
    }

    public <T extends Window> T openLookup(String windowAlias, Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return null;
    }

    public <T extends Window> T openLookup(String windowAlias, Lookup.Handler handler, WindowManager.OpenType openType) {
        return null;
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        return null;
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        return null;
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
    }

    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
    }

    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
    }

    public void showNotification(String caption, NotificationType type) {
    }

    public void showNotification(String caption, String description, NotificationType type) {
    }

    public <A extends IFrame> A getFrame() {
        return null;
    }

    public void setFrame(IFrame frame) {
    }

    public void expand(Component component, String height, String width) {
    }

    public void add(Component component) {
    }

    public void remove(Component component) {
    }

    public <T extends Component> T getOwnComponent(String id) {
        return null;
    }

    public <T extends Component> T getComponent(String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            return (T) allComponents.get(id);
        } else {
            Component frame = allComponents.get(elements[0]);
            if (frame != null && frame instanceof Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return (T) ((Container) frame).getComponent(subPath);
            } else
                return null;
        }
    }

    public Collection<Component> getOwnComponents() {
        return null;
    }

    public Collection<Component> getComponents() {
        return null;
    }

    public void expandLayout(boolean expandLayout) {
    }

    public <T> T getComponent() {
        return (T) panel;
    }

    public JComponent getComposition() {
        return panel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        return null;
    }

    public void setDebugId(String id) {
    }

    public boolean isEnabled() {
        return false;
    }

    public void setEnabled(boolean enabled) {
    }

    public boolean isVisible() {
        return false;
    }

    public void setVisible(boolean visible) {
    }

    public void requestFocus() {
    }

    public float getHeight() {
        return 0;
    }

    public int getHeightUnits() {
        return 0;
    }

    public void setHeight(String height) {
    }

    public float getWidth() {
        return 0;
    }

    public int getWidthUnits() {
        return 0;
    }

    public void setWidth(String width) {
    }

    public Alignment getAlignment() {
        return null;
    }

    public void setAlignment(Alignment alignment) {
    }

    public String getStyleName() {
        return null;
    }

    public void setStyleName(String name) {
    }

    public void setMargin(boolean enable) {
    }

    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
    }

    public void setSpacing(boolean enabled) {
    }

    public Window wrapBy(Class<Window> aClass) {
        try {
            Constructor<?> constructor;
            try {
                constructor = aClass.getConstructor(Window.class);
            } catch (NoSuchMethodException e) {
                constructor = aClass.getConstructor(IFrame.class);
            }

            wrapper = (Window) constructor.newInstance(this);
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Window getWrapper() {
        return wrapper;
    }
}
