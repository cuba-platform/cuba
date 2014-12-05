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
public class TestMessageTools extends MessageTools {
    public TestMessageTools(Configuration configuration, Messages messages,
                            Metadata metadata, ExtendedEntities extendedEntities) {
        super(configuration);

        this.messages = messages;
        this.metadata = metadata;
        this.extendedEntities = extendedEntities;
    }
}