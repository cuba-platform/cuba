/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

import javax.activation.MimeType;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface Convertor {
    public MimeType getMimeType();

    Object process(Entity entity, MetaClass metaclass, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    Object process(List<Entity> entities, MetaClass metaClass, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    Object process(Map<Entity, Entity> entityMap, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    CommitRequest parseCommitRequest(String content);

    void write(HttpServletResponse response, Object o) throws IOException;

}
