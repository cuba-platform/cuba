/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:09:14
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.global.BasicInvocationContext;

import java.util.List;

public interface DataService {
    <T> T create(T entity);
    <T> T update(T entity);
    void delete(BasicInvocationContext ctx);

    <T> T get(BasicInvocationContext ctx);

    <T> T load(BasicInvocationContext ctx);
    <T> List<T> loadList(BasicInvocationContext ctx);
}
