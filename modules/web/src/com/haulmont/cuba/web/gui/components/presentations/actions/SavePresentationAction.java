/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.presentations.actions;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.Presentation;
import org.dom4j.Element;

/**
 * @author shishov
 * @version $Id$
 */
public class SavePresentationAction extends AbstractPresentationAction {

    public SavePresentationAction(Table table) {
        super(table, "PresentationsPopup.save");
    }

    @Override
    public void actionPerform(Component component) {
        tableImpl.hidePresentationsPopup();

        Presentations presentations = table.getPresentations();
        Presentation current = presentations.getCurrent();
        Element e = presentations.getSettings(current);
        table.saveSettings(e);
        presentations.setSettings(current, e);
        presentations.commit();
    }
}
