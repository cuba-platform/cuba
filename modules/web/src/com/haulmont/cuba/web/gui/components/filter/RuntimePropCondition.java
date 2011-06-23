/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.entity.CategoryAttribute;
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
public class RuntimePropCondition extends CustomCondition {
    private Param categoryAttributeParam;
    private UUID categoryId;

    public RuntimePropCondition(ConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor, where, join, entityAlias);
        name = RandomStringUtils.randomAlphabetic(10);
        locCaption = MessageProvider.getMessage(getClass(), "runtimePropCondition");
    }

    public RuntimePropCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
        categoryId = UUID.fromString(element.attributeValue("category"));

        List<Element> paramElements = Dom4j.elements(element, "param");
        for (Element paramElement : paramElements) {
            if (BooleanUtils.toBoolean(paramElement.attributeValue("hidden", "false"), "true", "false")) {
                String paramName = paramElement.attributeValue("name");
                categoryAttributeParam = new Param(paramName, UUID.class, null, null, this.getDatasource(), false);
                categoryAttributeParam.parseValue(paramElement.getText());
            }
        }
    }

    @Override
    public OperationEditor createOperationEditor() {
        return new RuntimePropOperationEditor(this);
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

    public Param getCategoryAttributeParam() {
        return categoryAttributeParam;
    }


    public void setCategoryAttributeParam(Param categoryAttributeParam) {
        this.categoryAttributeParam = categoryAttributeParam;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID id) {
        categoryId = id;
    }
}
