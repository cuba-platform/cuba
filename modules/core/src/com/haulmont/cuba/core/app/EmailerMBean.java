/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 10:55:55
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface EmailerMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=Emailer";

    EmailerAPI getAPI();

    String getFromAddress();

    void setFromAddress(String address);

    String sendTestEmail(String addresses);
}
