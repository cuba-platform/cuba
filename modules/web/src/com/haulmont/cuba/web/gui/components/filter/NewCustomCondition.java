/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import org.apache.commons.lang.RandomStringUtils;

/**
 * @author krivopustov
 * @version $Id$
 */
public class NewCustomCondition extends CustomCondition {

    public NewCustomCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor, where, join, entityAlias);

        name = RandomStringUtils.randomAlphabetic(10);
        locCaption = AppBeans.get(Messages.class).getMessage(MESSAGES_PACK, "newCustomCondition");
    }

//    @Override
//    public OperationEditor createOperationEditor() {
//        return new CustomOperationEditor(this);
//    }
}