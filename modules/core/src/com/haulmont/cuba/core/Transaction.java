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
    /**
     * Commit current JTA transaction
     */
    void commit();

    /**
     * Commit current JTA transaction and start new one
     */
    void commitRetaining();

    /**
     * Rollback current JTA transaction if there were no succesfull commit before.<br>
     * Should be invoked in <code>finally</code> block
     */
    void end();
}
