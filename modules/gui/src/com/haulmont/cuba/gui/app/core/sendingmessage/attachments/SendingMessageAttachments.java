/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.sendingmessage.attachments;

import com.haulmont.cuba.gui.components.AbstractLookup;

import java.util.Map;

/**
 * @author gorelov
 * @version $Id$
 */
public class SendingMessageAttachments extends AbstractLookup {

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidth(800);
        getDialogParams().setHeight(500);
        getDialogParams().setResizable(true);
    }
}