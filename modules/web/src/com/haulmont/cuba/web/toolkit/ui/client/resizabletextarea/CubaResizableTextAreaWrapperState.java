/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.vaadin.shared.AbstractFieldState;

/**
 * @author gorelov
 * @version $Id$
 */
public class CubaResizableTextAreaWrapperState extends AbstractFieldState {
    {
        primaryStyleName = "cuba-resizabletextarea-wrapper";
    }

    public boolean resizable = false;
}
