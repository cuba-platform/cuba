/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

/**
 * @author artamonov
 * @version $Id$
 */
public interface ClusterNodeIdentifier {

    String NAME = "cuba_ClusterNodeIdentifier";

    String getClusterNodeName();
}