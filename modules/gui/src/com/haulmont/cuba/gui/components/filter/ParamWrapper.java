/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.global.QueryUtils;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.core.global.filter.ParametersHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.components.filter.condition.DynamicAttributesCondition;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import com.haulmont.cuba.gui.data.ValueListener;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class ParamWrapper implements Component.HasValue {

    public static final Pattern LIKE_PATTERN = Pattern.compile("\\slike\\s+" + ParametersHelper.QUERY_PARAMETERS_RE);

    protected final AbstractCondition condition;
    protected final Param param;

    protected Component parent;

    public ParamWrapper(AbstractCondition condition, Param param) {
        this.condition = condition;
        this.param = param;
    }

    @Override
    public <T> T getValue() {
        Object value = param.getValue();
        if (value instanceof String
                && !StringUtils.isEmpty((String) value)
                && !((String) value).startsWith(ParametersHelper.CASE_INSENSITIVE_MARKER)) {
            // try to wrap value for case-insensitive "like" search
            if (condition instanceof PropertyCondition || condition instanceof DynamicAttributesCondition) {
                Op op = condition.getOperator();
                if (Op.CONTAINS.equals(op) || op.equals(Op.DOES_NOT_CONTAIN)) {
                    value = wrapValueForLike(QueryUtils.escapeForLike(value.toString()));
                } else if (Op.STARTS_WITH.equals(op)) {
                    value = wrapValueForLike(QueryUtils.escapeForLike(value.toString()), false, true);
                } else if (Op.ENDS_WITH.equals(op)) {
                    value = wrapValueForLike(QueryUtils.escapeForLike(value.toString()), true, false);
                }
            }
        } else if (condition instanceof CustomCondition) {
            String where = ((CustomCondition) condition).getWhere();
            Op op = condition.getOperator();
            Matcher matcher = LIKE_PATTERN.matcher(where);
            if (matcher.find()) {
                if (Op.STARTS_WITH.equals(op)) {
                    value = wrapValueForLike(QueryUtils.escapeForLike(value.toString()), false, true);
                } else if (Op.ENDS_WITH.equals(op)) {
                    value = wrapValueForLike(QueryUtils.escapeForLike(value.toString()), true, false);
                } else {
                    value = wrapValueForLike(QueryUtils.escapeForLike(value.toString()));
                }
            }
        } else if (value instanceof EnumClass) {
            value = ((EnumClass) value).getId();
        }
        return (T) value;
    }

    protected String wrapValueForLike(Object value) {
        return wrapValueForLike(value, true, true);
    }

    protected String wrapValueForLike(Object value, boolean before, boolean after) {
        return ParametersHelper.CASE_INSENSITIVE_MARKER + (before ? "%" : "") + value + (after ? "%" : "");
    }

    @Override
    public void setValue(Object value) {
        param.setValue(value);
    }

    @Override
    public void addListener(ValueListener listener) {
        addValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void removeListener(ValueListener listener) {
        removeValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        param.addValueChangeListener(new ParamValueChangeListenerWrapper(this, listener));
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        param.removeValueChangeListener(new ParamValueChangeListenerWrapper(this, listener));
    }

    protected static class ParamValueChangeListenerWrapper implements Param.ParamValueChangeListener {

        protected final ParamWrapper paramWrapper;
        protected final ValueChangeListener valueChangeListener;

        public ParamValueChangeListenerWrapper(ParamWrapper paramWrapper, ValueChangeListener valueChangeListener) {
            this.paramWrapper = paramWrapper;
            this.valueChangeListener = valueChangeListener;
        }

        @Override
        public void valueChanged(@Nullable Object prevValue, @Nullable Object value) {
            valueChangeListener.valueChanged(new ValueChangeEvent(paramWrapper, prevValue, value));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != getClass()) {
                return false;
            }

            ParamValueChangeListenerWrapper that = (ParamValueChangeListenerWrapper) obj;

            return this.valueChangeListener.equals(that.valueChangeListener);
        }

        @Override
        public int hashCode() {
            return valueChangeListener.hashCode();
        }
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    @Override
    public String getId() {
        return param.getName();
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public Component getParent() {
        return parent;
    }

    @Override
    public void setParent(Component parent) {
        this.parent = parent;
    }

    @Override
    public String getDebugId() {
        return null;
    }

    @Override
    public void setDebugId(String id) {
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public int getHeightUnits() {
        return 0;
    }

    @Override
    public void setHeight(String height) {
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public int getWidthUnits() {
        return 0;
    }

    @Override
    public void setWidth(String width) {
    }

    @Override
    public Alignment getAlignment() {
        return Alignment.TOP_LEFT;
    }

    @Override
    public void setAlignment(Alignment alignment) {
    }

    @Override
    public String getStyleName() {
        return null;
    }

    @Override
    public void setStyleName(String name) {
    }

    @Override
    public Frame getFrame() {
        return null;
    }

    @Override
    public void setFrame(Frame frame) {
    }
}