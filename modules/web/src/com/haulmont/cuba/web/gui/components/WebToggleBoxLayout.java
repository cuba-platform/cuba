/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 12.08.2009 13:02:09
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Layout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ToggleBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.TogglePanel;
import com.vaadin.ui.VerticalLayout;
import org.dom4j.Element;
import org.apache.commons.lang.BooleanUtils;

import java.util.*;

public class WebToggleBoxLayout
        extends WebAbstractComponent<TogglePanel>
        implements ToggleBoxLayout
{
    protected com.vaadin.ui.Layout onLayout = new LayoutWrapper();
    protected com.vaadin.ui.Layout offLayout = new LayoutWrapper();

    public WebToggleBoxLayout() {
        component = new ToggleBoxWrapper(this);
        component.setExpandLayout(onLayout);
        component.setCollapseLayout(offLayout);
    }

    public Layout getOnLayout() {
        return (Layout) onLayout;
    }

    public Layout getOffLayout() {
        return (Layout) offLayout;
    }

    public void toggle() {
        component.togglePanel();
    }

    public void setOn(boolean on) {
        component.setExpanded(on);
    }

    public boolean isOn() {
        return component.isExpanded();
    }

    public void applySettings(Element element) {
        Element toggleElem = element.element("toggle");
        if (toggleElem != null) {
            String attr = toggleElem.attributeValue("on");
            if (attr != null) {
                component.setExpanded(BooleanUtils.toBoolean(attr));
            }
        }
    }

    public boolean saveSettings(Element element) {
        Element toggleElem = element.element("toggle");
        if (toggleElem != null)
            element.remove(toggleElem);
        toggleElem = element.addElement("toggle");
        toggleElem.addAttribute("on", String.valueOf(component.isExpanded()));
        return true;
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Component> T getOwnComponent(String id) {
        Component component = ((Layout) offLayout).getOwnComponent(id);
        if (component == null) {
            component = ((Layout) onLayout).getOwnComponent(id);
        }
        return (T) component;
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Component> T getComponent(String id) {
        Component component = ((Layout) offLayout).getComponent(id);
        if (component == null) {
            component = ((Layout) onLayout).getComponent(id);
        }
        return (T) component;
    }

    public Collection<Component> getOwnComponents() {
        Set<Component> ownComponents = new LinkedHashSet<Component>();

        ownComponents.addAll(((Layout) offLayout).getOwnComponents());
        ownComponents.addAll(((Layout) onLayout).getOwnComponents());

        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<Component> getComponents() {
        Set<Component> components = new LinkedHashSet<Component>();

        components.addAll(((Layout) offLayout).getComponents());
        components.addAll(((Layout) onLayout).getComponents());

        return components;
    }

    public class LayoutWrapper extends VerticalLayout implements Layout {

        protected Collection<Component> ownComponents = new HashSet<Component>();
        protected Map<String, Component> componentByIds = new HashMap<String, Component>();

        public void expand(Component component, String height, String width) {
            final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.unwrap(component);
            WebComponentsHelper.expand(this, expandedComponent, height, width);
        }

        public void add(Component component) {
            addComponent(WebComponentsHelper.unwrap(component));
            if (component.getId() != null) {
                componentByIds.put(component.getId(), component);
            }
            ownComponents.add(component);
        }

        public void remove(Component component) {
            removeComponent(WebComponentsHelper.unwrap(component));
            if (component.getId() != null) {
                componentByIds.remove(component.getId());
            }
            ownComponents.remove(component);
        }

        @SuppressWarnings({"unchecked"})
        public <T extends Component> T getOwnComponent(String id) {
            return (T) componentByIds.get(id);
        }

        public <T extends Component> T getComponent(String id) {
            return WebComponentsHelper.<T>getComponent(this, id);
        }

        public Collection<Component> getOwnComponents() {
            return Collections.unmodifiableCollection(ownComponents);
        }

        public Collection<Component> getComponents() {
            return WebComponentsHelper.getComponents(this);
        }

        public String getId() {
            throw new UnsupportedOperationException(
                    "You cannot execute this operation for TogglePanel.LayoutWrapper");
        }

        public void setId(String id) {
            throw new UnsupportedOperationException(
                    "You cannot execute this operation for TogglePanel.LayoutWrapper");
        }

        public void requestFocus() {
            throw new UnsupportedOperationException(
                    "You cannot execute this operation for TogglePanel.LayoutWrapper");
        }

        public Alignment getAlignment() {
            throw new UnsupportedOperationException(
                    "You cannot execute this operation for TogglePanel.LayoutWrapper");
        }

        public void setAlignment(Alignment alignment) {
            throw new UnsupportedOperationException(
                    "You cannot execute this operation for TogglePanel.LayoutWrapper");
        }
    }

    private static class ToggleBoxWrapper
            extends TogglePanel
            implements WebComponentEx
    {
        private Component component;

        private ToggleBoxWrapper(Component component) {
            this.component = component;
        }

        public Component asComponent() {
            return component;
        }
    }
}
