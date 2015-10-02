/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.FileMultiUploadField;

/**
 * @author abramov
 * @version $Id$
 */
public class FileMultiUploadFieldLoader extends AbstractComponentLoader<FileMultiUploadField> {
    @Override
    public void createComponent() {
        resultComponent = (FileMultiUploadField) factory.createComponent(FileMultiUploadField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

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