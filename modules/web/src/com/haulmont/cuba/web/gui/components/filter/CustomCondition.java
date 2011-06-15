/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 18:19:44
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Element;

import static org.apache.commons.lang.StringUtils.isBlank;

public class CustomCondition extends Condition {

    public enum Op {
        STARTS_WITH,
        ENDS_WITH
    }

    private Op operator=null;

    private String join;

    public CustomCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, filterComponentName, datasource);

        if (isBlank(caption))
            locCaption = element.attributeValue("locCaption");
        else
            locCaption = MessageUtils.loadString(messagesPack, caption);

        entityAlias = element.attributeValue("entityAlias");
        text = element.getText();
        join = element.attributeValue("join");
        String operatorName = element.attributeValue("operatorType", null);
        if (operatorName != null) {
            operator = Op.valueOf(operatorName);
        }
    }

    public CustomCondition(ConditionDescriptor descriptor, String where, String join, String entityAlias) {
         super(descriptor);
        this.entityAlias = entityAlias;

        this.join = join;
        this.text = where;
        if (param != null)
            text = text.replace("?", ":" + param.getName());
        String operatorName;
        if (descriptor.getElement() == null) {
            operatorName = null;
        } else {
            operatorName = descriptor.getElement().attributeValue("operatorType", null);
        }
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
    public OperationEditor createOperationEditor() {
        return new CustomOperationEditor(this);
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
}
