/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.runtimeproperties;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Service(RuntimePropertiesService.NAME)
public class RuntimePropertiesServiceBean implements RuntimePropertiesService {
    @Inject
    protected RuntimePropertiesManagerAPI runtimePropertiesManagerAPI;

    @Override
    public void loadCache(){
        runtimePropertiesManagerAPI.loadCache();
    };

    @Override
    public Collection<Category> getCategoriesForMetaClass(MetaClass metaClass) {
        return runtimePropertiesManagerAPI.getCategoriesForMetaClass(metaClass);
    }

    @Override
    public Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass){
        return runtimePropertiesManagerAPI.getAttributesForMetaClass(metaClass);
    }

    @Nullable
    @Override
    public CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
        return runtimePropertiesManagerAPI.getAttributeForMetaClass(metaClass, code);
    }
}
