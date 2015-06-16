/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.*;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Immutable
public class DynamicAttributesCache implements Serializable {
    protected final Multimap<String, Category> categoriesCache;
    protected final Map<String, Map<String, CategoryAttribute>> attributesCache;
    protected final Date creationDate;

    public DynamicAttributesCache(Multimap<String, Category> categoriesCache,
                                  Map<String, Map<String, CategoryAttribute>> attributesCache,
                                  Date creationDate) {
        this.categoriesCache = categoriesCache;
        this.attributesCache = attributesCache;
        this.creationDate = creationDate;
    }

    public Collection<Category> getCategoriesForMetaClass(MetaClass metaClass) {
        MetaClass targetMetaClass = resolveTargetMetaClass(metaClass);
        return new ArrayList<>(categoriesCache.get(targetMetaClass.getName()));
    }

    public Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass) {
        MetaClass targetMetaClass = resolveTargetMetaClass(metaClass);
        Collection<Category> categories = categoriesCache.get(targetMetaClass.getName());
        List<CategoryAttribute> categoryAttributes = new ArrayList<>();
        for (Category category : categories) {
            categoryAttributes.addAll(Collections2.filter(category.getCategoryAttrs(), new Predicate<CategoryAttribute>() {
                @Override
                public boolean apply(@Nullable CategoryAttribute input) {
                    return input != null && StringUtils.isNotBlank(input.getCode());
                }
            }));
        }
        return categoryAttributes;
    }

    @Nullable
    public CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
        MetaClass targetMetaClass = resolveTargetMetaClass(metaClass);
        Map<String, CategoryAttribute> attributes = attributesCache.get(targetMetaClass.getName());
        if (attributes != null) {
            return attributes.get(code);
        }

        return null;
    }

    protected MetaClass resolveTargetMetaClass(MetaClass metaClass) {
        if (metaClass == null) {
            return null;
        }

        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass targetMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
        if (targetMetaClass == null) {
            targetMetaClass = metaClass;
        }
        return targetMetaClass;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
