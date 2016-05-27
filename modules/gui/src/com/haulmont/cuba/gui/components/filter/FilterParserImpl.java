/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.filter.condition.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component(FilterParser.NAME)
public class FilterParserImpl implements FilterParser {
    protected static Logger log = LoggerFactory.getLogger(FilterParser.class);

    @Override
    public ConditionsTree getConditions(Filter filter, String xml) {
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

    protected void recursiveFromXml(Element element, Node<AbstractCondition> parentNode, Filter filter, String xml, ConditionsTree conditions) {
        for (Element el : Dom4j.elements(element)) {
            AbstractCondition condition;
            if ("c".equals(el.getName())) {
                String name = el.attributeValue("name");
                if (name == null)
                    throw new IllegalStateException("'name' attribute expected for 'c' element: " + xml);

                String type = el.attributeValue("type");
                condition = createCondition(ConditionType.valueOf(type), el, filter, xml);
                Node<AbstractCondition> node = new Node<>(condition);
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

    protected AbstractCondition createCondition(ConditionType type, Element element, Filter filter, String xml) {
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
                return new DynamicAttributesCondition(element, messagesPack, filterComponentName, datasource);
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
    @Override
    @Nullable
    public String getXml(ConditionsTree conditions, Param.ValueProperty valueProperty) {
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

    protected void recursiveToXml(Node<AbstractCondition> node, Element element, Param.ValueProperty valueProperty) {
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