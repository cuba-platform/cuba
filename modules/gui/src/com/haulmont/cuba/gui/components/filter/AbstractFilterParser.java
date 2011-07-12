/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class AbstractFilterParser {
    private static Log log = LogFactory.getLog(AbstractFilterParser.class);

    protected List<AbstractCondition> conditions;
    protected String xml;
    protected String messagesPack;
    protected String filterComponentName;
    protected Datasource datasource;

    public AbstractFilterParser(List<AbstractCondition> conditions, String messagesPack,
                                String filterComponentName, Datasource datasource) {
        this.conditions = conditions;
        this.messagesPack = messagesPack;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
    }

    public AbstractFilterParser(String xml, String messagesPack, String filterComponentName, Datasource datasource) {
        this.xml = xml;
        this.messagesPack = messagesPack;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
    }

    public AbstractFilterParser fromXml() {
        conditions = new ArrayList<AbstractCondition>();
        if (!StringUtils.isBlank(xml)) {
            Element root = Dom4j.readDocument(xml).getRootElement();
            Element andElem = root.element("and");
            if (andElem == null)
                throw new IllegalStateException("Root element doesn't contain 'and': " + xml);

            for (Element element : Dom4j.elements(andElem)) {
                if (!"c".equals(element.getName()))
                    throw new IllegalStateException("Only 'c' elements supported: " + xml);

                String name = element.attributeValue("name");
                if (name == null)
                    throw new IllegalStateException("'name' attribute expected for 'c' element: " + xml);

                AbstractCondition condition;
                String type = element.attributeValue("type");
                condition = createCondition(ConditionType.valueOf(type), element);

                conditions.add(condition);
            }

//            for (Condition condition : conditions) {
//                int i = 0;
//                for (Condition cond : conditions) {
//                    if (cond.getName().equals(condition.getName())) {
//                        cond.getParam().setIdx(i++);
//                    }
//                }
//            }
        }
        return this;
    }

    protected abstract AbstractCondition createCondition(ConditionType type, Element element);

    public AbstractFilterParser toXml() {
        xml = null;
        if (conditions != null && !conditions.isEmpty()) {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("filter");
            Element andElem = root.addElement("and");
            for (AbstractCondition condition : conditions) {
                Element elem = andElem.addElement("c");
                condition.toXml(elem);
            }
            xml = Dom4j.writeDocument(document, true);
        }
        log.trace("toXML: " + xml);
        return this;
    }

    public List<AbstractCondition> getConditions() {
        return conditions;
    }

    public String getXml() {
        return xml;
    }
}
