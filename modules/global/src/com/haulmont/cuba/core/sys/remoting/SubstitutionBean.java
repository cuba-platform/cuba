/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

/**
 * @author artamonov
 * @version $Id$
 */
public interface SubstitutionBean<T> {

    void setSubstitutedBean(T bean);
}