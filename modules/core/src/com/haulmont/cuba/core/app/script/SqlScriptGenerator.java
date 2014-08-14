/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.script;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.persistence.PostgresUUID;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(SqlScriptGenerator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SqlScriptGenerator {
    public static final String NAME = "cuba_SqlScriptGenerator";

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''");
    protected String insertTemplate = "insert into %s \n(%s) \nvalues (%s);";
    protected String updateTemplate = "update %s \nset %s \nwhere id=%s;";
    protected Class clazz;
    protected List<String> columnNames = new ArrayList<>();
    protected List<String> fieldNames = new ArrayList<>();
    protected String tableName;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    public SqlScriptGenerator(Class<? extends Entity> clazz) {
        this.clazz = clazz;
    }

    @PostConstruct
    public void init(){
        this.tableName = metadata.getTools().getDatabaseTable(metadata.getClass(clazz));
        Preconditions.checkNotNull(tableName,
                format("Could not generate script for class [%s], because it's not linked with DB table using @Table annotation", clazz));
        collectMetadata(clazz);
    }

    public String generateInsertScript(Entity entity) {
        Preconditions.checkArgument(entity.getClass().equals(clazz),
                format("Could not generate insert script for entity with class [%s]. This script generator is for class [%s]",
                        entity.getClass().getName(),
                        clazz.getClass()));

        List<String> valuesStr = new ArrayList<>();
        for (String fieldName : fieldNames) {
            Object value = entity.getValue(fieldName);
            valuesStr.add(convertValue(value));
        }

        String result = format(insertTemplate, tableName, convertList(columnNames), convertList(valuesStr));
        return result;
    }

    //todo: do we only need loaded fields for detached?
    public String generateUpdateScript(Entity entity) {
        Preconditions.checkArgument(entity.getClass().equals(clazz),
                format("Could not generate insert script for entity with class [%s]. This script generator is for class [%s]",
                        entity.getClass().getName(),
                        clazz.getClass()));

        List<String> valuesStr = new ArrayList<>();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String columnName = columnNames.get(i);

            if (!fieldName.equalsIgnoreCase("id")) {
                Object value = entity.getValue(fieldName);
                valuesStr.add(format("%s=%s", columnName, convertValue(value)));
            }
        }

        String result = format(updateTemplate, tableName, convertList(valuesStr), convertValue(entity.getUuid()));
        return result;
    }

    protected String convertValue(Object value) {
        try {
            String valueStr;
            if (value instanceof Entity) {
                value = ((Entity) value).getId();
            } else if (value instanceof EnumClass) {
                value = ((EnumClass) value).getId();
            }

            value = persistence.getDbTypeConverter().getSqlObject(value);

            if (value == null) {
                valueStr = null;
            } else if (value instanceof Date) {
                valueStr = dateFormat.format((Date) value);
            } else if (value instanceof String
                    || value instanceof UUID
                    || value instanceof PostgresUUID) {
                valueStr = format("'%s'", value);
            } else {
                valueStr = value.toString();
            }
            return valueStr;
        } catch (SQLException e) {
            throw new RuntimeException(format("An error occurred while converting object [%s] for SQL query", value), e);
        }
    }

    protected String convertList(List<String> strings) {
        String string = strings.toString();
        return string.substring(1, string.length() - 1);
    }

    protected void collectMetadata(Class clazz) {
        if (clazz == null) return;

        if (!clazz.getSuperclass().equals(Object.class)) {
            collectMetadata(clazz.getSuperclass());
        }

        for (Field field : clazz.getDeclaredFields()) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);

            if (columnAnnotation != null) {
                columnNames.add(columnAnnotation.name());
                fieldNames.add(field.getName());
            } else if (joinColumnAnnotation != null) {
                columnNames.add(joinColumnAnnotation.name());
                fieldNames.add(field.getName());
            }
        }
    }
}
