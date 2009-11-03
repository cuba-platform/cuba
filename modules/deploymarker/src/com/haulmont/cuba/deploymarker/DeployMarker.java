/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2009 11:01:25
 *
 * $Id$
 */
package com.haulmont.cuba.deploymarker;

import org.jboss.ejb3.annotation.Service;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

@Service(objectName = DeployMarkerMBean.OBJECT_NAME)
public class DeployMarker implements DeployMarkerMBean {

    private Log log = LogFactory.getLog(DeployMarker.class);

    public void create() {
        log.debug("create");
    }

    public void start() {
        log.debug("start");
    }
}
