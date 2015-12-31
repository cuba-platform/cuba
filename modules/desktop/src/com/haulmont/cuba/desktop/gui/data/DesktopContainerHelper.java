/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.desktop.gui.components.DesktopCheckBox;
import com.haulmont.cuba.desktop.gui.components.DesktopComponent;
import com.haulmont.cuba.desktop.gui.components.DesktopContainer;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import org.apache.commons.lang.StringUtils;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopContainerHelper {

    public static boolean mayHaveExternalCaption(Component component) {
        return (component instanceof Field && !(component instanceof DesktopCheckBox));
    }

    public static boolean hasExternalCaption(Component component) {
        if (component instanceof Field && !(component instanceof DesktopCheckBox)) {
            final String caption = ((Field) component).getCaption();
            if (StringUtils.isNotEmpty(caption)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for the presence of component description
     *
     * @param component component to check
     * @return <b>true</b> if the component has a description, and <b>false</b> otherwise
     */
    public static boolean hasExternalDescription(Component component) {
        if (component instanceof Field && !(component instanceof DesktopCheckBox)) {
            final String description = ((Field) component).getDescription();
            if (StringUtils.isNotEmpty(description)) {
                return true;
            }
        }
        return false;
    }

    public static void assignContainer(Component component, DesktopContainer container) {
        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setContainer(container);
        } else if (component instanceof Component.Wrapper) { // for frame wrappers
            Object wrapped = ((Component.Wrapper) component).getComposition();
            if (wrapped instanceof DesktopComponent) {
                ((DesktopComponent) wrapped).setContainer(container);
            }
        }
    }
}