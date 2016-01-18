/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.gui.components.Filter;

import javax.annotation.Nullable;

/**
 * Interface implementations is used to convert filter xml to conditions tree and vice versa
 *
 * @author gorbunkov
 * @version $Id$
 */
public interface FilterParser {
    String NAME = "cuba_FilterParser";

    ConditionsTree getConditions(Filter filter, String xml);

    /**
     * Converts filter conditions tree to filter xml
     * @param conditions conditions tree
     * @param valueProperty Describes what parameter value will be serialized to xml: current value or default one
     * @return filter xml
     */
    @Nullable
    String getXml(ConditionsTree conditions, Param.ValueProperty valueProperty);
}
