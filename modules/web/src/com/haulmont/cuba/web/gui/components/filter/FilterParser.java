/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractFilterParser;
import com.haulmont.cuba.gui.components.filter.ConditionType;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FilterParser extends AbstractFilterParser {

    public FilterParser(ConditionsTree conditions, String messagesPack, String filterComponentName, Datasource datasource) {
        super(conditions, messagesPack, filterComponentName, datasource);
    }

    public FilterParser(String xml, String messagesPack, String filterComponentName, Datasource datasource) {
        super(xml, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractCondition createCondition(ConditionType type, Element element) {
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
}