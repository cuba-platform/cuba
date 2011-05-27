/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.http.api.template;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Author: Alexander Chevelev
 * Date: 26.05.2011
 * Time: 0:11:34
 */
public class MetaClassRepresentation {
    private MetaClass meta;
    private List<View> views;

    public MetaClassRepresentation(MetaClass meta, List<View> views) {
        this.meta = meta;
        this.views = views;
    }

    public String getName() {
        return meta.getName();
    }

    public String getParent() {
        return meta.getAncestor() == null ? "" : "Parent is " + asHref(meta.getAncestor());
    }

    public String getDescription() {
        String result = MessageUtils.getEntityCaption(meta);
        return result == null ? "" : result;
    }

    public Collection<MetaClassRepProperty> getProperties() {
        List<MetaClassRepProperty> result = new ArrayList<MetaClassRepProperty>();
        for (MetaProperty property : meta.getProperties()) {
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

        public String getName() {
            return property.getName();
        }

        public String getDescription() {
            String result = MessageUtils.getPropertyCaption(property);
            return result == null ? "" : result;
        }

        public String getEnum() {
            return property.getRange().isEnum() ? "(enum)" : "";
        }

        public String getJavaType() {
            String type = property.getJavaType().getName();
            String simpleName = property.getJavaType().getSimpleName();
            return type.startsWith("java.lang.") && ("java.lang.".length() + simpleName.length() == type.length()) ?
                    simpleName :
                    property.getRange().isClass() ?
                            asHref(property.getRange().asClass()) :
                            type;
        }

        public String getCardinality() {
            switch (property.getRange().getCardinality()) {
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

        public String getOrdered() {
            return property.getRange().isOrdered() ? "Ordered" : "";
        }

        public String getMandatory() {
            return property.isMandatory() ? "Mandatory" : "Optional";
        }

        public String getReadOnly() {
            return property.isReadOnly() ? "Read Only" : "Read/Write";
        }

        public Collection<String> getAnnotations() {
            Collection<String> result = new ArrayList<String>();
            Map<String, Object> map = property.getAnnotations();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String annotationName = entry.getKey();
                if ("length".equals(annotationName) && !String.class.equals(property.getJavaType()))
                    continue;

                result.add(annotationName + ": " + entry.getValue());
            }
            return result;
        }

    }

    public Collection<MetaClassRepView> getViews() {
        if (views == null)
            return null;

        Collection<MetaClassRepView> result = new ArrayList<MetaClassRepView>();
        for (View view : views) {
            result.add(new MetaClassRepView(view));
        }
        return result;
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
            for (ViewProperty property : view.getProperties()) {
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

    private static String asHref(MetaClass metaClass) {
        return "<a href=\"#" + metaClass.getName() + "\">" + metaClass.getName() + "</a>";
    }
}
