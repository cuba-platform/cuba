/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.10.2009 16:13:12
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class FilterParser {

    private static Log log = LogFactory.getLog(FilterParser.class);

    private List<Condition> conditions;
    private String xml;
    private String messagesPack;
    private String filterComponentName;
    private Datasource datasource;

    public FilterParser(List<Condition> conditions, String messagesPack, String filterComponentName, Datasource datasource) {
        this.conditions = conditions;
        this.messagesPack = messagesPack;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
    }

    public FilterParser(String xml, String messagesPack, String filterComponentName, Datasource datasource) {
        this.xml = xml;
        this.messagesPack = messagesPack;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
    }

    public FilterParser fromXml() {
        conditions = new ArrayList<Condition>();
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

                Condition condition;
                String type = element.attributeValue("type");
                if (ConditionType.PROPERTY.name().equals(type))
                    condition = new PropertyCondition(element, messagesPack, filterComponentName, datasource);
                else if (ConditionType.CUSTOM.name().equals(type))
                    condition = new CustomCondition(element, messagesPack, filterComponentName, datasource);
                else
                    throw new IllegalStateException("Unknown condition type: " + type + " in " + xml);

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

    public FilterParser toXml() {
        xml = null;
        if (conditions != null && !conditions.isEmpty()) {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("filter");
            Element andElem = root.addElement("and");
            for (Condition condition : conditions) {
                Element elem = andElem.addElement("c");
                condition.toXml(elem);
            }
            xml = Dom4j.writeDocument(document, true);
        }
        log.trace("toXML: " + xml);
        return this;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public String getXml() {
        return xml;
    }
}
