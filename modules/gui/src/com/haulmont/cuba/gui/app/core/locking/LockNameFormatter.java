/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.components.Formatter;

public class LockNameFormatter implements Formatter<String> {

    public String format(String value) {
        MetaClass mc = MetadataProvider.getSession().getClass(value);
        if (mc != null) {
            return MessageUtils.getEntityCaption(mc);
        } else
            return value;
    }
}
