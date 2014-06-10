/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;
import org.vaadin.hene.popupbutton.PopupButton;

import java.util.*;

/**
 * @author pavlov
 * @version $Id$
 */
public class WebPopupButton
        extends WebAbstractComponent<PopupButton>
        implements com.haulmont.cuba.gui.components.PopupButton {

    protected Component popupComponent;
    protected com.vaadin.ui.Component vPopupComponent;
    protected String icon;

    protected List<Action> actionOrder = new LinkedList<>();

    public WebPopupButton() {
        component = new PopupButton("");
        component.setImmediate(true);

        vPopupComponent = new VerticalLayout();
        vPopupComponent.addStyleName("popupmenu");
        ((VerticalLayout) vPopupComponent).setMargin(false);
        vPopupComponent.setSizeUndefined();
        component.setComponent(vPopupComponent);
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (!StringUtils.isEmpty(icon)) {
            component.setIcon(new VersionedThemeResource(icon));
            component.addStyleName(WebButton.ICON_STYLE);
        } else {
            component.setIcon(null);
            component.removeStyleName(WebButton.ICON_STYLE);
        }
    }

    public void setPopupComponent(Component component) {
        this.popupComponent = component;
        vPopupComponent = WebComponentsHelper.unwrap(popupComponent);
        this.component.setComponent(vPopupComponent);
    }

    public void removePopupComponent() {
        popupComponent = null;
        this.component.removeComponent(vPopupComponent);
        vPopupComponent = null;
    }

    public Component getPopupComponent() {
        return popupComponent;
    }

    @Override
    public boolean isPopupVisible() {
        return component.isPopupVisible();
    }

    @Override
    public void setPopupVisible(boolean popupVisible) {
        component.setPopupVisible(popupVisible);
    }

    @Override
    public void setMenuWidth(String width) {
        if (vPopupComponent != null && width != null) {
            vPopupComponent.setWidth(width);
        }
    }

    @Override
    public boolean isAutoClose() {
        return component.isAutoClose();
    }

    @Override
    public void setAutoClose(boolean autoClose) {
        component.setAutoClose(autoClose);
    }

    @Override
    public void addAction(final Action action) {
        if (action != null && vPopupComponent instanceof com.vaadin.ui.Layout) {
            WebButton button = new WebButton() {
                @Override
                protected void beforeActionPerformed() {
                    super.beforeActionPerformed();

                    WebPopupButton.this.requestFocus();
                }
            };
            button.setAction(action);
            button.setIcon(null); // don't show icons to look the same as Table actions

            com.vaadin.ui.Button vButton = (com.vaadin.ui.Button) button.getComposition();
            vButton.setImmediate(true);
            vButton.setSizeFull();
            vButton.setStyleName(BaseTheme.BUTTON_LINK);

            vPopupComponent.setVisible(false); // do not requestRepaint
            ((com.vaadin.ui.Layout) vPopupComponent).addComponent(vButton);
            component.setAutoClose(true);
            actionOrder.add(action);

            if (getId() != null) {
                button.setId(action.getId());
            }
        }
    }

    @Override
    public void removeAction(Action action) {
        if (vPopupComponent instanceof com.vaadin.ui.Layout && actionOrder.remove(action)) {
            vPopupComponent.setVisible(false); // do not requestRepaint
            ((com.vaadin.ui.Layout) vPopupComponent).removeComponent(WebComponentsHelper.unwrap((Component) action.getOwner()));
        }
    }

    @Override
    public void removeAction(String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    @Override
    public void removeAllActions() {
        for (Action action : new ArrayList<>(actionOrder)) {
            removeAction(action);
        }
    }

    @Override
    public Action getAction(String id) {
        if (vPopupComponent instanceof com.vaadin.ui.Layout && id != null) {
            for (Action action : actionOrder) {
                if (id.equals(action.getId())) {
                    return action;
                }
            }
        }

        return null;
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionOrder);
    }
}