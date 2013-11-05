/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import com.vaadin.ui.CustomLayout;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebHtmlBoxLayout extends WebAbstractComponent<CustomLayout> implements HtmlBoxLayout {

    protected Collection<Component> ownComponents = new HashSet<>();
    protected Map<String, Component> componentByIds = new HashMap<>();

    public WebHtmlBoxLayout() {
        component = new CustomLayout("");
    }

    @Override
    public String getTemplateName() {
        return component.getTemplateName();
    }

    @Override
    public void setTemplateName(String templateName) {
        component.setTemplateName(templateName);
    }

    @Override
    public void add(Component childComponent) {
        final com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);

        if (childComponent.getId() != null) {
            component.addComponent(vComponent, childComponent.getId());
            componentByIds.put(childComponent.getId(), childComponent);
            if (frame != null) {
                frame.registerComponent(childComponent);
            }
        } else {
            component.addComponent(vComponent);
        }

        ownComponents.add(childComponent);
    }

    @Override
    public void remove(Component childComponent) {
        if (childComponent.getId() != null) {
            component.removeComponent(childComponent.getId());
            componentByIds.remove(childComponent.getId());
        } else {
            component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        }
        ownComponents.remove(childComponent);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return WebComponentsHelper.getComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }
}