/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

/**
 * Class for existing (described in filter component xml) custom condition descriptor
 * @author devyatkin
 * @version $Id$
 */
@MetaClass(name = "sec$CustomConditionDescriptor")
@SystemLevel
public class CustomConditionDescriptor extends AbstractConditionDescriptor {
    public CustomConditionDescriptor(Element element,
                                     String messagesPack,
                                     String filterComponentName,
                                     CollectionDatasource datasource) {
        super(element.attributeValue("name"), filterComponentName, datasource);
        this.element = element;
        this.messagesPack = messagesPack;
        this.caption = element.attributeValue("caption");
        if (this.caption != null) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            this.locCaption = messageTools.loadString(messagesPack, this.caption);
        }

        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
    }

    @Override
    public AbstractCondition createCondition() {
        CustomCondition condition = new CustomCondition(this,
                element.getText(), getJoinValue(), entityAlias, inExpr);
        return condition;
    }

    protected String getJoinValue() {
        Element joinElement = element.element("join");
        String join;
        if (joinElement != null) {
            join = joinElement.getText();
        } else {
            //for backward compatibility
            join = element.attributeValue("join");
        }
        return join;
    }

    @Override
    public Class getJavaClass() {
        String className = element.attributeValue("paramClass");
        if (className == null) {
            className = element.attributeValue("class");
        }

        if (className == null) {
            return null;
        } else {
            Scripting scripting = AppBeans.get(Scripting.NAME);
            return scripting.loadClass(element.attributeValue("paramClass"));
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