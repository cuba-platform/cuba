/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;

/**
 * Builder for filter condition parameters. Override the class if you want
 * some special Param in your project
 * @author gorbunkov
 * @version $Id$
 */
public interface ConditionParamBuilder {

    String NAME = "cuba_ConditionParamBuilder";

    Param createParam(AbstractCondition condition);

    String createParamName(AbstractCondition condition);
}
