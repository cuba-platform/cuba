/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.client.sys.MessagesClientImpl;
import com.haulmont.cuba.core.global.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class TestMessages extends MessagesClientImpl {

    public TestMessages(UserSessionSource userSessionSource, Configuration configuration,
                        Metadata metadata, ExtendedEntities extendedEntities) {
        this.userSessionSource = userSessionSource;
        this.messageTools = new TestMessageTools(configuration, this, metadata, extendedEntities);
    }

    @Override
    public void init() {
        super.init();
    }
}