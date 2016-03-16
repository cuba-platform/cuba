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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.UuidConverter;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEventListener;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.AggregateObjectMapping;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class EclipseLinkSessionEventListener extends SessionEventAdapter {

    private Metadata metadata = AppBeans.get(Metadata.NAME);

    private DescriptorEventListener descriptorEventListener = AppBeans.get(EclipseLinkDescriptorEventListener.NAME);

    @Override
    public void preLogin(SessionEvent event) {

        Session session = event.getSession();
        Map<Class, ClassDescriptor> descriptorMap = session.getDescriptors();

        for (Map.Entry<Class, ClassDescriptor> entry : descriptorMap.entrySet()) {
            MetaClass metaClass = metadata.getSession().getClassNN(entry.getKey());
            ClassDescriptor desc = entry.getValue();

            if (BaseEntity.class.isAssignableFrom(desc.getJavaClass())) {
                desc.getEventManager().addListener(descriptorEventListener);
            }

            if (SoftDelete.class.isAssignableFrom(desc.getJavaClass())) {
                desc.getQueryManager().setAdditionalCriteria("this.deleteTs is null");
            }

            List<DatabaseMapping> mappings = desc.getMappings();
            for (DatabaseMapping mapping : mappings) {
                // support UUID
                String attributeName = mapping.getAttributeName();
                MetaProperty metaProperty = metaClass.getPropertyNN(attributeName);
                if (metaProperty.getRange().isDatatype()) {
                    if (metaProperty.getJavaType().equals(UUID.class)) {
                        ((DirectToFieldMapping) mapping).setConverter(UuidConverter.getInstance());
                        setDatabaseFieldParameters(session, mapping.getField());
                    }
                } else if (metaProperty.getRange().isClass() && !metaProperty.getRange().getCardinality().isMany()) {
                    MetaClass refMetaClass = metaProperty.getRange().asClass();
                    MetaProperty refPkProperty = metadata.getTools().getPrimaryKeyProperty(refMetaClass);
                    if (refPkProperty != null && refPkProperty.getJavaType().equals(UUID.class)) {
                        for (DatabaseField field : ((OneToOneMapping) mapping).getForeignKeyFields()) {
                            setDatabaseFieldParameters(session, field);
                        }
                    }
                }
                // embedded attributes
                if (mapping instanceof AggregateObjectMapping) {
                    EmbeddedParameters embeddedParameters =
                            metaProperty.getAnnotatedElement().getAnnotation(EmbeddedParameters.class);
                    if (embeddedParameters != null && !embeddedParameters.nullAllowed())
                        ((AggregateObjectMapping) mapping).setIsNullAllowed(false);
                }
            }
        }
    }

    private void setDatabaseFieldParameters(Session session, DatabaseField field) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            field.setSqlType(Types.OTHER);
            field.setType(UUID.class);
        } else {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
        }
        field.setColumnDefinition("UUID");
    }
}
