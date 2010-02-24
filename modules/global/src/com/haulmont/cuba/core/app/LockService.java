/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2010 16:46:59
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.LockInfo;

import java.util.List;

public interface LockService {

    String NAME = "cuba_LockService";

    /**
     * Try to lock object
     * @param name locking object name
     * @param id locking object ID
     * @return <li>null in case of successful lock,
     * <li>{@link com.haulmont.cuba.core.global.LockNotSupported} instance in case of locking is not configured for this object,
     * <li>{@link LockInfo} instance in case of this object is already locked by someone
     */
    LockInfo lock(String name, String id);

    /**
     * Unlock object
     * @param name locking object name
     * @param id locking object ID
     */
    void unlock(String name, String id);

    /**
     * Get locking status for particular object
     * @param name locking object name
     * @param id locking object ID
     * @return <li>null in case of no lock,
     * <li>{@link com.haulmont.cuba.core.global.LockNotSupported} instance in case of locking is not configured for this object,
     * <li>{@link LockInfo} instance in case of this object is locked by someone
     */
    LockInfo getLockInfo(String name, String id);

    /**
     * List of current locks
     */
    List<LockInfo> getCurrentLocks();
}
