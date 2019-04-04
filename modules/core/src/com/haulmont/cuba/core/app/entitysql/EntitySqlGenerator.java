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

package com.haulmont.cuba.core.app.entitysql;

import com.google.common.base.Preconditions;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;
import static java.lang.String.valueOf;

@Component(EntitySqlGenerator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EntitySqlGenerator {

    public static final String NAME = "cuba_EntitySqlGenerator";

    public static final String ID = "id";

    protected SimpleDateFormat dateTimeFormat = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''");
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("''yyyy-MM-dd''");
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("''HH:mm:ss''");
    protected String insertTemplate = "insert into %s \n(%s) \nvalues (%s);";
    protected String updateTemplate = "update %s \nset %s \nwhere %s%s;";
    protected String selectTemplate = "select %s from %s where %s";

    protected Class clazz;
    protected MetaClass metaClass;
    protected List<Table> tables = new LinkedList<>();
    protected String discriminatorValue;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    public EntitySqlGenerator(Class<? extends Entity> clazz) {
        this.clazz = clazz;
    }

    @PostConstruct
    public void init() {
        metaClass = metadata.getClass(clazz);
        collectTableMetadata(metaClass, new Table(null));

        if (tables.isEmpty()) {
            throw new IllegalStateException(
                    format("Could not generate scripts for class %s, because it's not linked with any database tables.", clazz));
        }
    }

    public String generateInsertScript(Entity entity) {
        Preconditions.checkArgument(entity.getClass().equals(clazz),
                format("Could not generate insert script for entity with class [%s]. This script generator is for class [%s]",
                        entity.getClass().getName(),
                        clazz.getClass()));

        StringBuilder result = new StringBuilder();
        for (Table table : tables) {
            result.append(table.insert(entity)).append("\n");
        }

        return result.toString();
    }

    public String generateUpdateScript(Entity entity) {
        Preconditions.checkArgument(entity.getClass().equals(clazz),
                format("Could not generate update script for entity with class [%s]. This script generator is for class [%s]",
                        entity.getClass().getName(),
                        clazz.getClass()));

        StringBuilder result = new StringBuilder();
        for (Table table : tables) {
            result.append(table.update(entity)).append("\n");
        }

        return result.toString();
    }

    public String generateSelectScript(Entity entity) {
        Preconditions.checkArgument(entity.getClass().equals(clazz),
                format("Could not generate select script for entity with class [%s]. This script generator is for class [%s]",
                        entity.getClass().getName(),
                        clazz.getClass()));

        List<String> columns = new ArrayList<>();
        List<String> tableNames = new ArrayList<>();
        List<String> where = new ArrayList<>();

        String tableAlias = null;
        FieldEntry tableIdColumn = null;
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            tableIdColumn = table.fieldToColumnMapping.get(ID);
            tableAlias = format("t%s", valueOf(i));
            String parentAlias = format("t%s", valueOf(i - 1));
            tableNames.add(table.name + " " + tableAlias);

            for (FieldEntry fieldEntry : table.fieldToColumnMapping.values()) {
                columns.addAll(convertFieldNames(tableAlias, fieldEntry));
            }

            if (table.parent != null) {
                FieldEntry parentIdColumn = table.parent.fieldToColumnMapping.get(ID);
                where.add(format("%s.%s = %s.%s", tableAlias, tableIdColumn.columnName, parentAlias, parentIdColumn.columnName));
            }
        }
        where.addAll(convertWhere(tableAlias, tableIdColumn, entity));

        return format(selectTemplate, convertList(columns), convertList(tableNames),
                convertList(where).replaceAll(",", " and "));
    }

    protected List<String> convertFieldNames(String tableAlias, FieldEntry fieldEntry) {
        List<String> columns = new ArrayList<>();
        if (fieldEntry.isEmbedded) {
            for (FieldEntry entry : fieldEntry.fieldsMapping.values()) {
                columns.addAll(convertFieldNames(tableAlias, entry));
            }
        } else {
            columns.add(tableAlias + "." + fieldEntry.columnName);
        }
        return columns;
    }

    protected List<String> convertWhere(String tableAlias, FieldEntry fieldEntry, Entity entity) {
        List<String> where = new ArrayList<>();
        if (fieldEntry.isEmbedded) {
            for (FieldEntry entry : fieldEntry.fieldsMapping.values()) {
                where.addAll(convertWhere(tableAlias, entry, entity));
            }
        } else {
            where.add(tableAlias + "." + fieldEntry.columnName + " = " +
                    convertValue(entity, fieldEntry.getFieldName(), entity.getValueEx(fieldEntry.getFieldName())));
        }
        return where;
    }

    protected String convertValue(Entity entity, String fieldName, @Nullable Object value) {
        try {
            String valueStr;
            if (value instanceof Entity) {
                value = ((Entity) value).getId();
            } else if (value instanceof EnumClass) {
                value = ((EnumClass) value).getId();
            } else if (value instanceof Enum) {
                value = BaseEntityInternalAccess.getValue(entity, fieldName);
            }

            value = persistence.getDbTypeConverter().getSqlObject(value);

            if (value == null) {
                valueStr = null;
            } else if (value instanceof Date) {
                MetaPropertyPath propertyPath = metaClass.getPropertyPath(fieldName);
                if (propertyPath != null) {
                    MetaProperty property = propertyPath.getMetaProperty();
                    Datatype datatype = property.getRange().asDatatype();

                    if (datatype.getJavaClass().equals(java.sql.Date.class)) {
                        valueStr = dateFormat.format((Date) value);
                    } else if (datatype.getJavaClass().equals(Time.class)) {
                        valueStr = timeFormat.format((Date) value);
                    } else {
                        valueStr = dateTimeFormat.format((Date) value);
                    }
                } else {
                    valueStr = dateTimeFormat.format((Date) value);
                }
            } else if (value instanceof String
                    || value instanceof UUID
                    || value.getClass().getName().toLowerCase().contains("uuid")
                    || value instanceof Character) {
                if (value instanceof String) {
                    value = ((String) value).replaceAll("\'", "''");
                }

                valueStr = format("'%s'", value);
            } else {
                valueStr = value.toString();
            }
            return valueStr;
        } catch (Exception e) {
            throw new RuntimeException(format("An error occurred while converting object [%s] for SQL query", value), e);
        }
    }

    protected String convertList(List<String> strings) {
        String string = strings.toString();
        return string.substring(1, string.length() - 1);
    }

    protected void collectTableMetadata(MetaClass metaClass, Table table) {
        Class<?> javaClass = metaClass.getJavaClass();
        javax.persistence.Table annotation = javaClass.getAnnotation(javax.persistence.Table.class);
        MetaClass ancestor = metaClass.getAncestor();

        if (annotation != null && StringUtils.isNotEmpty(annotation.name())) {
            if (table.name == null) {
                table.name = annotation.name();
                tables.add(0, table);
            } else {
                Table newTable = new Table(annotation.name());
                tables.add(0, newTable);
                table.parent = newTable;
                table = newTable;
            }
        }

        if (ancestor != null) {
            collectTableMetadata(ancestor, table);
        }
        table.collectMetadata(javaClass);
    }

    protected class Table {
        protected Table parent;
        protected String name;
        protected String idColumn;
        protected String discriminatorColumn;
        protected DiscriminatorType discriminatorType;
        protected Map<String, FieldEntry> fieldToColumnMapping = new LinkedHashMap<>();

        public Table(String name) {
            this.name = name;
        }

        public String insert(Entity entity) {
            List<String> columnNames = new ArrayList<>();
            List<String> valuesStr = new ArrayList<>();

            if (discriminatorColumn != null) {
                String discriminatorValueStr = convertValue(null, null, discriminatorValue());
                columnNames.add(discriminatorColumn);
                valuesStr.add(discriminatorValueStr);
            }

            for (Map.Entry<String, FieldEntry> entry : fieldToColumnMapping.entrySet()) {
                Pair<List<String>, List<String>> insertStrings = getInsertStrings(entry.getValue(), entity);
                columnNames.addAll(insertStrings.getFirst());
                valuesStr.addAll(insertStrings.getSecond());
            }
            return format(insertTemplate, name, convertList(columnNames), convertList(valuesStr));
        }

        protected Pair<List<String>, List<String>> getInsertStrings(FieldEntry fieldEntry, Entity entity) {
            List<String> columnNames = new ArrayList<>();
            List<String> valuesStr = new ArrayList<>();
            String fieldName = fieldEntry.getFieldName();
            if (fieldEntry.isEmbedded) {
                for (FieldEntry entry : fieldEntry.fieldsMapping.values()) {
                    Pair<List<String>, List<String>> insertStrings = getInsertStrings(entry, entity);
                    columnNames.addAll(insertStrings.getFirst());
                    valuesStr.addAll(insertStrings.getSecond());
                }
            } else {
                Object value = entity.getValueEx(fieldName);
                columnNames.add(fieldEntry.columnName);
                valuesStr.add(convertValue(entity, fieldName, value));
            }
            return new Pair<>(columnNames, valuesStr);
        }

        public String update(Entity entity) {
            List<String> valuesStr = new ArrayList<>();
            List<String> whereStr = new ArrayList<>();
            for (Map.Entry<String, FieldEntry> entry : fieldToColumnMapping.entrySet()) {
                Pair<List<String>, List<String>> insertStrings = getUpdateStrings(entry.getValue(), entity);
                valuesStr.addAll(insertStrings.getFirst());
                whereStr.addAll(insertStrings.getSecond());
            }
            return format(updateTemplate, name, convertList(valuesStr), "", convertList(whereStr).replaceAll(",", " and "));
        }

        protected Pair<List<String>, List<String>> getUpdateStrings(FieldEntry fieldEntry, Entity entity) {
            List<String> valuesStr = new ArrayList<>();
            List<String> whereStr = new ArrayList<>();
            String fieldName = fieldEntry.getFieldName();
            if (!fieldName.equalsIgnoreCase(ID)) {
                if (fieldEntry.isEmbedded) {
                    for (FieldEntry entry : fieldEntry.fieldsMapping.values()) {
                        Pair<List<String>, List<String>> updateStrings = getUpdateStrings(entry, entity);
                        valuesStr.addAll(updateStrings.getSecond());
                    }
                } else {
                    Object value = entity.getValueEx(fieldName);
                    valuesStr.add(format("%s=%s", fieldEntry.columnName, convertValue(entity, fieldName, value)));
                }
            } else {
                if (fieldEntry.isEmbedded) {
                    for (FieldEntry entry : fieldEntry.fieldsMapping.values()) {
                        Pair<List<String>, List<String>> updateStrings = getUpdateStrings(entry, entity);
                        whereStr.addAll(updateStrings.getFirst());
                    }
                } else {
                    Object value = entity.getValueEx(fieldName);
                    whereStr.add(format("%s=%s", fieldEntry.columnName, convertValue(entity, fieldName, value)));
                }
            }
            return new Pair<>(valuesStr, whereStr);
        }


        @Nullable
        protected Object discriminatorValue() {
            if (discriminatorValue == null) {
                return null;
            } else {
                if (discriminatorColumn != null && discriminatorType != null) {
                    switch (discriminatorType) {
                        case CHAR:
                            return discriminatorValue.charAt(0);
                        case INTEGER:
                            return Integer.valueOf(discriminatorValue);
                        case STRING:
                            return discriminatorValue;
                    }
                }
            }

            return null;
        }

        protected void collectMetadata(Class clazz) {
            if (clazz == null) return;
            PrimaryKeyJoinColumn primaryKey = (PrimaryKeyJoinColumn) clazz.getAnnotation(PrimaryKeyJoinColumn.class);
            if (primaryKey != null) {
                idColumn = primaryKey.name();
            } else {
                idColumn = resolveIdColumn();
            }
            fieldToColumnMapping.put(ID, new FieldEntry(ID, idColumn));

            DiscriminatorValue discriminatorValueAnnotation = (DiscriminatorValue) clazz.getAnnotation(DiscriminatorValue.class);
            Extends extendsAnnotation = (Extends) clazz.getAnnotation(Extends.class);
            javax.persistence.Entity entityAnnotation = (javax.persistence.Entity) clazz.getAnnotation(javax.persistence.Entity.class);
            DiscriminatorColumn discriminatorColumn = (DiscriminatorColumn) clazz.getAnnotation(DiscriminatorColumn.class);

            if (discriminatorValueAnnotation != null) {
                discriminatorValue = discriminatorValueAnnotation.value();
            } else if (extendsAnnotation != null && entityAnnotation != null) {
                discriminatorValue = entityAnnotation.name();
            } else if (entityAnnotation != null && discriminatorColumn != null
                    && discriminatorColumn.discriminatorType().equals(DiscriminatorType.STRING)) {
                discriminatorValue = entityAnnotation.name();
            } else if (entityAnnotation != null && primaryKey != null) {
                discriminatorValue = entityAnnotation.name();
            }

            if (discriminatorColumn != null) {
                this.discriminatorColumn = discriminatorColumn.name();
                this.discriminatorType = discriminatorColumn.discriminatorType();
            } else if (discriminatorValue != null && parent == null) {
                this.discriminatorColumn = "DTYPE";
                this.discriminatorType = DiscriminatorType.STRING;
            }

            fieldToColumnMapping.putAll(collectFields(clazz));
        }

        private String resolveIdColumn() {
            if (idColumn != null) {
                return idColumn;
            } else if (parent != null) {
                return parent.resolveIdColumn();
            }

            return ID.toUpperCase();
        }

        private Map<String, FieldEntry> collectFields(Class clazz) {
            Map<String, FieldEntry> result = new LinkedHashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                Embedded embedded = field.getAnnotation(Embedded.class);
                AttributeOverrides attributeOverrides = field.getAnnotation(AttributeOverrides.class);
                AssociationOverrides associationOverrides = field.getAnnotation(AssociationOverrides.class);
                Column columnAnnotation = field.getAnnotation(Column.class);
                JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
                EmbeddedId embeddedIdAnnotation = field.getAnnotation(EmbeddedId.class);

                if (embedded != null || embeddedIdAnnotation != null) {
                    Class<?> embeddedObjectType = field.getType();
                    Map<String, FieldEntry> embeddedFields = collectFields(embeddedObjectType);

                    if (attributeOverrides != null) {
                        overrideAttributes(attributeOverrides, embeddedFields);
                    }

                    if (associationOverrides != null) {
                        overrideAssociations(associationOverrides, embeddedFields);
                    }

                    result.put(field.getName(), new FieldEntry(field.getName(), embeddedFields));

                } else if (columnAnnotation != null) {
                    result.put(field.getName(), new FieldEntry(field.getName(), columnAnnotation.name()));
                } else if (joinColumnAnnotation != null) {
                    result.put(field.getName(), new FieldEntry(field.getName(), joinColumnAnnotation.name()));
                }
            }

            return result;
        }

        private void overrideAttributes(AttributeOverrides overrides, Map<String, FieldEntry> embeddedFields) {
            AttributeOverride[] overriddenAttributes = overrides.value();
            for (AttributeOverride overriddenAttribute : overriddenAttributes) {
                embeddedFields.put(overriddenAttribute.name(), new FieldEntry(overriddenAttribute.name(), overriddenAttribute.column().name()));
            }
        }

        private void overrideAssociations(AssociationOverrides overrides, Map<String, FieldEntry> embeddedFields) {
            AssociationOverride[] overriddenAttributes = overrides.value();
            for (AssociationOverride overriddenAttribute : overriddenAttributes) {
                if (overriddenAttribute.joinColumns().length == 1) {
                    embeddedFields.put(overriddenAttribute.name(), new FieldEntry(overriddenAttribute.name(), overriddenAttribute.joinColumns()[0].name()));
                }
            }
        }
    }

    protected static class FieldEntry {
        protected String fieldName;
        protected boolean isEmbedded;
        protected String columnName;
        protected Map<String, FieldEntry> fieldsMapping;
        protected FieldEntry parentField;

        FieldEntry(String fieldName, String columnName) {
            this.fieldName = fieldName;
            this.columnName = columnName;
            this.isEmbedded = false;
        }

        FieldEntry(String fieldName, Map<String, FieldEntry> fieldsMapping) {
            this.fieldName = fieldName;
            this.fieldsMapping = fieldsMapping;
            for (FieldEntry fieldEntry : this.fieldsMapping.values()) {
                fieldEntry.parentField = this;
            }
            this.isEmbedded = true;
        }

        protected String getFieldName() {
            if (parentField != null) {
                return parentField.getFieldName() + "." + fieldName;
            }
            return fieldName;
        }
    }
}