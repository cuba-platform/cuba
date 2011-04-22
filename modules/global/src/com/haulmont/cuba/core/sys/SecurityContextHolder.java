/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface SecurityContextHolder {

    SecurityContext get();

    void set(SecurityContext securityContext);
}
