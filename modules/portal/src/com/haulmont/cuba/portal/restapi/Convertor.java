/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.activation.MimeType;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Convertor {
    public MimeType getMimeType();

    Object process(Entity entity, MetaClass metaclass, String requestURI, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    Object process(List<Entity> entities, MetaClass metaClass, String requestURI, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    Object process(Set<Entity> entities, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    Object processServiceMethodResult(Object result, String requestURI, @Nullable String viewName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    CommitRequest parseCommitRequest(String content);

    void write(HttpServletResponse response, Object o) throws IOException;
}