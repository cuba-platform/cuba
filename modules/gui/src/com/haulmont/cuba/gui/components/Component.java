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

public interface Component {

    interface AlignInfo {
        public static final int ALIGNMENT_LEFT = 1;
        public static final int ALIGNMENT_RIGHT = 2;
        public static final int ALIGNMENT_TOP = 4;
        public static final int ALIGNMENT_BOTTOM = 8;
        public static final int ALIGNMENT_HORIZONTAL_CENTER = 16;
        public static final int ALIGNMENT_VERTICAL_CENTER = 32;
    }

    String getId();
    void setId(String id);

    void requestFocus();

    int getHeight();
    int getHeightUnits();
    void setHeight(String height);

    int getWidth();
    int getWidthUnits();
    void setWidth(String width);

    int getVerticalAlignment();
    void setVerticalAlignment(int verticalAlignment);

    int getHorizontalAlignment();
    void setHorizontalAlignment(int horizontalAlignment);

    interface Container extends Component {
        void add(Component component);
        void remove(Component component);

        <T extends Component> T getOwnComponent(String id);
        <T extends Component> T getComponent(String id);
    }

    interface Wrapper {
        <T> T getComponent();
    }

    interface HasCaption {
        String getCaption();
        void setCaption(String caption);
    }

    interface Field extends Component {
        <T> T getValue();
        void setValue(Object value);
    }

    interface HasXmlDescriptor {
        Element getXmlDescriptor();
        void setXmlDescriptor(Element element);
    }

    interface ActionsOwner {
        void addAction(Action action);
        void removeAction(Action action);

        Collection<Action> getActions();

        Action getAction(String id);
    }
}
