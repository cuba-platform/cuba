/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UuidProvider;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectCollectionMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.Session;

import java.sql.Types;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UuidConverter implements Converter {

    private final static UuidConverter INSTANCE = new UuidConverter();

    public static UuidConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            return objectValue;
        } else {
            return objectValue != null ? objectValue.toString() : null;
        }
    }

    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            return dataValue;
        } else {
            return dataValue instanceof String ? UuidProvider.fromString((String) dataValue) : dataValue;
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public void initialize(DatabaseMapping mapping, Session session) {
//        DatabaseField field;
//        if (mapping instanceof DirectCollectionMapping) {
//            field = ((DirectCollectionMapping) mapping).getDirectField();
//        } else {
//            field = mapping.getField();
//        }
//        setFieldParameters(session, field);
//
//        for (DatabaseMapping m : mapping.getDescriptor().getMappings()) {
//            assert OneToOneMapping.class.isAssignableFrom(ManyToOneMapping.class);
//            if (m instanceof OneToOneMapping) {
//                for (DatabaseField f : ((OneToOneMapping) m).getForeignKeyFields()) {
//                    setFieldParameters(session, f);
//                }
//            }
//        }
    }

//    private void setFieldParameters(Session session, DatabaseField field) {
//        if (session.getPlatform() instanceof PostgreSQLPlatform) {
//            field.setSqlType(Types.OTHER);
//            field.setType(UUID.class);
//        } else {
//            field.setSqlType(Types.VARCHAR);
//            field.setType(String.class);
//        }
//        field.setColumnDefinition("UUID");
//    }
}
