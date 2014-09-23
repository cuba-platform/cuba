/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.domain;

import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import freemarker.template.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import java.util.*;

/**
 * @author chevelev
 * @version $Id$
 */
public class MetaClassRepresentation {
    private MetaClass meta;
    private List<View> views;

    public MetaClassRepresentation(MetaClass meta, List<View> views) {
        this.meta = meta;
        this.views = views;
        getTableName();
    }

    public String getTableName() {
        boolean isEmbeddable = meta.getJavaClass().isAnnotationPresent(Embeddable.class);
        if (isEmbeddable)
            return "not defined for embeddable entities";

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

        String databaseTable = metadataTools.getDatabaseTable(meta);
        return databaseTable != null ? databaseTable : "not defined";
    }

    public String getName() {
        return meta.getName();
    }

    public String getParent() {
        MetaClass ancestor = meta.getAncestor();

        if (ancestor == null || !ancestor.getName().contains("$") ||
                ancestor.getJavaClass().isAnnotationPresent(MappedSuperclass.class))
            return "";

        if (!readPermitted(ancestor)) {
            return null;
        }

        return "Parent is " + asHref(ancestor.getName());
    }

    public String getDescription() {
        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        String result = messageTools.getEntityCaption(meta);
        return result == null ? "" : result;
    }

    public Collection<MetaClassRepProperty> getProperties() {
        List<MetaClassRepProperty> result = new ArrayList<>();
        for (MetaProperty property : meta.getProperties()) {
            MetaProperty.Type propertyType = property.getType();
            //don't show property if user don't have permissions to view it
            if (!attrViewPermitted(meta, property.getName())) {
                continue;
            }

            //don't show property if it's reference and user
            //don't have permissions to view it's entity class
            if (propertyType == MetaProperty.Type.COMPOSITION
                    || propertyType == MetaProperty.Type.ASSOCIATION) {
                MetaClass propertyMetaClass = propertyMetaClass(property);
                if (!readPermitted(propertyMetaClass))
                    continue;
            }
            MetaClassRepProperty prop = new MetaClassRepProperty(property);
            result.add(prop);
        }
        return result;
    }

    public static class MetaClassRepProperty {
        private MetaProperty property;
        public MetaClassRepProperty(MetaProperty property) {
            this.property = property;
        }

        public String getTableName() {
            Column column = property.getAnnotatedElement().getAnnotation(Column.class);
            if (column != null)
                return column.name();

            JoinColumn joinColumn = property.getAnnotatedElement().getAnnotation(JoinColumn.class);
            return joinColumn != null ? joinColumn.name() : "";
        }

        public String getName() {
            return property.getName();
        }

        public String getDescription() {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            String result = messageTools.getPropertyCaption(property);
            return result == null ? "" : result;
        }

        public String getEnum() {
            return property.getRange().isEnum() ? asHref(property.getRange().asEnumeration().toString()) : null;
        }

        /**
         * @return map representing Enumeration with localized Enumeration item values
         */
        public TemplateHashModel getEnumValues() {
            if (property.getRange().isEnum()) {

                Enumeration<?> enumeration = property.getRange().asEnumeration();
                SimpleHash wrappedEnum = new SimpleHash();
                wrappedEnum.put("name", enumeration.toString());
                SimpleSequence values = new SimpleSequence();

                for (Enum enumItem : enumeration.getValues()) {
                    SimpleHash wrappedEnumElement = new SimpleHash();
                    try {
                        wrappedEnumElement.put("idObj", ObjectWrapper.BEANS_WRAPPER.wrap(enumItem)); //Some enums don't implement EnumClass interface so we'll get id field here via reflection.
                        Messages messages = AppBeans.get(Messages.NAME);
                        wrappedEnumElement.put("name", messages.getMessage(enumItem));
                        values.add(wrappedEnumElement);
                    } catch (TemplateModelException e) {
                        throw new RuntimeException(e);
                    }
                }
                wrappedEnum.put("values", values);

                return wrappedEnum;
            }
            return null;
        }

        public String getJavaType() {
            String type = property.getJavaType().getName();
            String simpleName = property.getJavaType().getSimpleName();
            return type.startsWith("java.lang.") && ("java.lang.".length() + simpleName.length() == type.length()) || type.startsWith("[")?
                    simpleName :
                    property.getRange().isClass() ?
                            asHref(property.getRange().asClass().getName()) :
                            type;
        }

        public String getCardinality() {
            switch (property.getRange().getCardinality()) {
                case NONE:
                    return "";
                case ONE_TO_ONE:
                    return property.getRange().isClass() ? "1:1" : "";
                case ONE_TO_MANY:
                    return "1:N";
                case MANY_TO_ONE:
                    return "N:1";
                case MANY_TO_MANY:
                    return "N:N";
                default:
                    return property.getRange().getCardinality().toString();
            }
        }

        public Collection<String> getAnnotations() {
            Collection<String> result = new ArrayList<>();
            Map<String, Object> map = property.getAnnotations();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String annotationName = entry.getKey();
                if (("length".equals(annotationName) && !String.class.equals(property.getJavaType())) || "persistent".equals(annotationName))
                    continue;

                result.add(annotationName + ": " + entry.getValue());
            }
            return result;
        }

        public boolean isPersistent() {
            MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
            return metadataTools.isPersistent(property);
        }
    }

    public Collection<MetaClassRepView> getViews() {
        if (views == null)
            return null;

        Collection<MetaClassRepView> result = new ArrayList<>();
        for (View view : views) {
            if (!viewAccessPermitted(view))
                continue;
            result.add(new MetaClassRepView(view));
        }
        return result;
    }

    private static boolean viewAccessPermitted(View view) {
        Class clazz = view.getEntityClass();
        MetaClass meta = getMetaClass(clazz);
        return MetaClassRepresentation.readPermitted(meta);
    }

    private static MetaClass getMetaClass(Class clazz) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getExtendedEntities().getEffectiveMetaClass(metadata.getClassNN(clazz));
    }

    private static boolean viewPropertyReadPermitted(MetaClass meta, ViewProperty viewProperty) {
        if (!attrViewPermitted(meta, viewProperty.getName()))
            return false;

        MetaProperty metaProperty = meta.getPropertyNN(viewProperty.getName());
        if (metaProperty.getType() == MetaProperty.Type.DATATYPE
                || metaProperty.getType() == MetaProperty.Type.ENUM)
            return true;

        MetaClass propertyMeta = metaProperty.getRange().asClass();
        return readPermitted(propertyMeta);
    }

    public static class MetaClassRepView {
        private View view;

        public MetaClassRepView(View view) {
            this.view = view;
        }

        public String getName() {
            return view.getName();
        }

        public Collection<MetaClassRepViewProperty> getProperties() {
            Collection<MetaClassRepViewProperty> result = new ArrayList<MetaClassRepViewProperty>();
            MetaClass meta = getMetaClass(view.getEntityClass());
            for (ViewProperty property : view.getProperties()) {
                if (!MetaClassRepresentation.viewPropertyReadPermitted(meta, property))
                    continue;
                result.add(new MetaClassRepViewProperty(property));
            }
            return result;
        }
    }

    public static class MetaClassRepViewProperty {
        private ViewProperty property;

        public MetaClassRepViewProperty(ViewProperty property) {
            this.property = property;
        }

        public String getName() {
            return property.getName();
        }

        public String getLazy() {
            return property.isLazy() ? "LAZY" : "";
        }

        public MetaClassRepView getView() {
            return property.getView() == null ? null : new MetaClassRepView(property.getView());
        }
    }

    private MetaClass propertyMetaClass(MetaProperty property) {
        return property.getRange().asClass();
    }

    private static boolean attrViewPermitted(MetaClass metaClass, String property) {
        return attrPermitted(metaClass, property, EntityAttrAccess.VIEW);
    }

    private static boolean attrPermitted(MetaClass metaClass, String property, EntityAttrAccess entityAttrAccess) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityAttrPermitted(metaClass, property, entityAttrAccess);
    }

    private static boolean readPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.READ);
    }

    private static boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(metaClass, entityOp);
    }

    private static String asHref(String element) {
        return "<a href=\"#" + element + "\">" + element + "</a>";
    }
}