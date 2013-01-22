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

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractFilterParser;
import com.haulmont.cuba.gui.components.filter.ConditionType;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

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
