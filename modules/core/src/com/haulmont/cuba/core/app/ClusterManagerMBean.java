/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.06.2010 19:00:16
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface ClusterManagerMBean {

    void start();

    void stop();

    boolean isStarted();

    boolean isMaster();

    String getCurrentView();
}
