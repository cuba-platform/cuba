/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:12:41
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface Component {

    interface AlignInfo {
        public static final int ALIGNMENT_LEFT = 1;
        public static final int ALIGNMENT_RIGHT = 2;
        public static final int ALIGNMENT_TOP = 4;
        public static final int ALIGNMENT_BOTTOM = 8;
        public static final int ALIGNMENT_HORIZONTAL_CENTER = 16;
        public static final int ALIGNMENT_VERTICAL_CENTER = 32;
    }
    
    int getVerticalAlIlignment();
    void setVerticalAlIlignment(int verticalAlIlignment);

    int getHorizontalAlIlignment();
    void setHorizontalAlIlignment(int horizontalAlIlignment);

    interface Container extends Component {
        void add(Component component);
        void remove(Component component);
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

    interface Sizable extends Component {
        boolean isFlexible();
        void setFlexible(boolean flexible);
    }
}
