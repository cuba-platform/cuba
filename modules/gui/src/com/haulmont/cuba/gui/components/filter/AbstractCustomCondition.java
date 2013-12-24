/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author devyatkin
 * @version $Id$
 */
public abstract class AbstractCustomCondition<T extends AbstractParam> extends AbstractCondition<T> {

    private Op operator;

    private String join;

    public AbstractCustomCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, filterComponentName, datasource);

        if (isBlank(caption))
            locCaption = element.attributeValue("locCaption");
        else
            locCaption = AppBeans.get(MessageTools.class).loadString(messagesPack, caption);

        entityAlias = element.attributeValue("entityAlias");
        text = element.getText();
        join = element.attributeValue("join");
        String operatorName = element.attributeValue("operatorType", null);
        if (operatorName != null) {
            operator = Op.valueOf(operatorName);
        }
    }

    public AbstractCustomCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor);
        this.entityAlias = entityAlias;
        this.join = join;
        this.text = where;
        if (param != null)
            text = StringUtils.replace(text, "?", ":" + param.getName());
        String operatorName;
        operatorName = descriptor.getOperatorType();
        if (operatorName != null) {
            operator = Op.valueOf(operatorName);
        }
    }

    @Override
    public void toXml(Element element) {
        super.toXml(element);

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
    public String getError() {
        String res = super.getError();
        if (res != null)
            return res;

        if (param == null)
            return locCaption + ": " + AppBeans.get(Messages.class).getMessage(MESSAGES_PACK, "CustomCondition.paramNotDefined");
        else
            return null;
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

    public Op getOperator() {
        return operator;
    }

    public void setOperator(Op operator) {
        this.operator = operator;
    }
}
