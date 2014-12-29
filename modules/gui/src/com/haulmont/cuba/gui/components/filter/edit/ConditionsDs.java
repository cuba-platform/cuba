/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;

import java.util.Map;
import java.util.UUID;

/**
 * Datasource for conditions tree in generic filter editor
 *
 * @author gorbunkov
 * @version $Id$
 */
public class ConditionsDs extends AbstractTreeDatasource<AbstractCondition, UUID> {

    protected ConditionsTree conditionsTree;

    @Override
    protected Tree loadTree(Map params) {
        conditionsTree = (ConditionsTree) params.get("conditions");
        return conditionsTree;
    }

}
