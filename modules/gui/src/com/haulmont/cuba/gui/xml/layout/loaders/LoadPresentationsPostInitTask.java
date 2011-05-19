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

public class LoadPresentationsPostInitTask implements ComponentLoader.PostInitTask {

    private Component.HasPresentations component;

    public LoadPresentationsPostInitTask(Component.HasPresentations c) {
        component = c;
    }

    public void execute(ComponentLoader.Context context, IFrame window) {
        if (component.isUsePresentations()) {
            component.loadPresentations();
        }
    }
}
