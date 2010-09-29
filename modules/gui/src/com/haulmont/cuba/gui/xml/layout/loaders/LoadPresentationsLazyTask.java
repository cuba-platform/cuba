/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 24.09.2010 15:36:35
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

public class LoadPresentationsLazyTask implements com.haulmont.cuba.gui.xml.layout.ComponentLoader.LazyTask {

    private Component.HasPresentations component;

    public LoadPresentationsLazyTask(Component.HasPresentations c) {
        component = c;
    }

    public void execute(ComponentLoader.Context context, IFrame frame) {
        if (component.isUsePresentations()) {
            component.loadPresentations();
        }
    }
}
