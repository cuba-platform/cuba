/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
