/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.RelatedEntitiesAssistant;
import com.haulmont.cuba.gui.components.ValuePathHelper;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.web.gui.components.filter.*;

import javax.annotation.ManagedBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(RelatedEntitiesAssistant.NAME)
public class WebRelatedEntitiesAssistant implements RelatedEntitiesAssistant {

    @Override
    public String getRelatedEntitiesFilterXml(MetaClass metaClass, List<UUID> ids, Filter component) {
        ConditionsTree tree = new ConditionsTree();

        String filterComponentPath = ComponentsHelper.getFilterComponentPath(component);
        String[] strings = ValuePathHelper.parse(filterComponentPath);
        String filterComponentName = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));

        AddConditionDlg.DescriptorBuilder builder = new AddConditionDlg.DescriptorBuilder(
                AppConfig.getMessagesPack(), filterComponentName, component.getDatasource());
        PropertyConditionDescriptor conditionDescriptor = builder.buildPropertyConditionDescriptor("id", "id");

        PropertyCondition condition = (PropertyCondition) conditionDescriptor.createCondition();
        condition.setInExpr(true);
        condition.setHidden(true);
        condition.setOperator(Op.IN);

        Param param = new Param(condition.createParamName(), UUID.class, "", "", component.getDatasource(), metaClass.getProperty("id"), true, true);
        param.setValue(ids);

        condition.setParam(param);

        tree.setRootNodes(Collections.singletonList(new Node<AbstractCondition>(condition)));

        FilterParser filterParser = new FilterParser(tree, AppConfig.getMessagesPack(), filterComponentName, component.getDatasource());
        return filterParser.toXml().getXml();
    }
}