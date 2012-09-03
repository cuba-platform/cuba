/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:31:30
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.DsContext;
import groovy.lang.Binding;
import org.dom4j.Element;

import java.util.Locale;
import java.util.Map;

public interface ComponentLoader {

    public interface Context {
        Map<String, Object> getParams();
        DsContext getDsContext();
        Binding getBinding();

        void addPostInitTask(PostInitTask task);
        void executePostInitTasks();

        IFrame getFrame();
        void setFrame(IFrame frame);

        Context getParent();
        void setParent(Context parent);
    }

    /**
     * PostInitTasks are used to perform deferred initialization of visual components
     */
    public interface PostInitTask {
        /**
         * This method will be invoked after window initialization
         * @param context loader context
         * @param window top-most window
         */
        void execute(Context context, IFrame window);
    }

    Context getContext();

    Locale getLocale();
    void setLocale(Locale locale);

    String getMessagesPack();
    void setMessagesPack(String name);

    Component loadComponent(ComponentsFactory factory, Element element, Component parent) 
            throws InstantiationException, IllegalAccessException;
}
