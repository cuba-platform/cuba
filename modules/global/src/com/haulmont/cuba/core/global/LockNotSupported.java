/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

/**
 * A special case of {@link LockInfo} that is returned from <code>LockManagerAPI</code> and
 * {@link com.haulmont.cuba.core.app.LockService} methods to indicate that locking is not supported for a
 * specified object type.
 *
 * @author krivopustov
 * @version $Id$
 */
public class LockNotSupported extends LockInfo {

    private static final long serialVersionUID = -5382095361423998544L;

    public LockNotSupported() {
        super(null, null, null);
    }
}
