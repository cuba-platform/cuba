/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.desktop.gui.components.DesktopComponent;
import com.haulmont.cuba.desktop.gui.components.DesktopContainer;
import com.haulmont.cuba.gui.components.Component;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopContainerHelper {

    public static void assignContainer(Component component, DesktopContainer container) {
        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setContainer(container);
        }
        else if (component instanceof Component.Wrapper) { // for frame wrappers
            Object wrapped = ((Component.Wrapper) component).getComposition();
            if (wrapped instanceof DesktopComponent) {
                ((DesktopComponent) wrapped).setContainer(container);
            }
        }
    }
}
