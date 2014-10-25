/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class SourceCodeEditorLoader extends AbstractFieldLoader {
    public SourceCodeEditorLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Element element, Field field, Component parent) {
        super.initComponent(element, field, parent);

        loadMode((SourceCodeEditor) field, element);
    }

    protected void loadMode(SourceCodeEditor component, Element element) {
        String mode = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(mode)) {
            component.setMode(SourceCodeEditor.Mode.parse(mode));
        }
    }
}