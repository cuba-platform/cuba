/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Gennady Pavlov
 * Created: 08.06.2010 13:54:46
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.vaadin.incubator.dashlayout.ui.VerDashLayout;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import org.apache.commons.lang.StringUtils;
import org.vaadin.hene.popupbutton.PopupButton;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WebPopupButton
        extends WebAbstractComponent<PopupButton>
        implements com.haulmont.cuba.gui.components.PopupButton{

    private Component popupComponent;
    private com.vaadin.ui.Component vPopupComponent;
    private String icon;

    private List<Action> actionOrder = new LinkedList<Action>();

    private static final long serialVersionUID = -3825116471579307544L;

    public WebPopupButton() {
        component = new PopupButton("");
        component.setImmediate(true);

        vPopupComponent = new VerDashLayout();
        vPopupComponent.addStyleName("popupmenu");
        ((VerDashLayout) vPopupComponent).setMargin(true);
        vPopupComponent.setSizeUndefined();
        component.setComponent(vPopupComponent);
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public Action getAction() {
        return null;
    }

    public void setAction(Action action) {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        if (!StringUtils.isEmpty(icon)) {
            component.setIcon(new ThemeResource(icon));
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
        vPopupComponent = null;
        popupComponent = null;
        this.component.removeComponent(WebComponentsHelper.unwrap(popupComponent));
    }

    public Component getPopupComponent() {
        return popupComponent;
    }

    public boolean isPopupVisible() {
        return component.isPopupVisible();
    }

    public void setPopupVisible(boolean popupVisible) {
        component.setPopupVisible(popupVisible);
    }

    public void setMenuWidth(String width) {
        if (vPopupComponent != null && width != null) {
            vPopupComponent.setWidth(width);    
        }
    }

    public boolean isAutoClose() {
        return component.isAutoClose();
    }

    public void setAutoClose(boolean autoClose) {
        component.setAutoClose(autoClose);
    }

    public void addAction(final Action action) {
        if (action != null && vPopupComponent instanceof com.vaadin.ui.Layout) {
            WebButton button = new WebButton();
            button.setAction(action);

            com.vaadin.ui.Button vButton = (com.vaadin.ui.Button) button.getComposition();
            vButton.setImmediate(true);            
            vButton.setSizeFull();
            vButton.setStyleName(com.vaadin.ui.Button.STYLE_LINK);

            ((com.vaadin.ui.Layout) vPopupComponent).addComponent(vButton);
            ((com.vaadin.ui.Layout) vPopupComponent).requestRepaintAll();
            component.setAutoClose(true);
            actionOrder.add(action);
        }
    }

    public void removeAction(Action action) {
        if (vPopupComponent instanceof com.vaadin.ui.Layout && actionOrder.remove(action)) {
            ((com.vaadin.ui.Layout) vPopupComponent).removeComponent(WebComponentsHelper.unwrap((Component) action.getOwner()));
        }
    }

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

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionOrder);
    }
}
