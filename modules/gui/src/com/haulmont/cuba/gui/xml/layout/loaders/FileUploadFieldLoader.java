/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.FileUploadField;

/**
 * @author abramov
 * @version $Id$
 */
public class FileUploadFieldLoader extends AbstractComponentLoader<FileUploadField> {
    @Override
    public void createComponent() {
        resultComponent = (FileUploadField) factory.createComponent(FileUploadField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadId(resultComponent, element);
        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadIcon(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
    }
}