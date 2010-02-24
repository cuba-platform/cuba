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

    LockInfo lock(String name, String id);

    void unlock(String name, String id);

    LockInfo getLockInfo(String name, String id);

    List<LockInfo> getCurrentLocks();
}
