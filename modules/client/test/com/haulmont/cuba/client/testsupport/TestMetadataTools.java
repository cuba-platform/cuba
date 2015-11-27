/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class TestMetadataTools extends MetadataTools {
    /**
     * Constructor used in client-side tests.
     */
    public TestMetadataTools(Metadata metadata) {
        this.metadata = metadata;
        this.persistentAttributesLoadChecker = (entity, property) -> true;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public void setUserSessionSource(UserSessionSource userSessionSource) {
        this.userSessionSource = userSessionSource;
    }
}