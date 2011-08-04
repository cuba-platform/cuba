/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.ButtonTabComponent;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Tabsheet;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTabsheet
    extends DesktopAbstractComponent<JTabbedPane>
    implements Tabsheet, Component.Container
{
    protected Map<Component, String> components = new HashMap<Component, String>();

    protected List<TabImpl> tabs = new ArrayList<TabImpl>();

    protected Set<LazyTabInfo> lazyTabs = new HashSet<LazyTabInfo>();

    private ComponentLoader.Context context;

    private boolean initLazyTabListenerAdded;
    private boolean componentTabChangeListenerInitialized;

    protected Set<TabChangeListener> listeners = new HashSet<TabChangeListener>();

    public DesktopTabsheet() {
        impl = new JTabbedPane();
    }

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        for (TabImpl tab : tabs) {
            if (tab.getComponent() instanceof Container) {
                Component component = DesktopComponentsHelper.getComponent((Container) tab.getComponent(), id);
                if (component != null)
                    return (T) component;
            }
        }
        return null;
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return DesktopComponentsHelper.<T>getComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return components.keySet();
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public Tab addTab(String name, Component component) {
        TabImpl tab = new TabImpl(name, component);

        tabs.add(tab);
        components.put(component, name);

        JComponent comp = DesktopComponentsHelper.getComposition(component);

        impl.addTab("", comp);

        return tab;
    }

    @Override
    public Tab addLazyTab(String name, Element descriptor, ComponentLoader loader) {
        DesktopVBox tabContent = new DesktopVBox();

        TabImpl tab = new TabImpl(name, tabContent);

        tabs.add(tab);
        components.put(tabContent, name);

        final JComponent comp = DesktopComponentsHelper.getComposition(tabContent);

        impl.addTab("", comp);
        lazyTabs.add(new LazyTabInfo(tabContent, descriptor, loader));

        if (!initLazyTabListenerAdded) {
            impl.addChangeListener(
                    new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            initLazyTab();
                        }
                    }
            );
            initLazyTabListenerAdded = true;
        }

        context = loader.getContext();

        return tab;
    }

    @Override
    public void removeTab(String name) {
        TabImpl tab = getTabImpl(name);
        components.remove(tab.getComponent());
        impl.remove(DesktopComponentsHelper.getComposition(tab.getComponent()));
    }

    private TabImpl getTabImpl(String name) {
        TabImpl tab = null;
        for (TabImpl t : tabs) {
            if (t.getName().equals(name)) {
                tab = t;
                break;
            }
        }
        if (tab == null)
            throw new IllegalStateException(String.format("Can't find tab '%s'", name));
        return tab;
    }

    @Override
    public Tab getTab() {
        JComponent component = (JComponent) impl.getSelectedComponent();
        if (component == null) {
            return null; // nothing selected
        }
        for (TabImpl tabImpl : tabs) {
            if (DesktopComponentsHelper.getComposition(tabImpl.getComponent()).equals(component))
                return tabImpl;
        }
        return tabs.get(0);
    }

    @Override
    public void setTab(Tab tab) {
        Component component = ((TabImpl) tab).getComponent();
        impl.setSelectedComponent(DesktopComponentsHelper.getComposition(component));
    }

    @Override
    public void setTab(String name) {
        TabImpl tab = getTabImpl(name);
        impl.setSelectedComponent(DesktopComponentsHelper.getComposition(tab.getComponent()));
    }

    @Override
    public Tab getTab(String name) {
        return getTabImpl(name);
    }

    @Override
    public Collection<Tab> getTabs() {
        return Collections.<Tab>unmodifiableCollection(tabs);
    }

    @Override
    public void addListener(TabChangeListener listener) {
        // init component SelectedTabChangeListener only when needed, making sure it is
        // after all lazy tabs listeners
        if (!componentTabChangeListenerInitialized) {
            impl.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    // Init lazy tab if needed
                    initLazyTab();
                    // Fire GUI listener
                    fireTabChanged();
                    // Execute outstanding post init tasks after GUI listener.
                    // We suppose that context.executePostInitTasks() executes a task once and then remove it from task list.
                    if (context != null)
                        context.executePostInitTasks();
                }
            });
            componentTabChangeListenerInitialized = true;
        }

        listeners.add(listener);
    }

    private void initLazyTab() {
        JComponent selectedTab = (JComponent) DesktopTabsheet.this.impl.getSelectedComponent();
        LazyTabInfo lti = null;
        for (LazyTabInfo lazyTabInfo : lazyTabs) {
            if (lazyTabInfo.getTabComponent() == selectedTab) {
                lti = lazyTabInfo;
                break;
            }
        }
        if (lti == null) // already initialized
            return;

        lazyTabs.remove(lti);

        Component comp;
        try {
            comp = lti.loader.loadComponent(AppConfig.getFactory(), lti.descriptor, null);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        lti.tabContent.add(comp);
        lti.tabContent.expand(comp, "", "");

        final Window window = ComponentsHelper.getWindow(DesktopTabsheet.this);
        if (window != null) {
            ComponentsHelper.walkComponents(
                    lti.tabContent,
                    new ComponentVisitor() {
                        public void visit(Component component, String name) {
                            if (component instanceof HasSettings) {
                                Settings settings = window.getSettings();
                                if (settings != null) {
                                    Element e = settings.get(name);
                                    ((HasSettings) component).applySettings(e);
                                }
                            }
                        }
                    }
            );

            ((DsContextImplementation) window.getDsContext()).resumeSuspended();
        }
    }

    @Override
    public void removeListener(TabChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireTabChanged() {
        for (TabChangeListener listener : listeners) {
            listener.tabChanged(getTab());
        }
    }

    private void updateTabVisibility(TabImpl tab) {
        // find insert/remove index by visibility of existing tabs
        int idx = 0;
        for (TabImpl t : tabs) {
            if (t.equals(tab))
                break;
            if (t.isVisible())
                idx++;
        }

        if (tab.isVisible()) {
            JComponent comp = DesktopComponentsHelper.getComposition(tab.getComponent());
            impl.insertTab(tab.getCaption(), null, comp, null, idx);
        } else {
            impl.removeTabAt(idx);
        }
    }

    protected class TabImpl implements Tabsheet.Tab {

        private String name;
        private Component component;
        private String caption;
        private boolean enabled = true;
        private boolean visible = true;
        private boolean closable;

        private TabCloseHandler closeHandler;

        public TabImpl(String name, Component component) {
            this.name = name;
            this.component = component;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public void setCaption(String caption) {
            this.caption = caption;
            DesktopTabsheet.this.impl.setTitleAt(getTabIndex(), caption);
            setTabComponentCaption(caption);
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            DesktopTabsheet.this.impl.setEnabledAt(getTabIndex(), enabled);
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean visible) {
            if (visible != this.visible) {
                this.visible = visible;
                DesktopTabsheet.this.updateTabVisibility(this);
            }
        }

        @Override
        public boolean isClosable() {
            return closable;
        }

        @Override
        public void setClosable(boolean closable) {
            if (closable != this.closable) {
                if (closable) {
                    addCloseComponent();
                }
                else {
                    removeCloseComponent();
                }
                this.closable = closable;
            }
        }

        private void removeCloseComponent() {
            impl.setTabComponentAt(getTabIndex(), null);
        }

        private void addCloseComponent() {
            ButtonTabComponent tabComponent = new ButtonTabComponent(
                impl,
                new ButtonTabComponent.CloseListener() {
                    public void onTabClose(int tabIndex) {
                        if (closeHandler != null) {
                            closeHandler.onTabClose(TabImpl.this);
                        }
                        else {
                            removeTab(getName());
                        }
                    }
                }
          );
          impl.setTabComponentAt(getTabIndex(), tabComponent);
        }

        private void setTabComponentCaption(String caption) {
            java.awt.Component component = impl.getTabComponentAt(getTabIndex());
            if (component instanceof ButtonTabComponent)
                ((ButtonTabComponent) component).setCaption(caption);
        }

        public TabCloseHandler getCloseHandler() {
            return closeHandler;
        }

        @Override
        public void setCloseHandler(TabCloseHandler tabCloseHandler) {
            this.closeHandler = tabCloseHandler;
        }

        public Component getComponent() {
            return component;
        }

        private int getTabIndex() {
            JComponent jComponent = DesktopComponentsHelper.getComposition(component);
            return DesktopTabsheet.this.impl.indexOfComponent(jComponent);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class LazyTabInfo {
        private DesktopAbstractBox tabContent;
        private Element descriptor;
        private ComponentLoader loader;

        private LazyTabInfo(DesktopAbstractBox tabContent, Element descriptor, ComponentLoader loader) {
            this.descriptor = descriptor;
            this.loader = loader;
            this.tabContent = tabContent;
        }

        private JComponent getTabComponent() {
            return DesktopComponentsHelper.getComposition(tabContent);
        }
    }
}
