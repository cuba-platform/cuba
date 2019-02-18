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

package com.haulmont.cuba.core.app;

import com.google.common.collect.Iterables;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component(SortJpqlGenerator.NAME)
public class SortJpqlGenerator {
    public static final String NAME = "cuba_SortJpqlGenerator";

    @Inject
    protected Metadata metadata;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected QueryTransformerFactory queryTransformerFactory;
    @Inject
    protected JpqlSortExpressionProvider jpqlSortExpressionProvider;

    public String processQuery(String entityName, String queryString, Sort sort) {
        List<Sort.Order> orders = sort.getOrders();
        if (orders.isEmpty()) {
            return queryString;
        }

        Map<Sort.Direction, List<Sort.Order>> directions = orders.stream()
                .collect(Collectors.groupingBy(Sort.Order::getDirection));
        if (directions.size() > 1) {
            throw new UnsupportedOperationException("Sorting by multiple properties in different directions is not supported");
        }
        boolean asc = directions.keySet().iterator().next() == Sort.Direction.ASC;

        List<String> sortExpressions = new ArrayList<>();

        for (Sort.Order order : sort.getOrders()) {
            MetaClass metaClass = metadata.getClassNN(entityName);
            MetaPropertyPath metaPropertyPath = metadata.getClassNN(entityName).getPropertyPath(order.getProperty());
            checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", order.getProperty(), metaClass);

            sortExpressions.addAll(getPropertySortExpressions(metaPropertyPath, asc));
        }

        return transformQuery(queryString, sortExpressions, asc);
    }

    protected String transformQuery(String queryString, List<String> sortExpressions, boolean asc) {
        if (!sortExpressions.isEmpty()) {
            QueryTransformer transformer = queryTransformerFactory.transformer(queryString);
            transformer.replaceOrderByExpressions(!asc, sortExpressions.toArray(new String[0]));
            return transformer.getResult();
        } else {
            return queryString;
        }
    }

    protected List<String> getPropertySortExpressions(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc) {
        MetaProperty metaProperty = metaPropertyPath.getMetaProperty();

        if (metadataTools.isPersistent(metaPropertyPath)) {
            if (!metaProperty.getRange().isClass()) {

                String sortExpression = metadataTools.isLob(metaProperty) ?
                        getLobPropertySortExpression(metaPropertyPath, sortDirectionAsc) :
                        getDatatypePropertySortExpression(metaPropertyPath, sortDirectionAsc);
                return sortExpression == null ? Collections.emptyList() : Collections.singletonList(sortExpression);

            } else if (!metaProperty.getRange().getCardinality().isMany()) {
                return getEntityPropertySortExpression(metaPropertyPath, sortDirectionAsc);
            }
        } else {
            return getNotPersistentPropertySortExpression(metaPropertyPath, sortDirectionAsc);
        }

        return Collections.emptyList();
    }

    protected String getDatatypePropertySortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc) {
        return jpqlSortExpressionProvider.getDatatypeSortExpression(metaPropertyPath, sortDirectionAsc);
    }

    protected String getLobPropertySortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc) {
        return supportsLobSorting(metaPropertyPath) ? jpqlSortExpressionProvider.getLobSortExpression(metaPropertyPath, sortDirectionAsc) : null;
    }

    protected List<String> getEntityPropertySortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc) {
        Collection<MetaProperty> properties = metadataTools.getNamePatternProperties(
                metaPropertyPath.getMetaProperty().getRange().asClass());

        if (!properties.isEmpty()) {
            List<String> sortExpressions = new ArrayList<>(properties.size());
            for (MetaProperty metaProperty : properties) {
                if (metadataTools.isPersistent(metaProperty)) {
                    MetaPropertyPath childPropertyPath = new MetaPropertyPath(metaPropertyPath, metaProperty);
                    sortExpressions.addAll(getPropertySortExpressions(childPropertyPath, sortDirectionAsc));
                }
            }
            return sortExpressions;
        } else {
            return Collections.singletonList(String.format("{E}.%s", metaPropertyPath.toString()));
        }
    }

    protected List<String> getNotPersistentPropertySortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc) {
        List<String> related = metadataTools.getRelatedProperties(metaPropertyPath.getMetaProperty());
        MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);

        if (!related.isEmpty()) {
            List<String> sortExpressions = new ArrayList<>(related.size());
            for (String item : related) {
                MetaProperty metaProperty = propertyMetaClass.getPropertyNN(item);
                if (metadataTools.isPersistent(metaProperty)) {
                    List<MetaProperty> metaProperties = Arrays.asList(metaPropertyPath.getMetaProperties());
                    metaProperties.set(metaProperties.size() - 1, metaProperty);
                    MetaPropertyPath childPropertyPath = new MetaPropertyPath(metaPropertyPath.getMetaClass(),
                            Iterables.toArray(metaProperties, MetaProperty.class));
                    sortExpressions.addAll(getPropertySortExpressions(childPropertyPath, sortDirectionAsc));
                }
            }
            return sortExpressions;
        }

        return Collections.emptyList();
    }


    protected boolean supportsLobSorting(MetaPropertyPath metaPropertyPath) {
        MetaClass metaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);
        String storeName = metadataTools.getStoreName(metaClass);

        return storeName == null || DbmsSpecificFactory.getDbmsFeatures(storeName).supportsLobSortingAndFiltering();
    }
}
