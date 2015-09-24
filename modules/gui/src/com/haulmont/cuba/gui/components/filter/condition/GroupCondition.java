/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.condition;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.descriptor.GroupConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.operationedit.GroupOperationEditor;
import org.dom4j.Element;

/**
 * Base GUI class for grouping conditions (AND & OR).
 *
 * @author krivopustov
 * @version $Id$
 */
@MetaClass(name = "sec$GroupCondition")
@SystemLevel
public class GroupCondition extends AbstractCondition {

    protected GroupType groupType;

    protected GroupCondition(GroupCondition condition) {
        super(condition);
        this.groupType = condition.groupType;
    }

    public GroupCondition(Element element, String filterComponentName) {
        super(element, null, filterComponentName, null);
        group = true;
        groupType = GroupType.fromXml(element.getName());
        locCaption = groupType.getLocCaption();
        param = null;
    }

    public GroupCondition(GroupConditionDescriptor descriptor) {
        super(descriptor);
        group = true;
        groupType = descriptor.getGroupType();
        locCaption = groupType.getLocCaption();
        param = null;
    }


    public GroupType getGroupType() {
        return groupType;
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        operationEditor = new GroupOperationEditor(this);
        return operationEditor;
    }

    @Override
    public AbstractCondition createCopy() {
        return new GroupCondition(this);
    }

    @Override
    public boolean canBeRequired() {
        return false;
    }

    @Override
    public boolean canHasWidth() {
        return false;
    }

    @Override
    public boolean canHasDefaultValue() {
        return false;
    }
}