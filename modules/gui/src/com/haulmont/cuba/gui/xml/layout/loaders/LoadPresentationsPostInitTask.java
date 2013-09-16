/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

/**
 * @author gorodnov
 * @version $Id$
 */
public class LoadPresentationsPostInitTask implements ComponentLoader.PostInitTask {

    private Component.HasPresentations component;

    public LoadPresentationsPostInitTask(Component.HasPresentations c) {
        component = c;
    }

    @Override
    public void execute(ComponentLoader.Context context, IFrame window) {
        if (component.isUsePresentations()) {
            component.loadPresentations();
        }
    }
}