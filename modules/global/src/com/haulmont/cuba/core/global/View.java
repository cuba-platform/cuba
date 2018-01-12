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
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Creatable;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Class to declare a graph of objects that must be retrieved from the database.
 * <p>
 * A view can be constructed in Java code or defined in XML and deployed
 * to the {@link com.haulmont.cuba.core.global.ViewRepository} for recurring usage.
 * </p>
 * There are the following predefined view types:
 * <ul>
 * <li>{@link #LOCAL}</li>
 * <li>{@link #MINIMAL}</li>
 * <li>{@link #BASE}</li>
 * </ul>
 *
 */
public class View implements Serializable {

    /**
     * Parameters object to be used in constructors.
     */
    public static class ViewParams {
        protected List<View> src = Collections.emptyList();
        protected Class<? extends Entity> entityClass;
        protected String name;
        protected boolean includeSystemProperties;

        public ViewParams src(View src) {
            this.src = Collections.singletonList(src);
            return this;
        }

        public void src(List<View> sources) {
            this.src = sources;
        }

        public ViewParams entityClass(Class<? extends Entity> entityClass) {
            this.entityClass = entityClass;
            return this;
        }

        public ViewParams name(String name) {
            this.name = name;
            return this;
        }

        public ViewParams includeSystemProperties(boolean includeSystemProperties) {
            this.includeSystemProperties = includeSystemProperties;
            return this;
        }
    }

    /**
     * Includes all local non-system properties.
     */
    public static final String LOCAL = "_local";

    /**
     * Includes only properties contained in {@link com.haulmont.chile.core.annotations.NamePattern}.
     */
    public static final String MINIMAL = "_minimal";

    /**
     * Includes all local non-system properties and properties defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * (effectively {@link #MINIMAL} + {@link #LOCAL}).
     */
    public static final String BASE = "_base";

    private static final long serialVersionUID = 4313784222934349594L;

    private Class<? extends Entity> entityClass;

    private String name;

    private Map<String, ViewProperty> properties = new LinkedHashMap<>();

    private boolean loadPartialEntities;

    public View(Class<? extends Entity> entityClass) {
        this(entityClass, "", true);
    }

    public View(Class<? extends Entity> entityClass, boolean includeSystemProperties) {
        this(entityClass, "", includeSystemProperties);
    }

    public View(Class<? extends Entity> entityClass, String name) {
        this(entityClass, name, true);
    }

    public View(Class<? extends Entity> entityClass, String name, boolean includeSystemProperties) {
        this(new ViewParams().entityClass(entityClass)
                        .name(name)
                        .includeSystemProperties(includeSystemProperties)
        );
    }

    public View(View src, String name, boolean includeSystemProperties) {
        this(src, null, name, includeSystemProperties);
    }

    public View(View src, @Nullable Class<? extends Entity> entityClass, String name, boolean includeSystemProperties) {
        this(new ViewParams().src(src)
                        .entityClass(entityClass != null ? entityClass : src.entityClass)
                        .name(name)
                        .includeSystemProperties(includeSystemProperties)
        );
    }

    public View(ViewParams viewParams) {
        this.entityClass = viewParams.entityClass;
        this.name = viewParams.name != null ? viewParams.name : "";
        if (viewParams.includeSystemProperties) {
            for (String propertyName : findSystemProperties(entityClass)) {
                addProperty(propertyName);
            }
        }
        List<View> sources = viewParams.src;

        if (isNotEmpty(sources)) {
            Class<? extends Entity> entityClass = sources.get(0).entityClass;

            if (this.entityClass == null) {
                this.entityClass = entityClass;
            }

            for (View view : sources) {
                putProperties(this.properties, view.getProperties());
            }
        }
    }

    protected void putProperties(Map<String, ViewProperty> thisProperties, Collection<ViewProperty> sourceProperties) {
        for (ViewProperty sourceProperty : sourceProperties) {
            String sourcePropertyName = sourceProperty.getName();

            if (thisProperties.containsKey(sourcePropertyName)) {
                View sourcePropertyView = sourceProperty.getView();

                if (sourcePropertyView != null && isNotEmpty(sourcePropertyView.getProperties())) {

                    Map<String, ViewProperty> thisViewProperties = thisProperties.get(sourcePropertyName).getView().properties;
                    putProperties(thisViewProperties, sourcePropertyView.getProperties());
                }

            } else {
                thisProperties.put(sourceProperty.getName(), sourceProperty);
            }
        }
    }

    public static View copy(@Nullable View view) {
        if (view == null) {
            return null;
        }

        View.ViewParams viewParams = new View.ViewParams()
                .entityClass(view.getEntityClass())
                .name(view.getName());
        View copy = new View(viewParams);
        for (ViewProperty property : view.getProperties()) {
            copy.addProperty(property.getName(), copy(property.getView()), property.getFetchMode());
        }

        return copy;
    }

    /**
     * @return entity class this view belongs to
     */
    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    /**
     * @return view name, unique within an entity
     */
    public String getName() {
        return name;
    }

    /**
     * @return collection of properties
     */
    public Collection<ViewProperty> getProperties() {
        return properties.values();
    }

    /**
     * Add a property to this view.
     * @param name  property name
     * @param view  a view for a reference attribute, or null
     * @param fetchMode fetch mode for a reference attribute
     * @return      this view instance for chaining
     */
    public View addProperty(String name, @Nullable View view, FetchMode fetchMode) {
        properties.put(name, new ViewProperty(name, view, fetchMode));
        return this;
    }

    @Deprecated
    public View addProperty(String name, @Nullable View view, boolean lazy) {
        properties.put(name, new ViewProperty(name, view, lazy));
        return this;
    }

    /**
     * Add a property to this view.
     * @param name  property name
     * @param view  a view for a reference attribute, or null
     * @return      this view instance for chaining
     */
    public View addProperty(String name, View view) {
        properties.put(name, new ViewProperty(name, view));
        return this;
    }

    /**
     * Add a property to this view.
     * @param name  property name
     * @return      this view instance for chaining
     */
    public View addProperty(String name) {
        properties.put(name, new ViewProperty(name, null));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        View view = (View) o;

        return entityClass.equals(view.entityClass) && name.equals(view.name);
    }

    @Override
    public int hashCode() {
        int result = entityClass.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return entityClass.getName() + "/" + name;
    }

    /**
     * Get directly owned view property by name.
     * @param name  property name
     * @return      view property instance or null if it is not found
     */
    @Nullable
    public ViewProperty getProperty(String name) {
        return properties.get(name);
    }

    /**
     * Check if a directly owned property with the given name exists in the view.
     * @param name  property name
     * @return      true if such property found
     */
    public boolean containsProperty(String name) {
        return properties.containsKey(name);
    }

    /**
     * If true, the view affects loading of local attributes. If false, only reference attributes are affected and
     * local are always loaded.
     *
     * @see #setLoadPartialEntities(boolean)
     */
    public boolean loadPartialEntities() {
        return loadPartialEntities;
    }

    /**
     * Specifies whether the view affects loading of local attributes. By default only reference attributes are affected and
     * local are always loaded.
     *
     * @param loadPartialEntities true to affect loading of local attributes
     * @return this view instance for chaining
     */
    public View setLoadPartialEntities(boolean loadPartialEntities) {
        this.loadPartialEntities = loadPartialEntities;
        return this;
    }

    /**
     * DEPRECATED since v.6
     */
    @Deprecated
    public boolean hasLazyProperties() {
        return false;
    }

    protected Set<String> findSystemProperties(Class entityClass) {
        Set<String> result = new LinkedHashSet<>();

        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getClassNN(entityClass);

        String pkName = metadata.getTools().getPrimaryKeyName(metaClass);
        if (pkName != null) {
            result.add(pkName);
        }

        addSystemPropertiesFrom(Creatable.class, entityClass, metaClass, metadata, result);
        addSystemPropertiesFrom(Updatable.class, entityClass, metaClass, metadata, result);
        addSystemPropertiesFrom(SoftDelete.class, entityClass, metaClass, metadata, result);

        return result;
    }

    protected void addSystemPropertiesFrom(Class<?> baseClass, Class<?> entityClass, MetaClass metaClass,
                                           Metadata metadata, Set<String> result) {
        if (baseClass.isAssignableFrom(entityClass)) {
            MetadataTools metadataTools = metadata.getTools();
            for (String property : getInterfaceProperties(baseClass)) {
                MetaProperty metaProperty = metaClass.getProperty(property);
                if (metaProperty != null && metadataTools.isPersistent(metaProperty)) {
                    result.add(property);
                }
            }
        }
    }

    protected List<String> getInterfaceProperties(Class<?> intf) {
        List<String> result = new ArrayList<>();
        for (Method method : intf.getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                result.add(StringUtils.uncapitalize(method.getName().substring(3)));
            }
        }
        return result;
    }
}