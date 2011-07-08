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
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.ScreenHistorySupport;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.AbstractAction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopWindowManager extends WindowManager {

    private JTabbedPane tabsPane;

    private final Map<JComponent, WindowBreadCrumbs> tabs = new HashMap<JComponent, WindowBreadCrumbs>();
    private final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<Window, WindowOpenMode>();
    private final Map<WindowBreadCrumbs,Stack<Map.Entry<Window,Integer>>> stacks = new HashMap<WindowBreadCrumbs,Stack<Map.Entry<Window,Integer>>>();
    private final Map<Window,Integer> windows = new HashMap<Window,Integer>();

    private boolean disableSavingScreenHistory;
    private ScreenHistorySupport screenHistorySupport = new ScreenHistorySupport();

    private Log log = LogFactory.getLog(DesktopWindowManager.class);

    public void setTabsPane(final JTabbedPane tabsPane) {
        this.tabsPane = tabsPane;

        tabsPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke("control W"),
                "closeTab"
        );
        tabsPane.getActionMap().put(
                "closeTab",
                new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        closeTab((JComponent) tabsPane.getSelectedComponent());
                    }
                }
        );
    }

    private void closeTab(JComponent tabContent) {
        if (tabContent == null)
            return;
        WindowBreadCrumbs breadCrumbs = tabs.get(tabContent);
        Runnable closeTask = new TabCloseTask(breadCrumbs);
        closeTask.run();
    }

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

        WindowOpenMode openMode = new WindowOpenMode(window, openType);
        Object windowData;

        switch (openType) {
            case NEW_TAB:
                JComponent tab = findTab(window);
                if (tab != null) {
                    tabsPane.setSelectedComponent(tab);
                    windowData = tab;
                } else {
                    windowData = showWindowNewTab(window, caption, description);
                }
                break;
            case THIS_TAB:
                windowData = showWindowThisTab(window, caption, description);
                break;
            case DIALOG:
                windowData = showWindowDialog(window, caption, description);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        openMode.setData(windowData);

        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            windowOpenMode.put(wrappedWindow, openMode);
        } else {
            windowOpenMode.put(window, openMode);
        }

        afterShowWindow(window);
    }

    private JDialog showWindowDialog(final Window window, String caption, String description) {
        JDialog dialog = new JDialog(App.getInstance().getMainFrame(), caption);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JComponent jComponent = DesktopComponentsHelper.getComposition(window);
        dialog.add(jComponent);
        dialog.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        window.close("close", true);
                    }
                }
        );

        Dimension dim = new Dimension();
        final DialogParams dialogParams = getDialogParams();
        if (dialogParams.getWidth() != null)
            dim.width = dialogParams.getWidth();
        else
            dim.width = 600;

        if (dialogParams.getHeight() != null) {
            dim.height = dialogParams.getHeight();
        }
        dialog.setMinimumSize(dim);
        dialog.setResizable(BooleanUtils.isTrue(dialogParams.getResizable()));
        dialog.pack();
        dialog.setLocationRelativeTo(App.getInstance().getMainFrame());

        dialogParams.reset();

        App.getInstance().disable(null);
        dialog.setVisible(true);

        return dialog;
    }

    private JComponent showWindowThisTab(Window window, String caption, String description) {
        JComponent layout = (JComponent) tabsPane.getSelectedComponent();

        WindowBreadCrumbs breadCrumbs = tabs.get(layout);
        if (breadCrumbs == null)
            throw new IllegalStateException("BreadCrumbs not found");

        Window currentWindow = breadCrumbs.getCurrentWindow();

        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        boolean pushed = false;
        for (Map.Entry<Window, Integer> entry : set) {
            if (entry.getKey().equals(currentWindow)) {
                windows.remove(currentWindow);
                stacks.get(breadCrumbs).push(entry);
                pushed = true;
                break;
            }
        }
        if (!pushed) {
            stacks.get(breadCrumbs).push(new AbstractMap.SimpleEntry<Window, Integer>(currentWindow, null));
        }

        windows.remove(window);
        layout.remove(DesktopComponentsHelper.getComposition(currentWindow));

        JComponent component = DesktopComponentsHelper.getComposition(window);
        layout.add(component);

        breadCrumbs.addWindow(window);

        tabsPane.setTitleAt(tabsPane.getSelectedIndex(), formatTabCaption(caption, description));

        return layout;
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

        tabsPane.add(formatTabCaption(caption, description), panel);
        int idx = tabsPane.getTabCount() - 1;

        ButtonTabComponent tabComponent = new ButtonTabComponent(
                tabsPane,
                new ButtonTabComponent.CloseListener() {
                    public void onTabClose(int tabIndex) {
                        JComponent tabContent = (JComponent) tabsPane.getComponentAt(tabIndex);
                        closeTab(tabContent);
                    }
                }
        );
        tabsPane.setTabComponentAt(idx, tabComponent);
        tabsPane.setSelectedIndex(idx);

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
        // the same as web window manager does
        if (parent instanceof Component.Container) {
            Component.Container container = (Component.Container) parent;
            for (Component c : container.getComponents()) {
                if (c instanceof Component.Disposable) {
                    Component.Disposable disposable =
                            (Component.Disposable) c;
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
                container.remove(c);
            }
            container.add(frame);
            // expand loaded frame inside container to take its full size
            if (container instanceof ExpandingLayout) {
                ((ExpandingLayout) container).expand(frame);
            }
        } else {
            throw new IllegalStateException(
                    "Parent component must be com.haulmont.cuba.gui.components.Component.Container"
            );
        }
    }

    @Override
    public void close(Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        final WindowOpenMode openMode = windowOpenMode.get(window);
        if (openMode == null) {
            log.warn("Problem closing window " + window + " : WindowOpenMode not found");
            return;
        }
        disableSavingScreenHistory = false;
        closeWindow(window, openMode);
        windowOpenMode.remove(window);
        windows.remove(openMode.getWindow());
    }

    protected void closeWindow(Window window, WindowOpenMode openMode) {
        if (!disableSavingScreenHistory) {
            screenHistorySupport.saveScreenHistory(window, openMode.getOpenType());
        }

        switch (openMode.openType) {
            case DIALOG: {
                JDialog dialog = (JDialog) openMode.getData();
                dialog.setVisible(false);
                App.getInstance().enable();
                fireListeners(window, tabs.size() != 0);
                break;
            }
            case NEW_TAB: {
                JComponent layout = (JComponent) openMode.getData();
                layout.remove(DesktopComponentsHelper.getComposition(window));

                tabsPane.remove(layout);

                WindowBreadCrumbs windowBreadCrumbs = tabs.get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                tabs.remove(layout);
                stacks.remove(windowBreadCrumbs);

                fireListeners(window, tabs.size() != 0);
                break;
            }
            case THIS_TAB: {
                JComponent layout = (JComponent) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                if (breadCrumbs == null)
                    throw new IllegalStateException("Unable to close screen: breadCrumbs not found");

                breadCrumbs.removeWindow();
                Window currentWindow = breadCrumbs.getCurrentWindow();
                if (!stacks.get(breadCrumbs).empty()) {
                    Map.Entry<Window, Integer> entry = stacks.get(breadCrumbs).pop();
                    putToWindowMap(entry.getKey(), entry.getValue());
                }
                JComponent component = DesktopComponentsHelper.getComposition(currentWindow);

                layout.remove(DesktopComponentsHelper.getComposition(window));
                layout.add(component);

                tabsPane.setTitleAt(tabsPane.getSelectedIndex(), formatTabCaption(currentWindow.getCaption(), currentWindow.getDescription()));

                fireListeners(window, tabs.size() != 0);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void showNotification(String caption, IFrame.NotificationType type) {
        App.getInstance().showNotificationPopup(caption, type);
    }

    @Override
    public void showNotification(String caption, String description, IFrame.NotificationType type) {
        showNotification("<b>" + caption + "</b><br/>" + description, type);
    }

    @Override
    public void showMessageDialog(String title, String message, IFrame.MessageType messageType) {
        JOptionPane.showMessageDialog(
                App.getInstance().getMainFrame(),
                message,
                title,
                DesktopComponentsHelper.convertMessageType(messageType)
        );
    }

    @Override
    public void showOptionDialog(String title, String message, IFrame.MessageType messageType, Action[] actions) {

        class ActionWrapper {
            Action action;

            ActionWrapper(Action action) {
                this.action = action;
            }

            Action getAction() {
                return action;
            }

            @Override
            public String toString() {
                return action.getCaption();
            }
        }

        Object[] options = new Object[actions.length];
        for (int i = 0; i < actions.length; i++) {
            Action action = actions[i];
            options[i] = new ActionWrapper(action);
        }
        int optionType;
        if (options.length == 1)
            optionType = JOptionPane.DEFAULT_OPTION;
        else if (options.length == 2)
            optionType = JOptionPane.YES_NO_OPTION;
        else if (options.length == 3)
            optionType = JOptionPane.YES_NO_CANCEL_OPTION;
        else
            throw new UnsupportedOperationException("Not more than 3 actions supported");

        final JOptionPane optionPane = new JOptionPane(
                message,
                DesktopComponentsHelper.convertMessageType(messageType),
                optionType,
                null,
                options,
                options[0]
        );
        final JDialog dialog = new JDialog(App.getInstance().getMainFrame(), title, true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        String prop = e.getPropertyName();
                        if (dialog.isVisible()
                                && (e.getSource() == optionPane)
                                && (prop.equals(JOptionPane.VALUE_PROPERTY)))
                        {
                            ActionWrapper actionWrapper = (ActionWrapper) e.getNewValue();
                            actionWrapper.getAction().actionPerform(null);

                            dialog.setVisible(false);
                        }
                    }
                });
        dialog.pack();
        dialog.setLocationRelativeTo(App.getInstance().getMainFrame());
        dialog.setVisible(true);
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

    public class TabCloseTask implements Runnable {
        private final WindowBreadCrumbs breadCrumbs;

        public TabCloseTask(WindowBreadCrumbs breadCrumbs) {
            this.breadCrumbs = breadCrumbs;
        }

        public void run() {
            Window windowToClose = breadCrumbs.getCurrentWindow();
            if (windowToClose != null) {
                windowToClose.closeAndRun("close", new TabCloseTask(breadCrumbs));
            }
        }
    }

}
