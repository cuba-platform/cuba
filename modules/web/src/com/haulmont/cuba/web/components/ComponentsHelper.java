/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:54:57
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.itmill.toolkit.ui.Component;

public class ComponentsHelper {
    public static Component unwrap(com.haulmont.cuba.gui.components.Component component) {
        return (com.itmill.toolkit.ui.Component) (component instanceof com.haulmont.cuba.gui.components.Component.Wrapper ?
                ((com.haulmont.cuba.gui.components.Component.Wrapper) component).getComponent() : component);
    }
}
