/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.vaadin;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Focus nested component only if it is visible. <br/>
 * Fixes #PL-4243
 *
 * @author artamonov
 * @version $Id$
 */
public class FocusHandlerWindow extends Window {

    private static final Log log = LogFactory.getLog(FocusHandlerWindow.class);

    public FocusHandlerWindow() {
    }

    public FocusHandlerWindow(String caption) {
        super(caption);
    }

    @Override
    protected void paintFocusComponent(PaintTarget target) throws PaintException {
        if (pendingFocus != null) {
            if (pendingFocus.getWindow() == this
                    || (pendingFocus.getWindow() != null && pendingFocus
                    .getWindow().getParent() == this)) {

                if (isComponentVisibleOnClient(pendingFocus)) {
                    // ensure focused component is still attached to this main window
                    target.addAttribute("focused", pendingFocus);
                } else {
                    log.warn("Unable to focus invisible component " + pendingFocus.getClass());
                }
            }
            pendingFocus = null;
        }
    }

    protected boolean isComponentVisibleOnClient(Component component) {
        Component parent = component.getParent();
        if (parent == null) {
            return true;
        }

        if (component instanceof AbstractComponent) {
            AbstractComponent vComponent = (AbstractComponent) component;
            if (!vComponent.isComponentVisible()) {
                return false;
            }

            if (parent instanceof TabSheet) {
                TabSheet tabSheet = (TabSheet) parent;
                if (component != tabSheet.getSelectedTab()) {
                    return false;
                }
            }

            return isComponentVisibleOnClient(parent);
        }

        return component.isVisible();
    }
}