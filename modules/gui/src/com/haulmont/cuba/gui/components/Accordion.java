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

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import java.util.Collection;

/**
 */
public interface Accordion extends Component.Container, Component.BelongToFrame {
    String NAME = "accordion";

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
     * Get selected tab. May be null if the accordion does not contain tabs at all.
     */
    Tab getTab();

    /**
     * Set selected tab.
     * @param tab tab instance
     */
    void setTab(Tab tab);

    /**
     * Set selected tab.
     * @param name tab id
     */
    void setTab(String name);

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
     * Add a listener that will be notified when a selected tab is changed.
     */
    void addListener(TabChangeListener listener);
    void removeListener(TabChangeListener listener);

    /**
     * Tab interface.
     */
    interface Tab extends Component.HasIcon {
        /**
         * Get tab id.
         */
        String getName();

        /**
         * INTERNAL. Set tab id.
         */
        void setName(String name);

        /**
         * Get tab caption.
         */
        String getCaption();

        /**
         * Set tab caption.
         */
        void setCaption(String caption);

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
         * Set style for UI element that represents the tab header.
         * @param styleName style
         */
        void setStyleName(String styleName);
        String getStyleName();
    }

    /**
     * Listener notified when a selected tab is changed.
     */
    interface TabChangeListener {
        void tabChanged(Tab newTab);
    }
}
