/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.dom4j.Element;

import java.util.List;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class AbstractRuntimePropCondition<T extends AbstractParam> extends AbstractCustomCondition<T> {
    private AbstractParam categoryAttributeParam;
    private UUID categoryId;


    public AbstractRuntimePropCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor, where, join, entityAlias);
        name = RandomStringUtils.randomAlphabetic(10);
        locCaption = MessageProvider.getMessage(MESSAGES_PACK, "runtimePropCondition");
    }

    public AbstractRuntimePropCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
        categoryId = UUID.fromString(element.attributeValue("category"));

        List<Element> paramElements = Dom4j.elements(element, "param");
        for (Element paramElement : paramElements) {
            if (BooleanUtils.toBoolean(paramElement.attributeValue("hidden", "false"), "true", "false")) {
                String paramName = paramElement.attributeValue("name");
                categoryAttributeParam = paramFactory.createParam(paramName, UUID.class, null, null, this.getDatasource(), false, required);
                categoryAttributeParam.parseValue(paramElement.getText());
            }
        }
    }

    public void toXml(Element element) {
        super.toXml(element);
        element.addAttribute("type", ConditionType.RUNTIME_PROPERTY.name());
        element.addAttribute("category", categoryId.toString());
        Element paramElem = element.addElement("param");
        paramElem.addAttribute("name", categoryAttributeParam.getName());
        paramElem.addAttribute("hidden", "true");
        paramElem.setText(categoryAttributeParam.formatValue());
    }

    @Override
    public String getError() {
        String res = super.getError();
        if (res != null)
            return res;

        if (param == null)
            return locCaption + ": " + MessageProvider.getMessage(MESSAGES_PACK, "CustomCondition.paramNotDefined");
        else
            return null;
    }

    public AbstractParam getCategoryAttributeParam() {
        return categoryAttributeParam;
    }

    public void setCategoryAttributeParam(AbstractParam categoryAttributeParam) {
        this.categoryAttributeParam = categoryAttributeParam;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID id) {
        categoryId = id;
    }

    @Override
    public String getOperationCaption() {
        if (getOperator() != null)
            return MessageProvider.getMessage(getOperator());
        else return super.getOperationCaption();
    }

}
