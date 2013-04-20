/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.util

/**
 * @author artamonov
 * @version $Id$
 */
class ViewPropertyNode {
    String name
    String view
    Boolean lazy

    List<ViewPropertyNode> viewProperties = []
}