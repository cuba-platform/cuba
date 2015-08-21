/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.DsContext;
import groovy.lang.Binding;
import org.dom4j.Element;

import java.util.Locale;
import java.util.Map;

/**
 * Base interface for loaders which create components by XML definitions.
 *
 * @author abramov
 * @version $Id$
 */
public interface ComponentLoader {

    interface Context {
        Map<String, Object> getParams();
        DsContext getDsContext();
        Binding getBinding();

        void addPostInitTask(PostInitTask task);
        void executePostInitTasks();

        Frame getFrame();
        void setFrame(Frame frame);

        String getFullFrameId();
        void setFullFrameId(String frameId);

        String getCurrentFrameId();
        void setCurrentFrameId(String currentFrameId);

        Context getParent();
        void setParent(Context parent);
    }

    /**
     * PostInitTasks are used to perform deferred initialization of visual components
     */
    interface PostInitTask {
        /**
         * This method will be invoked after window initialization
         * @param context loader context
         * @param window top-most window
         */
        void execute(Context context, Frame window);
    }

    Context getContext();

    Locale getLocale();
    void setLocale(Locale locale);

    String getMessagesPack();
    void setMessagesPack(String name);

    /**
     * Load component by XML definition.
     *
     * @param factory   factory instance
     * @param element   XML-element defining the component
     * @param parent    already created parent component instance
     * @return          a new component instance initialized according to XML definition
     */
    Component loadComponent(ComponentsFactory factory, Element element, Component parent);
}