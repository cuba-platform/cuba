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

package com.haulmont.cuba.desktop.exception;

import com.haulmont.bali.util.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is used to configure {@link ExceptionHandlers} via spring.xml.
 *
 * <p>If a project needs specific exception handlers, it should define a bean of this type with its own
 * <strong>id</strong>, e.g. <code>refapp_ExceptionHandlersConfiguration</code>, and set the list of handler class
 * names in <code>handlerClasses</code> property.</p>
 *
 *
 */
public class ExceptionHandlersConfiguration {

    private List<Class> handlerClasses = new ArrayList<>();

    /**
     * Set the list of exception handler class names, usually from spring.xml.
     * @param list  list of class names
     */
    public void setHandlerClasses(List<String> list) {
        for (String className : list) {
            handlerClasses.add(ReflectionHelper.getClass(className));
        }
    }

    /**
     * Get the list of exception handler class names.
     * @return  list of class names
     */
    public List<Class> getHandlerClasses() {
        return handlerClasses;
    }
}