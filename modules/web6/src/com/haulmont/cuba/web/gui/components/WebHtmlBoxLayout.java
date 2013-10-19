/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import com.haulmont.cuba.gui.components.IFrame;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Layout;

import java.util.*;

public class WebHtmlBoxLayout extends CustomLayout implements HtmlBoxLayout {

    protected String id;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    private IFrame frame;

    public WebHtmlBoxLayout() {
        super("");
    }

    public void expand(Component component, String height, String width) {
        //do nothing
    }

    public void expand(Component component) {
        expand(component, "", "");
    }

    public void add(Component component) {
        final com.vaadin.ui.Component itmillComponent = WebComponentsHelper.getComposition(component);

        if (component.getId() != null) {
            addComponent(itmillComponent, component.getId());
            componentByIds.put(component.getId(), component);
            if (frame != null) {
                frame.registerComponent(component);
            }
        } else {
            addComponent(itmillComponent);
        }

        ownComponents.add(component);
    }

    public void remove(Component component) {
        if (component.getId() != null) {
            removeComponent(component.getId());
            componentByIds.remove(component.getId());
        } else {
            removeComponent(WebComponentsHelper.getComposition(component));
        }
        ownComponents.remove(component);
    }

    @SuppressWarnings("unchecked")
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
    }

    public void requestFocus() {
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = this.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this,
                    WebComponentsHelper.convertAlignment(alignment));
        }
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }
}
