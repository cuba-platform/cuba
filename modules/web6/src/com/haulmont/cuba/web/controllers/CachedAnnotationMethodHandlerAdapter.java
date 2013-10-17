/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 11.03.2011 16:54:49
 *
 * $Id$
 */
package com.haulmont.cuba.web.controllers;

import org.springframework.web.servlet.mvc.LastModified;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import javax.servlet.http.HttpServletRequest;

public class CachedAnnotationMethodHandlerAdapter extends AnnotationMethodHandlerAdapter {
    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        if (handler instanceof LastModified) {
            return ((LastModified) handler).getLastModified(request);
        }
        return -1L;
    }
}
