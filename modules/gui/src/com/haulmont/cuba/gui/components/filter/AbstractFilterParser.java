/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class AbstractFilterParser {
    private static Log log = LogFactory.getLog(AbstractFilterParser.class);

    protected ConditionsTree conditions;
    protected String xml;
    protected String messagesPack;
    protected String filterComponentName;
    protected Datasource datasource;

    public AbstractFilterParser(ConditionsTree conditions, String messagesPack,
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
        conditions = new ConditionsTree();
        if (!StringUtils.isBlank(xml)) {
            Element root = Dom4j.readDocument(xml).getRootElement();
            Element andElem = root.element("and");
            if (andElem == null)
                throw new IllegalStateException("Root element doesn't contain 'and': " + xml);

            recursiveFromXml(andElem, null);
        }
        return this;
    }

    protected void recursiveFromXml(Element element, Node<AbstractCondition> parentNode) {
        for (Element el : Dom4j.elements(element)) {
            AbstractCondition condition;
            if ("c".equals(el.getName())) {
                String name = el.attributeValue("name");
                if (name == null)
                    throw new IllegalStateException("'name' attribute expected for 'c' element: " + xml);

                String type = el.attributeValue("type");
                condition = createCondition(ConditionType.valueOf(type), el);
                Node<AbstractCondition> node = new Node<AbstractCondition>(condition);
                if (parentNode != null)
                    parentNode.addChild(node);
                else
                    conditions.getRootNodes().add(node);

            } else if ("and".equals(el.getName()) || "or".equals(el.getName())) {
                condition = createCondition(ConditionType.GROUP, el);
                Node<AbstractCondition> node = new Node<AbstractCondition>(condition);
                if (parentNode != null)
                    parentNode.addChild(node);
                else
                    conditions.getRootNodes().add(node);

                recursiveFromXml(el, node);

            } else {
                throw new UnsupportedOperationException("Unknown element: " + el.getName());
            }
        }
    }

    protected abstract AbstractCondition createCondition(ConditionType type, Element element);

    public AbstractFilterParser toXml() {
        xml = null;
        if (conditions != null && !conditions.getRootNodes().isEmpty()) {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("filter");
            Element element = root.addElement("and");
            for (Node<AbstractCondition> node : conditions.getRootNodes()) {
                recursiveToXml(node, element);
            }
            xml = Dom4j.writeDocument(document, true);
        }
        log.trace("toXML: " + xml);
        return this;
    }

    protected void recursiveToXml(Node<AbstractCondition> node, Element element) {
        AbstractCondition condition = node.getData();
        if (condition.isGroup()) {
            Element elem = element.addElement(((AbstractGroupCondition) condition).getGroupType().getXml());
            condition.toXml(elem);
            for (Node<AbstractCondition> n : node.getChildren()) {
                recursiveToXml(n, elem);
            }
        } else {
            Element elem = element.addElement("c");
            condition.toXml(elem);
        }
    }

    public ConditionsTree getConditions() {
        return conditions;
    }

    public String getXml() {
        return xml;
    }
}
