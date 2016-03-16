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