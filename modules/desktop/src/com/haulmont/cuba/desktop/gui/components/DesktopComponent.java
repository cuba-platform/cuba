/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;

/**
 * <p>$Id$</p>
 *
 * Desktop components require to know their container.
 *
 * When its size or alignment changes, component asks container to update him.
 *
 * @author Alexander Budarov
 */
public interface DesktopComponent extends Component {
    void setContainer(DesktopContainer container);

    void setExpanded(boolean expanded);
}
