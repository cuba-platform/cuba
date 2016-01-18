/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.FetchGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_FetchGroupManager")
public class FetchGroupManager {

    private Logger log = LoggerFactory.getLogger(FetchGroupManager.class);

    @Inject
    private Metadata metadata;

    @Inject
    private MetadataTools metadataTools;

    @Inject
    private ViewRepository viewRepository;

    public void setView(JpaQuery query, String queryString, @Nullable View view, boolean singleResultExpected) {
        Preconditions.checkNotNullArgument(query, "query is null");
        if (view != null) {
            FetchGroup fg = new FetchGroup();
            applyView(query, queryString, fg, view, singleResultExpected);
        } else {
            query.setHint(QueryHints.FETCH_GROUP, null);
        }
    }

    public void addView(JpaQuery query, String queryString, View view, boolean singleResultExpected) {
        Preconditions.checkNotNullArgument(query, "query is null");
        Preconditions.checkNotNullArgument(view, "view is null");

        Map<String, Object> hints = query.getHints();
        FetchGroup fg = null;
        if (hints != null)
            fg = (FetchGroup) hints.get(QueryHints.FETCH_GROUP);
        if (fg == null)
            fg = new FetchGroup();

        applyView(query, queryString, fg, view, singleResultExpected);
    }

    private void applyView(JpaQuery query, String queryString, FetchGroup fetchGroup, View view,
                           boolean singleResultExpected) {
        Set<FetchGroupField> fetchGroupFields = new LinkedHashSet<>();
        processView(view, null, fetchGroupFields);

        Set<String> fetchGroupAttributes = new TreeSet<>();
        Map<String, String> fetchHints = new TreeMap<>(); // sort hints by attribute path

        for (FetchGroupField field : fetchGroupFields) {
            fetchGroupAttributes.add(field.path());
        }
        fetchGroup.setShouldLoadAll(true);

        List<FetchGroupField> refFields = new ArrayList<>();
        for (FetchGroupField field : fetchGroupFields) {
            if (field.metaProperty.getRange().isClass() && !metadataTools.isEmbedded(field.metaProperty))
                refFields.add(field);
        }
        if (!refFields.isEmpty()) {
            MetaClass metaClass = metadata.getClassNN(view.getEntityClass());
            String alias = QueryTransformerFactory.createParser(queryString).getEntityAlias();

            List<FetchGroupField> batchFields = new ArrayList<>();
            List<FetchGroupField> joinFields = new ArrayList<>();

            for (FetchGroupField refField : refFields) {
                if (refField.fetchMode == FetchMode.UNDEFINED) {
                    if (refField.metaProperty.getRange().getCardinality().isMany()) {
                        List<String> masterAttributes = getMasterEntityAttributes(fetchGroup, fetchGroupFields, refField);
                        fetchGroupAttributes.addAll(masterAttributes);
                    }
                    continue;
                }

                boolean selfRef = false;
                for (MetaProperty mp : refField.metaPropertyPath.getMetaProperties()) {
                    if (!mp.getRange().getCardinality().isMany()
                            && metadataTools.isAssignableFrom(mp.getRange().asClass(), metaClass)) {
                        batchFields.add(refField);
                        selfRef = true;
                        break;
                    }
                }

                if (!selfRef) {
                    if (refField.metaProperty.getRange().getCardinality().isMany()) {
                        List<String> masterAttributes = getMasterEntityAttributes(fetchGroup, fetchGroupFields, refField);
                        fetchGroupAttributes.addAll(masterAttributes);

                        if (refField.fetchMode == FetchMode.JOIN) {
                            joinFields.add(refField);
                        } else {
                            batchFields.add(refField);
                        }
                    } else {
                        if (refField.fetchMode == FetchMode.BATCH) {
                            batchFields.add(refField);
                        } else {
                            joinFields.add(refField);
                        }
                    }
                }
            }

            for (FetchGroupField joinField : new ArrayList<>(joinFields)) {
                if (joinField.fetchMode == FetchMode.AUTO) {
                    Optional<FetchMode> parentMode = refFields.stream()
                            .filter(f -> joinField.metaPropertyPath.startsWith(f.metaPropertyPath) && joinField.fetchMode != FetchMode.AUTO)
                            .sorted((f1, f2) -> f1.metaPropertyPath.getPath().length - f2.metaPropertyPath.getPath().length)
                            .findFirst()
                            .map(f -> f.fetchMode);
                    if (parentMode.isPresent()) {
                        if (parentMode.get() == FetchMode.UNDEFINED) {
                            joinFields.remove(joinField);
                        } else if (parentMode.get() == FetchMode.BATCH) {
                            joinFields.remove(joinField);
                            batchFields.add(joinField);
                        }
                    } else {
                        for (FetchGroupField batchField : new ArrayList<>(batchFields)) {
                            if (joinField.metaPropertyPath.startsWith(batchField.metaPropertyPath)) {
                                joinFields.remove(joinField);
                                batchFields.add(joinField);
                            }
                        }
                    }
                }
            }

            QueryParser parser = QueryTransformerFactory.createParser(queryString);

            for (Iterator<FetchGroupField> fieldIt = joinFields.iterator(); fieldIt.hasNext(); ) {
                FetchGroupField joinField = fieldIt.next();
                if (joinField.fetchMode == FetchMode.AUTO && parser.hasIsNullCondition(joinField.path())) {
                    fieldIt.remove();
                    for (Iterator<String> attrIt = fetchGroupAttributes.iterator(); attrIt.hasNext(); ) {
                        String attribute = attrIt.next();
                        if (attribute.startsWith(joinField.path() + ".")) {
                            attrIt.remove();
                        }
                    }
                }
            }


            long toManyCount = refFields.stream()
                    .filter(f -> f.metaProperty.getRange().getCardinality().isMany()).count();

            // For query by ID, remove BATCH mode for to-many attributes that have no nested attributes
            if (singleResultExpected && toManyCount <= 1) {
                for (FetchGroupField batchField : new ArrayList<>(batchFields)) {
                    if (batchField.metaProperty.getRange().getCardinality().isMany()) {
                        boolean hasNested = refFields.stream()
                                .anyMatch(f -> f != batchField && f.metaPropertyPath.startsWith(batchField.metaPropertyPath));
                        if (!hasNested && batchField.fetchMode != FetchMode.BATCH) {
                            batchFields.remove(batchField);
                        }
                    }
                }
            }

            for (FetchGroupField joinField : joinFields) {
                String attr = alias + "." + joinField.path();
                fetchHints.put(attr, QueryHints.LEFT_FETCH);
            }

            for (FetchGroupField batchField : batchFields) {
                if (batchField.fetchMode == FetchMode.BATCH || !singleResultExpected || batchFields.size() > 1) {
                    String attr = alias + "." + batchField.path();
                    fetchHints.put(attr, QueryHints.BATCH);
                }
            }
        }

        if (log.isTraceEnabled())
            log.trace("Fetch group for " + view + ":\n" + fetchGroupAttributes.stream().collect(Collectors.joining("\n")));
        for (String attribute : fetchGroupAttributes) {
            fetchGroup.addAttribute(attribute);
        }

        query.setHint(QueryHints.FETCH_GROUP, fetchGroup);

        if (log.isDebugEnabled())
            log.debug("Fetch modes for " + view + ": " +
                    fetchHints.entrySet().stream()
                        .map(e -> e.getKey() + "=" + (e.getValue().equals(QueryHints.LEFT_FETCH) ? "JOIN" : "BATCH"))
                        .collect(Collectors.joining(", ")));
        for (Map.Entry<String, String> entry : fetchHints.entrySet()) {
            query.setHint(entry.getValue(), entry.getKey());
        }

        query.setHint(QueryHints.BATCH_TYPE, "IN");
    }

    private List<String> getMasterEntityAttributes(FetchGroup fetchGroup, Set<FetchGroupField> fetchGroupFields,
                                           FetchGroupField toManyField) {
        List<String> result = new ArrayList<>();

        MetaClass propMetaClass = toManyField.metaProperty.getRange().asClass();
        propMetaClass.getProperties().stream()
                .filter(mp -> mp.getRange().isClass() && toManyField.metaProperty.getInverse() == mp)
                .findFirst()
                .ifPresent(inverseProp -> {
                    for (FetchGroupField fetchGroupField : fetchGroupFields) {
                        if (fetchGroupField.metaClass.equals(toManyField.metaClass)
                                // add only local properties
                                && !fetchGroupField.metaProperty.getRange().isClass()
                                // do not add properties from subclasses
                                && fetchGroupField.metaProperty.getDomain().equals(inverseProp.getRange().asClass())) {
                            String attribute = toManyField.path() + "." + inverseProp.getName() + "." + fetchGroupField.metaProperty.getName();
                            result.add(attribute);
                        }
                    }
                    if (result.isEmpty()) {
                        result.add(toManyField.path() + "." + inverseProp.getName() + ".id");
                    }
        });

        return result;
    }

    private void processView(View view, FetchGroupField parentField, Set<FetchGroupField> fetchGroupFields) {
        if (view.isIncludeSystemProperties()) {
            includeSystemProperties(view, parentField, fetchGroupFields);
        }

        Class<? extends Entity> entityClass = view.getEntityClass();

        // Always add SoftDelete properties to support EntityManager contract
        if (SoftDelete.class.isAssignableFrom(entityClass)) {
            for (String property : getInterfaceProperties(SoftDelete.class)) {
                fetchGroupFields.add(createFetchGroupField(entityClass, parentField, property));
            }
        }

        // Always add uuid property if the entity has primary key not of type UUID
        if (!BaseUuidEntity.class.isAssignableFrom(entityClass)
                && !EmbeddableEntity.class.isAssignableFrom(entityClass)) {
            fetchGroupFields.add(createFetchGroupField(entityClass, parentField, "uuid"));
        }

        for (ViewProperty property : view.getProperties()) {
            String propertyName = property.getName();
            MetaClass metaClass = metadata.getClassNN(entityClass);
            MetaProperty metaProperty = metaClass.getPropertyNN(propertyName);

            if (parentField != null && property.getView() == null
                    && metaProperty.getRange().isClass()
                    && metaProperty.getRange().asClass().equals(parentField.metaProperty.getDomain())) {
                // do not add immediate back references without a view
                if (metaProperty.getRange().getCardinality().equals(Range.Cardinality.ONE_TO_ONE)) {
                    // For ONE_TO_ONE, add the back reference. This leads to additional useless SELECTs but
                    // saves from "Cannot get unfetched attribute" exception.
                    FetchGroupField field = createFetchGroupField(entityClass, parentField, propertyName, property.getFetchMode());
                    fetchGroupFields.add(field);
                    //noinspection unchecked
                    Class<Entity> javaClass = metaProperty.getRange().asClass().getJavaClass();
                    fetchGroupFields.add(createFetchGroupField(javaClass, field, "id"));
                }
                continue;
            }

            if (metadataTools.isPersistent(metaProperty)) {
                FetchGroupField field = createFetchGroupField(entityClass, parentField, propertyName, property.getFetchMode());
                fetchGroupFields.add(field);
                if (property.getView() != null) {
                    processView(property.getView(), field, fetchGroupFields);
                }
            }

            List<String> relatedProperties = metadataTools.getRelatedProperties(entityClass, propertyName);
            for (String relatedProperty : relatedProperties) {
                if (!view.containsProperty(relatedProperty)) {
                    FetchGroupField field = createFetchGroupField(entityClass, parentField, relatedProperty);
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
        MetaClass metaClass = metadata.getClassNN(entityClass);
        if (BaseEntity.class.isAssignableFrom(entityClass)) {
            for (String property : getInterfaceProperties(BaseEntity.class)) {
                if (metadataTools.isPersistent(metaClass.getPropertyNN(property))) {
                    fetchGroupFields.add(createFetchGroupField(entityClass, parentField, property));
                }
            }
        }
        if (Updatable.class.isAssignableFrom(entityClass)) {
            for (String property : getInterfaceProperties(Updatable.class)) {
                if (metadataTools.isPersistent(metaClass.getPropertyNN(property))) {
                    fetchGroupFields.add(createFetchGroupField(entityClass, parentField, property));
                }
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

    private FetchGroupField createFetchGroupField(Class<? extends Entity> entityClass,
                                                  FetchGroupField parentField,
                                                  String property) {
        return createFetchGroupField(entityClass, parentField, property, FetchMode.AUTO);
    }

    private FetchGroupField createFetchGroupField(Class<? extends Entity> entityClass,
                                                  FetchGroupField parentField,
                                                  String property,
                                                  FetchMode fetchMode) {
        return new FetchGroupField(getRealClass(entityClass, property), parentField, property, fetchMode);
    }

    private MetaClass getRealClass(Class<? extends Entity> entityClass, String property) {
        // todo ?
        return metadata.getClassNN(entityClass);
    }

    protected static class FetchGroupField {
        private final MetaClass metaClass;
        private FetchMode fetchMode;
        private final MetaProperty metaProperty;
        private final MetaPropertyPath metaPropertyPath;

        public FetchGroupField(MetaClass metaClass, FetchGroupField parentField, String property, FetchMode fetchMode) {
            this.metaClass = metaClass;
            this.fetchMode = fetchMode;
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
            if (!metaPropertyPath.equals(that.metaPropertyPath)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = metaClass.hashCode();
            result = 31 * result + metaPropertyPath.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return path();
        }
    }
}
