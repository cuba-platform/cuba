/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.condition;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.operationedit.CustomOperationEditor;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author devyatkin
 * @version $Id$
 */

@MetaClass(name = "sec$CustomCondition")
@SystemLevel
public class CustomCondition extends AbstractCondition {

    private String join;

    protected CustomCondition(AbstractCondition other) {
        super(other);
        this.join = ((CustomCondition) other).join;
        this.operator = ((CustomCondition) other).operator;
    }

    public CustomCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);

        if (isBlank(caption)) {
            locCaption = element.attributeValue("locCaption");
        } else {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            locCaption = messageTools.loadString(messagesPack, caption);
        }

        entityAlias = element.attributeValue("entityAlias");
        join = element.attributeValue("join");
    }

    public CustomCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor);
        this.entityAlias = entityAlias;
        this.join = join;
        this.text = where;
        if (param != null)
            text = StringUtils.replace(text, "?", ":" + param.getName());
    }

    @Override
    public void toXml(Element element, Param.ValueProperty valueProperty) {
        super.toXml(element, valueProperty);

        element.addAttribute("type", ConditionType.CUSTOM.name());

        if (isBlank(caption)) {
            element.addAttribute("locCaption", locCaption);
        }

        element.addAttribute("entityAlias", entityAlias);

        if (!isBlank(join)) {
            element.addAttribute("join", StringEscapeUtils.escapeXml(join));
        }
        if (operator != null) {
            element.addAttribute("operatorType", operator.name());
        }
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        operationEditor = new CustomOperationEditor(this);
        return operationEditor;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getWhere() {
        return text;
    }

    public void setWhere(String where) {
        this.text = where;
    }

    @Override
    public AbstractCondition createCopy() {
        return new CustomCondition(this);
    }
}
