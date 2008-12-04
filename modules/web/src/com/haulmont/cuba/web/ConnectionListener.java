/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 13:10:09
 *
 * $Id$
 */
package com.haulmont.cuba.web;

public interface ConnectionListener
{
    void connectionStateChanged(Connection connection);
}
