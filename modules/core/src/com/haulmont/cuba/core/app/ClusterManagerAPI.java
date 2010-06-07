/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.06.2010 19:00:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import java.io.Serializable;

public interface ClusterManagerAPI {

    String NAME = "cuba_ClusterManager";

    void send(Serializable message);

    void addListener(Class messageClass, ClusterListener listener);

    void removeListener(Class messageClass, ClusterListener listener);

    boolean isMaster();
}
