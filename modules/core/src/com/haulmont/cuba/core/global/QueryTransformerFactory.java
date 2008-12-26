/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.12.2008 10:10:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

public class QueryTransformerFactory
{
    public static QueryTransformer createTransformer(String query, String targetEntity) {
        return new QueryTransformerRegex(query, targetEntity);
    }
}
