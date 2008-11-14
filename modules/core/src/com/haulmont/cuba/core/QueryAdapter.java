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

import javax.persistence.TemporalType;
import java.util.List;
import java.util.Date;

public interface QueryAdapter
{
    List getResultList();

    Object getSingleResult();

    QueryAdapter setMaxResults(int maxResult);

    QueryAdapter setFirstResult(int startPosition);

    QueryAdapter setParameter(String name, Object value);

    QueryAdapter setParameter(String name, Date value, TemporalType temporalType);

    QueryAdapter setParameter(int position, Object value);

    QueryAdapter setParameter(int position, Date value, TemporalType temporalType);
}
