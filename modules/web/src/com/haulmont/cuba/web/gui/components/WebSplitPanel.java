/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalSplitPanel;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebSplitPanel extends WebAbstractComponent<AbstractSplitPanel>
        implements SplitPanel, Component.HasSettings, Component.Wrapper {

    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new HashSet<>();

    protected Alignment alignment = Alignment.TOP_LEFT;

    protected boolean showHookButton = false;
    protected String defaultPosition = null;

    protected SplitPanel.PositionUpdateListener positionListener;

    protected int orientation;

    public WebSplitPanel() {
    }

//    vaadin7
//    @Override
//    public void changeVariables(Object source, Map<String, Object> variables) {
//        int previousPosition = this.getSplitPosition();
//        super.changeVariables(source, variables);
//        int newPosition = this.getSplitPosition();
//        if ((newPosition != previousPosition) && (positionListener != null))
//            positionListener.updatePosition(previousPosition, newPosition);
//    }

//    vaadin7
//    @Override
//    public void paintContent(PaintTarget target) throws PaintException {
//        super.paintContent(target);
//
//        target.addAttribute("useHookButton", showHookButton);
//        if (defaultPosition != null)
//            target.addAttribute("defaultPosition", defaultPosition);
//    }

    @Override
    public void add(Component childComponent) {
//        vaadin7
        if (component == null) {
            if (orientation == SplitPanel.ORIENTATION_HORIZONTAL)
                component = new HorizontalSplitPanel();
            else
                component = new VerticalSplitPanel();
        }

        final com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(vComponent);

        if (childComponent.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
            if (frame != null) {
                frame.registerComponent(childComponent);
            }
        }
        ownComponents.add(childComponent);
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component parentComponent = component.getParent();
        if (parentComponent instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) parentComponent).setComponentAlignment(component, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    @Override
    public void applySettings(Element element) {
        Element e = element.element("position");
        if (e != null) {
            String value = e.attributeValue("value");
            String unit = e.attributeValue("unit");
            if (!StringUtils.isBlank(value) && !StringUtils.isBlank(unit))  {
                component.setSplitPosition(Float.valueOf(value), Sizeable.Unit.getUnitFromSymbol(unit));
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        Element e = element.element("position");
        if (e == null)
            e = element.addElement("position");
        e.addAttribute("value", String.valueOf(component.getSplitPosition()));
        e.addAttribute("unit", String.valueOf(component.getSplitPositionUnit()));
        return true;
    }

    public boolean isShowHookButton() {
        return showHookButton;
    }

    public void setShowHookButton(boolean showHookButton) {
        this.showHookButton = showHookButton;
    }

    public String getDefaultPosition() {
        return defaultPosition;
    }

    public void setDefaultPosition(String defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    @Override
    public int getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public void setSplitPosition(int pos) {
        component.setSplitPosition(pos);
    }

    @Override
    public void setLocked(boolean locked) {
        component.setLocked(locked);
    }

    @Override
    public boolean isLocked() {
        return component.isLocked();
    }

    @Override
    public void setPositionUpdateListener(PositionUpdateListener positionListener) {
        this.positionListener = positionListener;
    }

    @Override
    public PositionUpdateListener getPositionUpdateListener() {
        return positionListener;
    }
}