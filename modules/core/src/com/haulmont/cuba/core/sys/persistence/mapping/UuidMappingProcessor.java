/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence.mapping;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.UuidConverter;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.platform.database.*;
import org.eclipse.persistence.sessions.Session;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.Types;
import java.util.UUID;

/**
 * Updates entity mappings to add UUID support even for databases that do not support UUID datatype directly.
 */
@Component("cuba_UuidMappingProcessor")
public class UuidMappingProcessor implements MappingProcessor {

    @Inject
    private Metadata metadata;

    @Override
    public void process(MappingProcessorContext context) {
        DatabaseMapping mapping = context.getMapping();
        Session session = context.getSession();

        MetaClass metaClass = metadata.getSession().getClassNN(mapping.getDescriptor().getJavaClass());

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

    }

    private void setDatabaseFieldParameters(Session session, DatabaseField field) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            field.setSqlType(Types.OTHER);
            field.setType(UUID.class);
            field.setColumnDefinition("UUID");
        } else if (session.getPlatform() instanceof MySQLPlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("varchar(32)");
        } else if (session.getPlatform() instanceof HSQLPlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("varchar(36)");
        } else if (session.getPlatform() instanceof SQLServerPlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("uniqueidentifier");
        } else if (session.getPlatform() instanceof OraclePlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("varchar2(32)");
        } else {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
        }
    }
}
