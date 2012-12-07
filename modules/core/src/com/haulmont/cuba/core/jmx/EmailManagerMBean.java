/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.EmailManagerAPI}.
 *
 * @author ovchinnikov
 * @version $Id$
 */
public interface EmailManagerMBean {

    int getDelayCallCount();

    int getMessageQueueCapacity();
}
