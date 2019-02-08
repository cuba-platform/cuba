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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.UuidConverter;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StudioEclipseLinkSessionEventListener extends SessionEventAdapter {
    @Override
    public void preLogin(SessionEvent event) {
        Session session = event.getSession();


        Map<Class, ClassDescriptor> descriptorMap = session.getDescriptors();
        for (Map.Entry<Class, ClassDescriptor> entry : descriptorMap.entrySet()) {
            ClassDescriptor desc = entry.getValue();

            if (SoftDelete.class.isAssignableFrom(desc.getJavaClass())) {
                desc.getQueryManager().setAdditionalCriteria("this.deleteTs is null");
                desc.setDeletePredicate(entity -> entity instanceof SoftDelete &&
                        PersistenceHelper.isLoaded(entity, "deleteTs") &&
                        ((SoftDelete) entity).isDeleted());
            }

            List<DatabaseMapping> mappings = desc.getMappings();
            Class entityClass = entry.getKey();

            for (DatabaseMapping mapping : mappings) {
                if (UUID.class.equals(getFieldType(entityClass, mapping.getAttributeName()))) {
                    ((DirectToFieldMapping) mapping).setConverter(UuidConverter.getInstance());
                    setDatabaseFieldParameters(session, mapping.getField());
                }
            }
        }
    }

    private Class<?> getFieldType(Class aClass, String name) {
        try {
            Field declaredField = aClass.getDeclaredField(name);
            return declaredField.getType();
        } catch (NoSuchFieldException e) {
            Class superclass = aClass.getSuperclass();
            if (superclass != null) {
                return getFieldType(superclass, name);
            }
            return null;
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
