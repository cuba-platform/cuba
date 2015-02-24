/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import com.haulmont.cuba.gui.components.IFrame;
import com.vaadin.ui.CustomLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebHtmlBoxLayout extends WebAbstractComponent<CustomLayout> implements HtmlBoxLayout {

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
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
        } else {
            component.addComponent(vComponent);
        }

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
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

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : ownComponents) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
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
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
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