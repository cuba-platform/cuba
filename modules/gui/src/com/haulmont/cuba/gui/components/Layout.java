/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 13:39:01
 * $Id$
 */
package com.haulmont.cuba.gui.components;

/**
 * Component container which can expand enclosing components
 */
public interface Layout extends Component.Container {

    void expand(Component component, String height, String width);

    interface Spacing{
        void setSpacing(boolean enabled);
    }

    interface Margin {
        void setMargin(boolean enable);
        void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable);
    }
}
