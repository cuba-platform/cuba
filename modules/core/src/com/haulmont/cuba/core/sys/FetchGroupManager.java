/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.FetchGroup;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_FetchGroupManager")
public class FetchGroupManager {

    @Inject
    private Metadata metadata;

    @Inject
    private MetadataTools metadataTools;

    @Inject
    private ViewRepository viewRepository;

    public void setView(JpaQuery query, String queryString, @Nullable View view) {
        Preconditions.checkNotNullArgument(query, "query is null");
        if (view != null) {
            FetchGroup fg = new FetchGroup();
            applyView(query, queryString, fg, view);
        } else {
            query.setHint(QueryHints.FETCH_GROUP, null);
        }
    }

    public void addView(JpaQuery query, String queryString, View view) {
        Preconditions.checkNotNullArgument(query, "query is null");
        Preconditions.checkNotNullArgument(view, "view is null");

        Map<String, Object> hints = query.getHints();
        FetchGroup fg = null;
        if (hints != null)
            fg = (FetchGroup) hints.get(QueryHints.FETCH_GROUP);
        if (fg == null)
            fg = new FetchGroup();

        applyView(query, queryString, fg, view);
    }

    private void applyView(JpaQuery query, String queryString, FetchGroup fetchGroup, View view) {
        Set<FetchGroupField> fetchGroupFields = new LinkedHashSet<>();
        processView(view, null, fetchGroupFields);
        for (FetchGroupField field : fetchGroupFields) {
            fetchGroup.addAttribute(field.path());
        }
        fetchGroup.setShouldLoadAll(true);

        query.setHint(QueryHints.FETCH_GROUP, fetchGroup);

        List<FetchGroupField> refFields = new ArrayList<>();
        for (FetchGroupField field : fetchGroupFields) {
            if (field.metaProperty.getRange().isClass() && !metadataTools.isEmbedded(field.metaProperty))
                refFields.add(field);
        }
        if (!refFields.isEmpty()) {
            String alias = QueryTransformerFactory.createParser(queryString).getEntityAlias();
            for (FetchGroupField field : refFields) {
                boolean isMany = false;
                for (MetaProperty mp : field.metaPropertyPath.getMetaProperties()) {
                    if (mp.getRange().getCardinality().isMany()) {
                        isMany = true;
                        break;
                    }
                }
                if (!isMany) {
                    query.setHint(QueryHints.LEFT_FETCH, alias + "." + field.path());
                }
            }
        }
    }

    private void processView(View view, FetchGroupField parentField, Set<FetchGroupField> fetchGroupFields) {
        if (view.isIncludeSystemProperties()) {
            includeSystemProperties(view, parentField, fetchGroupFields);
        }

        Class<? extends Entity> entityClass = view.getEntityClass();

        // Always add SoftDelete properties to support EntityManager contract
        if (SoftDelete.class.isAssignableFrom(entityClass)) {
            for (String property : getInterfaceProperties(SoftDelete.class)) {
                fetchGroupFields.add(createFetchPlanField(entityClass, parentField, property));
            }
        }

        // Always add uuid property if the entity has primary key not of type UUID
        if (!BaseUuidEntity.class.isAssignableFrom(entityClass)
                && !EmbeddableEntity.class.isAssignableFrom(entityClass)) {
            fetchGroupFields.add(createFetchPlanField(entityClass, parentField, "uuid"));
        }

        for (ViewProperty property : view.getProperties()) {
            String propertyName = property.getName();
            MetaClass metaClass = metadata.getClassNN(entityClass);
            MetaProperty metaProperty = metaClass.getPropertyNN(propertyName);

//            if (property.isLazy() || metadataTools.isEmbedded(metaProperty)) {
//                Class propertyClass = metaProperty.getJavaType();
//                MetaClass propertyMetaClass = metadata.getClass(propertyClass);
//                if (propertyMetaClass == null || !metadataTools.isEmbeddable(propertyMetaClass)) {
//                    continue;
//                } else {
//                    LogFactory.getLog(getClass()).warn(String.format(
//                            "Embedded property '%s' of class '%s' cannot have lazy view",
//                            propertyName, metaClass.getName()));
//                }
//            }

            if (metadataTools.isPersistent(metaProperty)) {
                FetchGroupField field = createFetchPlanField(entityClass, parentField, propertyName);
                fetchGroupFields.add(field);
                if (property.getView() != null && !metadataTools.isEmbedded(metaProperty)) { // todo EL: FetchGroups don’t work inside Embedded properties
                    processView(property.getView(), field, fetchGroupFields);
                }
            }

            List<String> relatedProperties = metadataTools.getRelatedProperties(entityClass, propertyName);
            for (String relatedProperty : relatedProperties) {
                if (!view.containsProperty(relatedProperty)) {
                    FetchGroupField field = createFetchPlanField(entityClass, parentField, relatedProperty);
                    fetchGroupFields.add(field);
                    MetaProperty relatedMetaProp = metaClass.getPropertyNN(relatedProperty);
                    if (relatedMetaProp.getRange().isClass()) {
                        View relatedView = viewRepository.getView(relatedMetaProp.getRange().asClass(), View.MINIMAL);
                        processView(relatedView, field, fetchGroupFields);
                    }
                }
            }
        }
    }

    private void includeSystemProperties(View view, FetchGroupField parentField, Set<FetchGroupField> fetchGroupFields) {
        Class<? extends Entity> entityClass = view.getEntityClass();
        if (BaseEntity.class.isAssignableFrom(entityClass)) {
            for (String property : getInterfaceProperties(BaseEntity.class)) {
                fetchGroupFields.add(createFetchPlanField(entityClass, parentField, property));
            }
        }
        if (Updatable.class.isAssignableFrom(entityClass)) {
            for (String property : getInterfaceProperties(Updatable.class)) {
                fetchGroupFields.add(createFetchPlanField(entityClass, parentField, property));
            }
        }
    }

    private List<String> getInterfaceProperties(Class<?> intf) {
        List<String> result = new ArrayList<>();
        for (Method method : intf.getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                result.add(StringUtils.uncapitalize(method.getName().substring(3)));
            }
        }
        return result;
    }

    private FetchGroupField createFetchPlanField(Class<? extends Entity> entityClass, FetchGroupField parentField, String property) {
        return new FetchGroupField(getRealClass(entityClass, property), parentField, property);
    }

    private MetaClass getRealClass(Class<? extends Entity> entityClass, String property) {
        // todo ?
        return metadata.getClassNN(entityClass);
    }

    protected static class FetchGroupField {
        private final MetaClass metaClass;
        private final MetaProperty metaProperty;
        private final MetaPropertyPath metaPropertyPath;

        public FetchGroupField(MetaClass metaClass, FetchGroupField parentField, String property) {
            this.metaClass = metaClass;
            this.metaProperty = metaClass.getPropertyNN(property);
            this.metaPropertyPath = parentField == null ?
                    new MetaPropertyPath(metaClass, metaProperty) :
                    new MetaPropertyPath(parentField.metaPropertyPath, metaProperty);
        }

        public String path() {
            return metaPropertyPath.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FetchGroupField that = (FetchGroupField) o;

            if (!metaClass.equals(that.metaClass)) return false;
            if (!metaProperty.equals(that.metaProperty)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = metaClass.hashCode();
            result = 31 * result + metaProperty.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return path();
        }
    }
}
