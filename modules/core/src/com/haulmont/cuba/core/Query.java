/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 18:02:40
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;

import javax.persistence.TemporalType;
import java.util.List;
import java.util.Date;

public interface Query
{
    List getResultList();

    Object getSingleResult();

    int executeUpdate();

    Query setMaxResults(int maxResult);

    Query setFirstResult(int startPosition);

    Query setParameter(String name, Object value);

    Query setParameter(String name, Date value, TemporalType temporalType);

    Query setParameter(int position, Object value);

    Query setParameter(int position, Date value, TemporalType temporalType);

    Query setView(View view);
}
