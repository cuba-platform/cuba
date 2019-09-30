/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.sys;

import com.google.common.base.Splitter;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component(ViewLoader.NAME)
public class ViewLoader {

    public static final String NAME = "cuba_ViewLoader";

    private final Logger log = LoggerFactory.getLogger(ViewLoader.class);

    protected Metadata metadata;

    @Inject
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ViewInfo getViewInfo(Element viewElem) {
        return getViewInfo(viewElem, null);
    }

    public ViewInfo getViewInfo(Element viewElem, @Nullable MetaClass providedMetaClass) {
        String viewName = viewElem.attributeValue("name");

        MetaClass metaClass = providedMetaClass != null ? providedMetaClass : getMetaClass(viewElem);

        ViewInfo viewInfo = new ViewInfo(metaClass, viewName);

        boolean overwrite = Boolean.parseBoolean(viewElem.attributeValue("overwrite"));

        String extended = viewElem.attributeValue("extends");
        List<String> ancestors = null;

        if (isNotBlank(extended)) {
            ancestors = splitExtends(extended);
        }

        if (!overwrite && ancestors != null) {
            overwrite = ancestors.contains(viewName);
        }

        viewInfo.setAncestors(ancestors);
        viewInfo.setOverwrite(overwrite);
        viewInfo.setSystemProperties(Boolean.parseBoolean(viewElem.attributeValue("systemProperties")));
        return viewInfo;
    }


    public View.ViewParams getViewParams(ViewInfo viewInfo, Function<String, View> ancestorViewResolver) {
        MetaClass metaClass = viewInfo.getMetaClass();
        String viewName = viewInfo.name;

        View.ViewParams viewParams = new View.ViewParams().entityClass(metaClass.getJavaClass()).name(viewName);
        if (isNotEmpty(viewInfo.ancestors)) {
            List<View> ancestorsViews = viewInfo.ancestors.stream()
                    .map(ancestorViewResolver)
                    .collect(Collectors.toList());

            viewParams.src(ancestorsViews);
        }
        viewParams.includeSystemProperties(viewInfo.systemProperties);
        return viewParams;
    }


    public void loadViewProperties(Element viewElem,
                                   View view,
                                   boolean systemProperties,
                                   BiFunction<MetaClass, String, View> refViewResolver) {
        final MetaClass metaClass = metadata.getClassNN(view.getEntityClass());
        final String viewName = view.getName();

        Set<String> propertyNames = new HashSet<>();

        for (Element propElem : viewElem.elements("property")) {
            String propertyName = propElem.attributeValue("name");

            if (propertyNames.contains(propertyName)) {
                throw new DevelopmentException(String.format("View %s/%s definition error: view declared property %s twice",
                        metaClass.getName(), viewName, propertyName));
            }
            propertyNames.add(propertyName);

            MetaProperty metaProperty = metaClass.getProperty(propertyName);
            if (metaProperty == null) {
                throw new DevelopmentException(String.format("View %s/%s definition error: property %s doesn't exist",
                        metaClass.getName(), viewName, propertyName));
            }

            View refView = null;
            String refViewName = propElem.attributeValue("view");

            MetaClass refMetaClass;
            Range range = metaProperty.getRange();
            if (range == null) {
                throw new RuntimeException("cannot find range for meta property: " + metaProperty);
            }

            final List<Element> propertyElements = Dom4j.elements(propElem, "property");
            boolean inlineView = !propertyElements.isEmpty();

            if (!range.isClass() && (refViewName != null || inlineView)) {
                throw new DevelopmentException(String.format("View %s/%s definition error: property %s is not an entity",
                        metaClass.getName(), viewName, propertyName));
            }

            if (refViewName != null) {
                refMetaClass = getMetaClass(propElem, range);
                refView = refViewResolver.apply(refMetaClass, refViewName);
            }

            if (inlineView) {
                // try to import anonymous views
                Class<? extends Entity> rangeClass = range.asClass().getJavaClass();

                if (refView != null) {
                    refView = new View(refView, rangeClass, "", false); // system properties are already in the source view
                } else {
                    ViewProperty existingProperty = view.getProperty(propertyName);
                    if (existingProperty != null && existingProperty.getView() != null) {
                        refView = new View(existingProperty.getView(), rangeClass, "", systemProperties);
                    } else {
                        refView = new View(rangeClass, systemProperties);
                    }
                }
                loadViewProperties(propElem, refView, systemProperties, refViewResolver);
            }

            FetchMode fetchMode = FetchMode.AUTO;
            String fetch = propElem.attributeValue("fetch");
            if (fetch != null)
                fetchMode = FetchMode.valueOf(fetch);

            view.addProperty(propertyName, refView, fetchMode);
        }
    }

    protected String getViewName(Element viewElem) {
        String viewName = viewElem.attributeValue("name");
        if (StringUtils.isBlank(viewName))
            throw new DevelopmentException("Invalid view definition: no 'name' attribute present");
        return viewName;
    }

    protected MetaClass getMetaClass(Element viewElem) {
        MetaClass metaClass;
        String entity = viewElem.attributeValue("entity");
        if (StringUtils.isBlank(entity)) {
            String className = viewElem.attributeValue("class");
            if (StringUtils.isBlank(className))
                throw new DevelopmentException("Invalid view definition: no 'entity' or 'class' attribute present");
            Class entityClass = ReflectionHelper.getClass(className);
            metaClass = metadata.getClassNN(entityClass);
        } else {
            metaClass = metadata.getClassNN(entity);
        }
        return metaClass;
    }

    protected MetaClass getMetaClass(String entityName, String entityClass) {
        if (entityName != null) {
            return metadata.getClassNN(entityName);
        } else {
            return metadata.getClassNN(ReflectionHelper.getClass(entityClass));
        }
    }

    protected MetaClass getMetaClass(Element propElem, Range range) {
        MetaClass refMetaClass;
        String refEntityName = propElem.attributeValue("entity"); // this attribute is deprecated
        if (refEntityName == null) {
            refMetaClass = range.asClass();
        } else {
            refMetaClass = metadata.getClassNN(refEntityName);
        }
        return refMetaClass;
    }

    protected void checkDuplicates(Element rootElem) {
        Set<String> checked = new HashSet<>();
        for (Element viewElem : Dom4j.elements(rootElem, "view")) {
            String viewName = getViewName(viewElem);
            String key = getMetaClass(viewElem) + "/" + viewName;
            if (!Boolean.parseBoolean(viewElem.attributeValue("overwrite"))) {
                String extend = viewElem.attributeValue("extends");
                if (extend != null) {
                    List<String> ancestors = splitExtends(extend);

                    if (!ancestors.contains(viewName) && checked.contains(key)) {
                        log.warn("Duplicate view definition without 'overwrite' attribute and not extending parent view: " + key);
                    }
                }
            }
            checked.add(key);
        }
    }

    protected List<String> splitExtends(String extend) {
        return Splitter.on(',').omitEmptyStrings().trimResults().splitToList(extend);
    }

    public static class ViewInfo {
        protected MetaClass metaClass;
        protected String name;
        protected List<String> ancestors;
        protected boolean overwrite = false;
        protected boolean systemProperties = false;

        public ViewInfo(MetaClass metaClass, String name) {
            this.metaClass = metaClass;
            this.name = name;
        }

        public MetaClass getMetaClass() {
            return metaClass;
        }

        public Class getJavaClass() {
            return metaClass.getJavaClass();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAncestors() {
            return ancestors;
        }

        public void setAncestors(List<String> ancestors) {
            this.ancestors = ancestors;
        }

        public boolean isOverwrite() {
            return overwrite;
        }

        public void setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
        }

        public boolean isSystemProperties() {
            return systemProperties;
        }

        public void setSystemProperties(boolean systemProperties) {
            this.systemProperties = systemProperties;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ViewInfo)) {
                return false;
            }

            ViewInfo that = (ViewInfo) obj;
            return this.getJavaClass() == that.getJavaClass() && Objects.equals(this.name, that.name);
        }

        @Override
        public int hashCode() {
            int result = getJavaClass().hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}
