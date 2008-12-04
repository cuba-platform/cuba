/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:35:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.service;

import com.haulmont.cuba.core.global.BasicServiceRemote;

import javax.ejb.Local;

@Local
public interface BasicService extends BasicServiceRemote
{
    String JNDI_NAME = "cuba/core/BasicService";
}
