/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.filter.condition.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.Nullable;

/**
 * Class is used to convert filter xml to conditions tree and vice versa
 * @author devyatkin
 * @version $Id$
 */
public class FilterParser {
    private static Log log = LogFactory.getLog(FilterParser.class);

    public static ConditionsTree getConditions(Filter filter, String xml) {
        ConditionsTree conditions = new ConditionsTree();
        if (!StringUtils.isBlank(xml)) {
            Element root = Dom4j.readDocument(xml).getRootElement();
            Element andElem = root.element("and");
            if (andElem == null)
                throw new IllegalStateException("Root element doesn't contain 'and': " + xml);

            recursiveFromXml(andElem, null, filter, xml, conditions);
        }
        return conditions;
    }

    private static void recursiveFromXml(Element element, Node<AbstractCondition> parentNode, Filter filter, String xml, ConditionsTree conditions) {
        for (Element el : Dom4j.elements(element)) {
            AbstractCondition condition;
            if ("c".equals(el.getName())) {
                String name = el.attributeValue("name");
                if (name == null)
                    throw new IllegalStateException("'name' attribute expected for 'c' element: " + xml);

                String type = el.attributeValue("type");
                condition = createCondition(ConditionType.valueOf(type), el, filter, xml);
                Node<AbstractCondition> node = new Node<AbstractCondition>(condition);
                if (parentNode != null)
                    parentNode.addChild(node);
                else
                    conditions.getRootNodes().add(node);

            } else if ("and".equals(el.getName()) || "or".equals(el.getName())) {
                condition = createCondition(ConditionType.GROUP, el, filter, xml);
                Node<AbstractCondition> node = new Node<>(condition);
                if (parentNode != null)
                    parentNode.addChild(node);
                else
                    conditions.getRootNodes().add(node);

                recursiveFromXml(el, node, filter, xml, conditions);

            } else {
                throw new UnsupportedOperationException("Unknown element: " + el.getName());
            }
        }
    }

    private static AbstractCondition createCondition(ConditionType type, Element element, Filter filter, String xml) {
        String filterComponentName = filter.getId();
        String messagesPack = filter.getFrame().getMessagesPack();
        CollectionDatasource datasource = filter.getDatasource();
        switch (type) {
            case GROUP:
                return new GroupCondition(element, filterComponentName);
            case PROPERTY:
                return new PropertyCondition(element, messagesPack, filterComponentName, datasource);
            case CUSTOM:
                return new CustomCondition(element, messagesPack, filterComponentName, datasource);
            case RUNTIME_PROPERTY:
                return new RuntimePropCondition(element, messagesPack, filterComponentName, datasource);
            default:
                throw new IllegalStateException("Unknown condition type: " + type + " in " + xml);
        }
    }

    /**
     * Converts filter conditions tree to filter xml
     * @param conditions conditions tree
     * @param valueProperty Describes what parameter value will be serialized to xml: current value or default one
     * @return filter xml
     */
    @Nullable
    public static String getXml(ConditionsTree conditions, Param.ValueProperty valueProperty) {
        String xml = null;
        if (conditions != null && !conditions.getRootNodes().isEmpty()) {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("filter");
            Element element = root.addElement("and");
            for (Node<AbstractCondition> node : conditions.getRootNodes()) {
                recursiveToXml(node, element, valueProperty);
            }
            xml = Dom4j.writeDocument(document, true);
        }
        log.trace("toXML: " + xml);
        return xml;
    }

    private static void recursiveToXml(Node<AbstractCondition> node, Element element, Param.ValueProperty valueProperty) {
        AbstractCondition condition = node.getData();
        if (condition.isGroup()) {
            Element elem = element.addElement(((GroupCondition) condition).getGroupType().getXml());
            condition.toXml(elem, valueProperty);
            for (Node<AbstractCondition> n : node.getChildren()) {
                recursiveToXml(n, elem, valueProperty);
            }
        } else {
            Element elem = element.addElement("c");
            condition.toXml(elem, valueProperty);
        }
    }

}