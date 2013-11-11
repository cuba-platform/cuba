/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
@SuppressWarnings("serial")
public class WebSplitPanel extends com.vaadin.ui.SplitPanel
        implements SplitPanel, Component.HasSettings {

    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new LinkedHashSet<>();

    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean showHookButton = false;
    private String defaultPosition = null;

    private PositionUpdateListener positionListener;

    private IFrame frame;

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        int previousPosition = this.getSplitPosition();
        super.changeVariables(source, variables);
        int newPosition = this.getSplitPosition();
        if ((newPosition != previousPosition) && (positionListener != null))
            positionListener.updatePosition(previousPosition, newPosition);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute("useHookButton", showHookButton);
        if (defaultPosition != null)
            target.addAttribute("defaultPosition", defaultPosition);
    }

    @Override
    public void add(Component component) {
        final com.vaadin.ui.Component itmillComponent = WebComponentsHelper.getComposition(component);

        addComponent(itmillComponent);

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            if (frame != null) {
                frame.registerComponent(component);
            }
        }
        ownComponents.add(component);
    }

    @Override
    public void remove(Component component) {
        removeComponent(WebComponentsHelper.getComposition(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return WebComponentsHelper.<T>getComponent(this, id);
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
        final com.vaadin.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    @Override
    public void applySettings(Element element) {
        Element e = element.element("position");
        if (e != null) {
            String value = e.attributeValue("value");
            String unit = e.attributeValue("unit");
            if (!StringUtils.isBlank(value) && !StringUtils.isBlank(unit)) {
                try {
                    Integer posValue = Integer.valueOf(value);
                    Integer unitValue = Integer.valueOf(unit);

                    setSplitPosition(posValue, unitValue);
                } catch (NumberFormatException ex) {
                    LogFactory.getLog(WebSplitPanel.class).warn(
                            String.format("Unable to applySettings for position %s and unit %s", value, unit));
                }
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        Element e = element.element("position");
        if (e == null)
            e = element.addElement("position");
        e.addAttribute("value", String.valueOf(getSplitPosition()));
        e.addAttribute("unit", String.valueOf(getSplitPositionUnit()));
        return true;
    }

    @Override
    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    @Override
    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
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
    public void setPositionUpdateListener(PositionUpdateListener positionListener) {
        this.positionListener = positionListener;
    }

    @Override
    public PositionUpdateListener getPositionUpdateListener() {
        return positionListener;
    }
}