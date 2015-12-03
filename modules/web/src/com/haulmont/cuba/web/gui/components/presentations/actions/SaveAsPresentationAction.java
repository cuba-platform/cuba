/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.presentations.actions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.security.entity.Presentation;

/**
 * @author shishov
 * @version $Id$
 */
public class SaveAsPresentationAction extends AbstractEditPresentationAction {

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    public SaveAsPresentationAction(Table table) {
        super(table, "PresentationsPopup.saveAs");
    }

    @Override
    public void actionPerform(Component component) {
        tableImpl.hidePresentationsPopup();

        Presentation presentation = metadata.create(Presentation.class);
        presentation.setComponentId(ComponentsHelper.getComponentPath(table));

        openEditor(presentation);
    }
}
