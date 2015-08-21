/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class FoldersPaneLoader extends ComponentLoader {
    public FoldersPaneLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        FoldersPane component = (FoldersPane) factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(final FoldersPane component, Element element, Component parent) {
        loadId(component, element);

        loadStyleName(component, element);
        loadAlign(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        loadEnable(component, element);
        loadVisible(component, element);

        assignFrame(component);

        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, Frame window) {
                if (component.isVisible() && component.isEnabled()) {
                    component.loadFolders();
                }
            }
        });
    }
}