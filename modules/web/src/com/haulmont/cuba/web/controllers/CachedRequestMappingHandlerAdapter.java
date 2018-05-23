/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.controllers;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.LastModified;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.http.HttpServletRequest;

//TODO: CUBA 7
//Remove CachedAnnotationMethodHandlerAdapter and StaticContentController from platform
//https://github.com/cuba-platform/cuba/issues/894
public class CachedRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {
    @Override
    public long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod) {
        if (handlerMethod.getBean() instanceof LastModified) {
            return ((LastModified) handlerMethod.getBean()).getLastModified(request);
        }
        return -1L;
    }
}
