/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:12:41
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import org.dom4j.Element;

import java.util.Collection;

import com.haulmont.cuba.gui.data.ValueListener;

public interface Component {
    enum Alignment {
        TOP_RIGHT,
        TOP_LEFT,
        TOP_CENTER,
        MIDDLE_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER
    }

    public static final int UNITS_PIXELS = 0;
    public static final int UNITS_PERCENTAGE = 8;

    String getId();
    void setId(String id);

    boolean isVisible();
    void setVisible(boolean visible);

    void requestFocus();

    float getHeight();
    int getHeightUnits();
    void setHeight(String height);

    float getWidth();
    int getWidthUnits();
    void setWidth(String width);

    Alignment getAlignment();
    void setAlignment(Alignment alignment);

    String getStyleName();
    void setStyleName(String name);

    interface Container extends Component {
        void add(Component component);
        void remove(Component component);

        <T extends Component> T getOwnComponent(String id);
        <T extends Component> T getComponent(String id);

        Collection<Component> getOwnComponents();
        Collection<Component> getComponents();
    }

    interface Wrapper {
        <T> T getComponent();
    }

    interface BelongToFrame extends Component {
        <A extends IFrame> A getFrame();
        void setFrame(IFrame frame);
    }

    interface HasCaption {
        String getCaption();
        void setCaption(String caption);
    }

    interface Field extends Editable, BelongToFrame {
        <T> T getValue();
        void setValue(Object value);

        void addListener(ValueListener listener);
        void removeListener(ValueListener listener);
    }

    interface HasXmlDescriptor {
        Element getXmlDescriptor();
        void setXmlDescriptor(Element element);
    }

    interface Actions extends Component {
        void addAction(Action action);
        void removeAction(Action action);

        Collection<Action> getActions();

        Action getAction(String id);
    }

    interface Editable extends Component {
        boolean isEditable();
        void setEditable(boolean editable);
    }
}
