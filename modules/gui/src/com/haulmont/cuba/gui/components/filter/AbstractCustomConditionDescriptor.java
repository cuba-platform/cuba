/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

/**
 * @author devyatkin
 * @version $Id$
 */
public abstract class AbstractCustomConditionDescriptor<T extends AbstractParam> extends AbstractConditionDescriptor<T> {
    public AbstractCustomConditionDescriptor(Element element,
                                             String messagesPack,
                                             String filterComponentName,
                                             CollectionDatasource datasource) {
        super(element.attributeValue("name"), filterComponentName, datasource);
        this.element = element;

        this.caption = element.attributeValue("caption");
        if (this.caption != null)
            this.locCaption = AppBeans.get(MessageTools.class).loadString(messagesPack, this.caption);

        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
    }

    @Override
    public Class getJavaClass() {
        String className = element.attributeValue("paramClass");
        if (className == null)
            className = element.attributeValue("class");
        if (className == null)
            return null;
        else {
            return ScriptingProvider.loadClass(element.attributeValue("paramClass"));
        }
    }

    @Override
    public String getEntityParamWhere() {
        return element.attributeValue("paramWhere");
    }

    @Override
    public String getEntityParamView() {
        return element.attributeValue("paramView");
    }
}