/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.01.2009 13:04:10
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

/**
 * This enum defines a behaviour to deal with linked objects in case of soft delete<br>
 * <ul>
 * <li>DENY - throw {@link DeletePolicyException} when linked object exists
 * <li>CASCADE - soft delete the linked object
 * <li>UNLINK - remove link
 * </ul>
 */
public enum DeletePolicy
{
    DENY,
    CASCADE,
    UNLINK
}
