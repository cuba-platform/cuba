/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import org.dom4j.Element;

/**
 * Base GUI class for grouping conditions (AND & OR).
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractGroupCondition<T extends AbstractParam> extends AbstractCondition<T> {

    protected GroupType groupType;

    protected AbstractGroupCondition(Element element, String filterComponentName) {
        super(element, filterComponentName, null);
        group = true;
        groupType = GroupType.fromXml(element.getName());
        locCaption = MessageProvider.getMessage(MESSAGES_PACK, "GroupType." + groupType);
        param = null;
    }

    protected AbstractGroupCondition(AbstractGroupConditionDescriptor<T> descriptor) {
        super(descriptor);
        group = true;
        groupType = descriptor.getGroupType();
        locCaption = MessageProvider.getMessage(MESSAGES_PACK, "GroupType." + groupType);
        param = null;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    @Override
    public void toXml(Element element) {
        super.toXml(element);
    }
}
