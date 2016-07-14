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
 */
public interface ComponentLoader<T extends Component> {

    interface Context {
        Map<String, Object> getParams();
        DsContext getDsContext();
        Binding getBinding();

        void addPostInitTask(PostInitTask task);
        void executePostInitTasks();

        void addInjectTask(InjectTask task);
        void executeInjectTasks();

        void addInitTask(InitTask task);
        void executeInitTasks();

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
     * PostInitTasks are used to perform deferred initialization of visual components.
     */
    interface PostInitTask {
        /**
         * This method will be invoked after window initialization.
         *
         * @param context loader context
         * @param window  top-most window
         */
        void execute(Context context, Frame window);
    }

    /**
     * For internal use only.
     */
    interface InjectTask {
        /**
         * This method will be invoked after window components loading before window initialization.
         *
         * @param context loader context
         * @param window top-most window
         */
        void execute(Context context, Frame window);
    }

    /**
     * For internal use only.
     */
    interface InitTask {
        /**
         * This method will be invoked after window components loading before window initialization.
         *
         * @param context loader context
         * @param window top-most window
         */
        void execute(Context context, Frame window);
    }

    Context getContext();
    void setContext(Context context);

    Locale getLocale();
    void setLocale(Locale locale);

    String getMessagesPack();
    void setMessagesPack(String name);

    ComponentsFactory getFactory();
    void setFactory(ComponentsFactory factory);

    LayoutLoaderConfig getLayoutLoaderConfig();
    void setLayoutLoaderConfig(LayoutLoaderConfig config);

    Element getElement(Element element);
    void setElement(Element element);

    /**
     * Creates result component by XML-element and loads its Id. Also creates all nested components.
     *
     * @see #getResultComponent()
     */
    void createComponent();

    /**
     * Loads component properties by XML definition.
     *
     * @see #getElement(Element)
     */
    void loadComponent();

    /**
     * Returns previously created instance of component.
     *
     * @see #createComponent()
     */
    Component getResultComponent();
}