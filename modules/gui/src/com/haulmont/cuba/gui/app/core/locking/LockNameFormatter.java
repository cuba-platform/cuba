/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Formatter;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LockNameFormatter implements Formatter<String> {

    protected Metadata metadata = AppBeans.get(Metadata.class);
    protected MessageTools messageTools = AppBeans.get(MessageTools.class);

    public String format(String value) {
        MetaClass mc = metadata.getSession().getClass(value);
        if (mc != null) {
            return messageTools.getEntityCaption(mc);
        } else
            return value;
    }
}
