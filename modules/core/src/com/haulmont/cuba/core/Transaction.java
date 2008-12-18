/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.11.2008 18:35:31
 *
 * $Id$
 */
package com.haulmont.cuba.core;

public interface Transaction
{
    void commit();

    void commitRetaining();

    void end();
}
