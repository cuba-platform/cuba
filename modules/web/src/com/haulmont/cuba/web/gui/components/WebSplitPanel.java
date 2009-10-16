/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 20.01.2009 17:09:40
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

public class WebSplitPanel extends com.vaadin.ui.SplitPanel
        implements SplitPanel, Component.HasSettings
{
    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<String, Component>();
    protected Collection<Component> ownComponents = new HashSet<Component>();

    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    public void add(Component component) {
        final com.vaadin.ui.Component itmillComponent = WebComponentsHelper.unwrap(component);

        addComponent(itmillComponent);

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
        return id;
    }

    public void setId(String id) {
        this.id = id;
//        setDebugId(id);
    }

    public void requestFocus() {
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void applySettings(Element element) {
        Element e = element.element("position");
        if (e != null) {
            String value = e.attributeValue("value");
            String unit = e.attributeValue("unit");
            if (!StringUtils.isBlank(value) && !StringUtils.isBlank(unit))
                setSplitPosition(Integer.valueOf(value), Integer.valueOf(unit));
        }
    }

    public boolean saveSettings(Element element) {
        Element e = element.element("position");
        if (e == null)
            e = element.addElement("position");
        e.addAttribute("value", String.valueOf(pos));
        e.addAttribute("unit", String.valueOf(posUnit));
        return true;
    }
}
