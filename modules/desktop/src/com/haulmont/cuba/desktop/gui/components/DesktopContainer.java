/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public interface DesktopContainer extends Component.Container {

    /*
     * Updates child component after size or alignment change.
     * Called by child component.
     * General implementation is to call layout to update / reset component's constraints.
     */
    void updateComponent(Component child);
}
