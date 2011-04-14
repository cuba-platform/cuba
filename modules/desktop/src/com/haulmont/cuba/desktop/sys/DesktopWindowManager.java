/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopWindowManager extends WindowManager {

    protected final Map<JComponent, WindowBreadCrumbs> tabs = new HashMap<JComponent, WindowBreadCrumbs>();
    protected final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<Window, WindowOpenMode>();
    protected final Map<WindowBreadCrumbs,Stack<Map.Entry<Window,Integer>>> stacks = new HashMap<WindowBreadCrumbs,Stack<Map.Entry<Window,Integer>>>();
    protected final Map<Window,Integer> windows = new HashMap<Window,Integer>();

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<Window>(windowOpenMode.keySet());
    }

    @Override
    protected void putToWindowMap(Window window, Integer hashCode) {
        if (window != null) {
            windows.put(window, hashCode);
        }
    }

    @Override
    protected Window getWindow(Integer hashCode) {
        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        for (Map.Entry<Window, Integer> entry : set) {
            if (hashCode.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void checkCanOpenWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
    }

    @Override
    protected void showWindow(Window window, String caption, OpenType openType) {
        showWindow(window, caption, null, openType);
    }

    @Override
    protected void showWindow(Window window, String caption, String description, OpenType openType) {
        window.setCaption(caption);
        window.setDescription(description);

        JComponent jComponent = null;

        final WindowOpenMode openMode = new WindowOpenMode(window, openType);

        boolean newTab = true;
        switch (openType) {
            case NEW_TAB:
                JComponent tab = findTab(window);
                if (tab != null) {
                    App.getInstance().getTabsPane().setSelectedComponent(tab);
                    jComponent = tab;
                    newTab = false;
                } else {
                    jComponent = showWindowNewTab(window, caption, description);
                }
                break;
            case THIS_TAB:
                break;
            case DIALOG:
                break;
            default:
                throw new UnsupportedOperationException();
        }

        openMode.setData(jComponent);

        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            windowOpenMode.put(wrappedWindow, openMode);
        } else {
            windowOpenMode.put(window, openMode);
        }

        afterShowWindow(window, newTab);
    }

    protected JComponent showWindowNewTab(Window window, String caption, String description) {
        final WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs();
        breadCrumbs.addListener(
                new WindowBreadCrumbs.Listener() {
                    public void windowClick(final Window window) {
                        Runnable op = new Runnable() {
                            public void run() {
                                Window currentWindow = breadCrumbs.getCurrentWindow();

                                if (currentWindow != null && window != currentWindow) {
                                    currentWindow.closeAndRun("close", this);
                                }
                            }
                        };
                        op.run();
                    }
                }
        );
        breadCrumbs.addWindow(window);

        JComponent tabContent = createNewTabSheet(window, caption, description, breadCrumbs);

        tabs.put(tabContent, breadCrumbs);

        return tabContent;
    }

    protected JComponent createNewTabSheet(Window window, String caption, String description, WindowBreadCrumbs breadCrumbs) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(breadCrumbs, BorderLayout.NORTH);

        JComponent composition = DesktopComponentsHelper.getComposition(window);
        panel.add(composition, BorderLayout.CENTER);

        JTabbedPane tabsPane = App.getInstance().getTabsPane();
        tabsPane.add(formatTabCaption(caption, description), panel);

        return panel;
    }

    private String formatTabCaption(String caption, String description) {
        String s = formatTabDescription(caption, description);
        int maxLength = ConfigProvider.getConfig(DesktopConfig.class).getMainTabCaptionLength();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        } else {
            return s;
        }
    }

    protected String formatTabDescription(final String caption, final String description) {
        if (!StringUtils.isEmpty(description)) {
            return String.format("%s | %s", caption, description);
        } else {
            return caption;
        }
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs() {
        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs();
        stacks.put(windowBreadCrumbs, new Stack<Map.Entry<Window, Integer>>());
        return windowBreadCrumbs;
    }

    @Override
    protected void showFrame(Component parent, IFrame frame) {
    }

    @Override
    protected void initCompanion(Element companionsElem, AbstractWindow res) {
    }

    @Override
    public void showNotification(String caption, IFrame.NotificationType type) {
    }

    @Override
    public void showNotification(String caption, String description, IFrame.NotificationType type) {
    }

    @Override
    public void showMessageDialog(String title, String message, IFrame.MessageType messageType) {
    }

    @Override
    public void showOptionDialog(String title, String message, IFrame.MessageType messageType, Action[] actions) {
    }

    protected JComponent findTab(Window window) {
        Set<Map.Entry<JComponent, WindowBreadCrumbs>> set = tabs.entrySet();
        for (Map.Entry<JComponent, WindowBreadCrumbs> entry : set) {
            if (entry.getValue().getCurrentWindow().equals(window))
                return entry.getKey();
        }
        return null;
    }

    protected static class WindowOpenMode {

        protected Window window;
        protected OpenType openType;
        protected Object data;

        public WindowOpenMode(Window window, OpenType openType) {
            this.window = window;
            this.openType = openType;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Window getWindow() {
            return window;
        }

        public OpenType getOpenType() {
            return openType;
        }
    }

}
