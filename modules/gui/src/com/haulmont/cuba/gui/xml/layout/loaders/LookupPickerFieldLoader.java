/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupPickerField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LookupPickerFieldLoader extends LookupFieldLoader {

    @Override
    public void createComponent() {
        resultComponent = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        LookupPickerField lookupPickerField = (LookupPickerField) resultComponent;

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            Metadata metadata = AppBeans.get(Metadata.NAME);
            lookupPickerField.setMetaClass(metadata.getSession().getClass(metaClass));
        }

        loadActions(lookupPickerField, element);
        if (lookupPickerField.getActions().isEmpty()) {
            lookupPickerField.addLookupAction();
            lookupPickerField.addOpenAction();
        }
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        return loadPickerDeclarativeAction(actionsHolder, element);
    }
}