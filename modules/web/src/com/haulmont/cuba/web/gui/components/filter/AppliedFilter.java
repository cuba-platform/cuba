/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.ComponentContainer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AppliedFilter {

    private FilterEntity filterEntity;
    private LinkedHashMap<String, String> params = new LinkedHashMap<>();

    protected Messages messages = AppBeans.get(Messages.NAME);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    public AppliedFilter(FilterEntity filterEntity, ComponentContainer container) {
        this.filterEntity = filterEntity;

        for (ParamEditor paramEditor : WebComponentsHelper.getComponents(container, ParamEditor.class)) {
            AbstractCondition<Param> condition = paramEditor.getCondition();
            Param param = condition.getParam();
            Object value = param.getValue();
            if (value != null) {
                params.put(condition.getLocCaption() + " " + condition.getOperationCaption(), formatParamValue(value));
            }
        }
    }

    public String getText() {
        String name = filterEntity.getName();
        if (StringUtils.isBlank(name)) {
            name = messages.getMainMessage(filterEntity.getCode());
        }
        StringBuilder sb = new StringBuilder(name);

        if (!params.isEmpty()) {
            sb.append(": ");
            for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                sb.append(entry.getKey()).append(" ").append(entry.getValue());
                if (it.hasNext())
                    sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String formatParamValue(Object value) {
        if (value == null)
            return "";

        if (value instanceof Instance)
            return ((Instance) value).getInstanceName();

        if (value instanceof Enum)
            return messages.getMessage((Enum) value);

        if (value instanceof ArrayList){
            ArrayList<String> names = new ArrayList<>();
            ArrayList list = ((ArrayList) value);
            for (Object obj : list) {
                if (obj instanceof Instance)
                    names.add(((Instance) obj).getInstanceName());
                else
                    names.add(String.valueOf(obj));
            }
            return names.toString();
        }

        Datatype datatype = Datatypes.get(value.getClass());
        if (datatype != null)
            return datatype.format(value, userSessionSource.getLocale());

        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppliedFilter that = (AppliedFilter) o;

        if (!filterEntity.equals(that.filterEntity)) return false;
        if (!params.equals(that.params)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filterEntity.hashCode();
        result = 31 * result + params.hashCode();
        return result;
    }
}