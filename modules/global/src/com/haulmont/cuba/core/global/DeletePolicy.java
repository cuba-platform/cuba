/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
