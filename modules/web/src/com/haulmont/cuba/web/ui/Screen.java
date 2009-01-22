/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 19:02:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.itmill.toolkit.ui.ExpandLayout;

import java.util.HashMap;
import java.util.Map;

public class Screen extends ExpandLayout implements Window
{
    protected ScreenContext screenContext;
    private String id;

    private Map<String, Component> componentByIds = new HashMap<String, Component>();

    public Screen() {
        super(ExpandLayout.ORIENTATION_VERTICAL);
        setMargin(true);
        setSpacing(true);
    }

    public void add(Component component) {
        addComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
    }

    public void remove(Component component) {
        removeComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
    }

    public void init(ScreenContext context) {
        screenContext = context;
    }

    public boolean onClose() {
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void requestFocus() {
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

    public int getVerticalAlIlignment() {
        return ALIGNMENT_VERTICAL_CENTER;
    }

    public void setVerticalAlIlignment(int verticalAlIlignment) {}

    public int getHorizontalAlIlignment() {
        return ALIGNMENT_HORIZONTAL_CENTER;
    }

    public void setHorizontalAlIlignment(int horizontalAlIlignment) {}

    public void expand(Component component, String height, String width) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
