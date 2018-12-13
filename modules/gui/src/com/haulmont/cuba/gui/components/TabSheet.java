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
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * TabSheet component interface.
 */
public interface TabSheet extends ComponentContainer, Component.BelongToFrame, Component.HasIcon, Component.HasCaption,
                                  Component.Focusable, HasContextHelp, HasHtmlCaption, HasHtmlDescription {

    String NAME = "tabSheet";

    /**
     * Add a new tab to the component.
     * @param name      id of the new tab
     * @param component a component that will be the content of the new tab
     * @return  the new tab
     */
    Tab addTab(String name, Component component);

    /**
     * INTERNAL. Add a new lazy tab to the component.
     */
    Tab addLazyTab(String name, Element descriptor, ComponentLoader loader);

    /**
     * Remove tab.
     * @param name id of the tab to remove
     */
    void removeTab(String name);

    /**
     * Remove all tabs.
     */
    void removeAllTabs();

    /**
     * Get selected tab. May be null if the tabsheet does not contain tabs at all.
     */
    Tab getSelectedTab();
    /**
     * Set selected tab.
     * @param tab tab instance
     */
    void setSelectedTab(Tab tab);
    /**
     * Set selected tab.
     * @param name tab id
     */
    void setSelectedTab(String name);

    /**
     * Get selected tab. May be null if the tabsheet does not contain tabs at all.
     * @deprecated use {@link #getSelectedTab()}
     */
    @Deprecated
    default Tab getTab() {
        return getSelectedTab();
    }
    /**
     * Set selected tab.
     * @param tab tab instance
     * @deprecated Use {@link #setSelectedTab(Tab)}
     */
    @Deprecated
    default void setTab(Tab tab) {
        setSelectedTab(tab);
    }
    /**
     * Set selected tab.
     * @param name tab id
     * @deprecated Use {@link #setSelectedTab(String)}
     */
    @Deprecated
    default void setTab(String name) {
        setSelectedTab(name);
    }

    /**
     * Get tab with the provided id.
     * @param name tab id
     * @return tab instance
     */
    Tab getTab(String name);

    /**
     * Get a component that is a content of the tab.
     * @param name tab id
     * @return  tab content
     */
    Component getTabComponent(String name);

    /**
     * Get all tabs.
     */
    Collection<Tab> getTabs();

    /**
     * @return true if the tab captions are rendered as HTML, false if rendered as plain text
     */
    boolean isTabCaptionsAsHtml();
    /**
     * Sets whether HTML is allowed in the tab captions.
     *
     * @param tabCaptionsAsHtml true if the tab captions are rendered as HTML, false if rendered as plain text
     */
    void setTabCaptionsAsHtml(boolean tabCaptionsAsHtml);

    /**
     * @return true if the tabs are shown in the UI, false otherwise
     */
    boolean isTabsVisible();
    /**
     * Sets whether the tab selection part should be shown in the UI.
     *
     * @param tabsVisible true if the tabs should be shown in the UI, false otherwise
     */
    void setTabsVisible(boolean tabsVisible);

    /**
     * Tab interface.
     */
    interface Tab extends Component.HasIcon, Component.HasCaption {
        /**
         * Get tab id.
         */
        String getName();

        /**
         * INTERNAL. Set tab id.
         */
        void setName(String name);

        /**
         * Whether the tab is enabled.
         */
        boolean isEnabled();
        void setEnabled(boolean enabled);

        /**
         * Whether the tab is visible.
         */
        boolean isVisible();
        void setVisible(boolean visible);

        /**
         * Whether the tab has a button that allows a user to close the tab.
         * False by default.
         */
        boolean isClosable();
        void setClosable(boolean closable);

        /**
         * Whether the tab can be detached to a new window in desktop client.
         * False by default.
         */
        boolean isDetachable();
        void setDetachable(boolean detachable);

        /**
         * Set a handler that can override the close behavior if {@link #isClosable()} is true.
         * Default action just removes the tab.
         */
        void setCloseHandler(TabCloseHandler tabCloseHandler);
        TabCloseHandler getCloseHandler();

        /**
         * Set style for UI element that represents the tab header.
         * @param styleName style
         */
        void setStyleName(String styleName);
        String getStyleName();
    }

    /**
     * Handler that overrides the default behavior if {@link Tab#isClosable()} is true and a user clicks the close button.
     */
    interface TabCloseHandler {
        void onTabClose(Tab tab);
    }

    /**
     * Add a listener that will be notified when a selected tab is changed.
     */
    Subscription addSelectedTabChangeListener(Consumer<SelectedTabChangeEvent> listener);

    /**
     * Remove previously added SelectedTabChangeListener.
     *
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeSelectedTabChangeListener(Consumer<SelectedTabChangeEvent> listener);

    /**
     * SelectedTabChangeEvents are fired when a selected tab is changed.
     */
    class SelectedTabChangeEvent extends EventObject implements HasUserOriginated {
        private final Tab selectedTab;
        protected final boolean userOriginated;

        public SelectedTabChangeEvent(TabSheet tabSheet, Tab selectedTab) {
            this(tabSheet, selectedTab, false);
        }

        public SelectedTabChangeEvent(Object source, Tab selectedTab, boolean userOriginated) {
            super(source);
            this.selectedTab = selectedTab;
            this.userOriginated = userOriginated;
        }

        @Override
        public TabSheet getSource() {
            return (TabSheet) super.getSource();
        }

        public Tab getSelectedTab() {
            return selectedTab;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }
}