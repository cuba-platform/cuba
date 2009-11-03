/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2009 11:00:59
 *
 * $Id$
 */
package com.haulmont.cuba.deploymarker;

import org.jboss.ejb3.annotation.Management;

@Management
public interface DeployMarkerMBean {

    String OBJECT_NAME = "haulmont.cuba:service=DeployMarker";

    void create();

    void start();
}
