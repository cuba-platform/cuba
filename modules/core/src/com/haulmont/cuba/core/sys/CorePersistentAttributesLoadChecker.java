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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.global.GlobalPersistentAttributesLoadChecker;
import com.haulmont.cuba.core.global.MetadataTools;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class CorePersistentAttributesLoadChecker extends GlobalPersistentAttributesLoadChecker {

    @Inject
    protected Persistence persistence;

    @Inject
    protected MetadataTools metadataTools;

    @Override
    protected boolean isLoadedSpecificCheck(Object entity, String property, MetaClass metaClass, MetaProperty metaProperty) {
        if (metadataTools.isEmbeddable(metaClass)
                || (entity instanceof BaseGenericIdEntity && BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity))) {
            //TODO eude:
            // this is workaround for unexpected EclipseLink behaviour when PersistenceUnitUtil.isLoaded
            // throws exception if embedded entity refers to persistent entity
            return checkIsLoadedWithGetter(entity, property);
        }
        EntityManagerFactory jpaEmf = ((PersistenceImpl) persistence).getJpaEmf(metadataTools.getStoreName(metaClass));
        return jpaEmf.getPersistenceUnitUtil().isLoaded(entity, property);
    }
}