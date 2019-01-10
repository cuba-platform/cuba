/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.options.DatasourceOptions;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

import javax.annotation.Nullable;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * A class which stores information that can be used
 * when creating a component by the {@link UiComponents}.
 */
public class ComponentGenerationContext {
    protected MetaClass metaClass;
    protected String property;
    @Deprecated
    protected Datasource datasource;
    @Deprecated
    protected CollectionDatasource optionsDatasource;
    protected ValueSource valueSource;
    protected Options options;
    protected Element xmlDescriptor;
    protected Class componentClass;

    /**
     * Creates an instance of ComponentGenerationContext.
     *
     * @param metaClass the entity for which the component is created
     * @param property  the entity attribute for which the component is created
     */
    public ComponentGenerationContext(MetaClass metaClass, String property) {
        checkNotNullArgument(metaClass);
        checkNotNullArgument(property);

        this.metaClass = metaClass;
        this.property = property;
    }

    /**
     * @return the entity for which the component is created
     */
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * Sets the entity for which the component is created, using fluent API method.
     *
     * @param metaClass the entity for which the component is created
     * @return this object
     */
    public ComponentGenerationContext setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
        return this;
    }

    /**
     * @return the entity attribute for which the component is created
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the entity attribute for which the component is created, using fluent API method.
     *
     * @param property the entity attribute for which the component is created
     * @return this object
     */
    public ComponentGenerationContext setProperty(String property) {
        this.property = property;
        return this;
    }

    /**
     * @return a datasource that can be used to create the component
     * @deprecated Use {@link #getValueSource()} instead
     */
    @Deprecated
    @Nullable
    public Datasource getDatasource() {
        return datasource;
    }

    /**
     * Sets a datasource, using fluent API method.
     *
     * @param datasource a datasource
     * @return this object
     * @deprecated Use {@link #setValueSource(ValueSource)} instead
     */
    @Deprecated
    public ComponentGenerationContext setDatasource(Datasource datasource) {
        this.datasource = datasource;
        return this;
    }

    /**
     * @return a datasource that can be used to show options
     * @deprecated Use {@link #getOptions()} instead
     */
    @Deprecated
    @Nullable
    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    /**
     * Sets a datasource that can be used to show options, using fluent API method.
     *
     * @param optionsDatasource a datasource that can be used as optional to create the component
     * @return this object
     * @deprecated Use {@link #setOptions(Options)} instead
     */
    @Deprecated
    public ComponentGenerationContext setOptionsDatasource(CollectionDatasource optionsDatasource) {
        this.optionsDatasource = optionsDatasource;
        return this;
    }

    /**
     * @return a value source that can be used to create the component
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public ValueSource getValueSource() {
        return valueSource == null && datasource != null
                ? new DatasourceValueSource(datasource, property)
                : valueSource;
    }

    /**
     * Sets a value source, using fluent API method.
     *
     * @param valueSource a value source to set
     * @return this object
     */
    public ComponentGenerationContext setValueSource(ValueSource valueSource) {
        this.valueSource = valueSource;
        return this;
    }

    /**
     * @return an options object
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public Options getOptions() {
        return options == null && optionsDatasource != null
                ? new DatasourceOptions(optionsDatasource)
                : options;
    }

    /**
     * Sets an options object, using fluent API method.
     *
     * @param options an options object that can be used as optional to create the component
     * @return this object
     */
    public ComponentGenerationContext setOptions(Options options) {
        this.options = options;
        return this;
    }

    /**
     * @return an XML descriptor which contains additional information
     */
    @Nullable
    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    /**
     * Sets an XML descriptor which contains additional information, using fluent API method.
     *
     * @param xmlDescriptor an XML descriptor which contains additional information
     * @return this object
     */
    public ComponentGenerationContext setXmlDescriptor(Element xmlDescriptor) {
        this.xmlDescriptor = xmlDescriptor;
        return this;
    }

    /**
     * @return a component class for which a component is created
     */
    @Nullable
    public Class getComponentClass() {
        return componentClass;
    }

    /**
     * Sets a component class for which a component is created, using fluent API method.
     *
     * @param componentClass a component class for which a component is created
     * @return this object
     */
    public ComponentGenerationContext setComponentClass(Class componentClass) {
        this.componentClass = componentClass;
        return this;
    }
}
