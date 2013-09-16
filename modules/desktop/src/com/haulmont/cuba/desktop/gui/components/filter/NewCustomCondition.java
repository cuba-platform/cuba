/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import org.apache.commons.lang.RandomStringUtils;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class NewCustomCondition extends CustomCondition {

    public NewCustomCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor, where, join, entityAlias);

        name = RandomStringUtils.randomAlphabetic(10);
        locCaption = MessageProvider.getMessage(MESSAGES_PACK, "newCustomCondition");
    }

    @Override
    public OperationEditor createOperationEditor() {
        return new CustomOperationEditor(this);
    }
}
