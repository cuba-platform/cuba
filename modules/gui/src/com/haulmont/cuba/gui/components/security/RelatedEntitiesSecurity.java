/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.security;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.RelatedEntities;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;

/**
 * Class that is used for checking suitable property for {@link RelatedEntities} component.
 */
@Component(RelatedEntitiesSecurity.NAME)
public final class RelatedEntitiesSecurity {

    public static final String NAME = "cuba_RelatedEntitiesSecurity";

    protected MetadataTools metadataTools;
    protected Security security;

    private RelatedEntitiesSecurity() {
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * Checks suitable property for {@link RelatedEntities} component.
     *
     * @param metaProperty       added meta property
     * @param effectiveMetaClass meta class of entity from which we add property
     * @return true if property can be added to related entities component
     */
    public boolean isSuitableProperty(MetaProperty metaProperty, MetaClass effectiveMetaClass) {
        if (metaProperty.getRange().isClass()
                && !Category.class.isAssignableFrom(metaProperty.getJavaType())) {

            // check that entities are placed in the same data store
            MetaClass propertyMetaClass = metaProperty.getRange().asClass();
            String propertyStore = metadataTools.getStoreName(propertyMetaClass);
            String effectiveStore = metadataTools.getStoreName(effectiveMetaClass);
            if (!Objects.equals(effectiveStore, propertyStore)) {
                return false;
            }

            // check security
            if (security.isEntityAttrPermitted(effectiveMetaClass, metaProperty.getName(), EntityAttrAccess.VIEW)
                    && security.isEntityOpPermitted(metaProperty.getRange().asClass(), EntityOp.READ)) {
                return true;
            }
        }
        return false;
    }
}