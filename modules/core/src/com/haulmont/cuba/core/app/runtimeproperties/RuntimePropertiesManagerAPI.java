/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.runtimeproperties;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface RuntimePropertiesManagerAPI {
    String NAME = "cuba_RuntimePropertiesManager";

    void loadCache();

    Collection<Category> getCategoriesForMetaClass(MetaClass metaClass);

    Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass);

    @Nullable
    CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code);
}
