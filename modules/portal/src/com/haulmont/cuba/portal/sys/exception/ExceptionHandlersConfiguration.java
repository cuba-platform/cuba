/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.exception;

import com.haulmont.bali.util.ReflectionHelper;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is used to configure {@link ExceptionHandlers} via spring.xml.
 * <p/>
 * <p>If a project needs specific exception handlers, it should define a bean of this type with its own
 * <strong>id</strong>, e.g. <code>refapp_ExceptionHandlersConfiguration</code>, and set the list of handler class
 * names in <code>handlerClasses</code> property.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class ExceptionHandlersConfiguration {

    protected List<Class> handlerClasses = new ArrayList<>();

    protected List<String> handlerBeans = new ArrayList<>();

    /**
     * Set the list of exception handler class names, usually from spring.xml.
     *
     * @param list list of class names
     */
    public void setHandlerClasses(List<String> list) {
        for (String className : list) {
            handlerClasses.add(ReflectionHelper.getClass(className));
        }
    }

    /**
     * Set the list of exception handler class names, usually from spring.xml.
     *
     * @param list list of bean ids
     */
    public void setHandlerBeans(List<String> list) {
        for (String id : list) {
            if (StringUtils.isNotBlank(id))
                handlerBeans.add(id);
        }
    }

    /**
     * Get the list of exception handler class names.
     *
     * @return list of class names
     */
    public List<Class> getHandlerClasses() {
        return handlerClasses;
    }

    /**
     * Get the list of exception handler bean ids.
     *
     * @return list of bean ids
     */
    public List<String> getHandlerBeans() {
        return handlerBeans;
    }
}
