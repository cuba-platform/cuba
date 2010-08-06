/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 16:55:25
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.web.toolkit.ui.OptionGroup;
import com.haulmont.bali.util.Dom4j;
import com.vaadin.data.Property;

import java.util.*;

import org.dom4j.Element;

public class WebOptionsGroup
        extends
            WebAbstractOptionsField<OptionGroup>
        implements
            OptionsGroup, Component.Wrapper, Component.HasSettings
{
    public WebOptionsGroup() {
        component = new OptionGroup() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }
        };
        attachListener(component);
        component.setImmediate(true);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T getValue() {
        if (optionsDatasource != null) {
            final Object key = super.getValue();
            return (T) getValueFromKey(key);
        } else {
            return (T) wrapAsCollection(super.getValue());
        }
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T getValueFromKey(Object key) {
        if (key instanceof Collection) {
            final Set<Object> set = new HashSet<Object>();
            for (Object o : (Collection) key) {
                Object t = getValue(o);
                set.add(t);
            }
            return (T) set;
        } else {
            final Object o = getValue(key);
            return (T) wrapAsCollection(o);
        }
    }

    protected <T> Object getValue(Object o) {
        Object t;
        if (o instanceof Enum) {
            t = o;
        } else if (o instanceof Entity) {
            t = o;
        } else {
            t = optionsDatasource.getItem(o);
        }
        return t;
    }

    @Override
    public void setValue(Object value) {
        // TODO (abramov) need to be changed
        super.setValue(getKeyFromValue(value));
    }

    protected Object getKeyFromValue(Object value) {
        Object v;
        if (isMultiSelect()) {
            if (value instanceof Collection) {
                final Set<Object> set = new HashSet<Object>();
                for (Object o : (Collection) value) {
                    Object t = getKey(o);
                    set.add(t);
                }
                v = set;
            } else {
                v = getKey(value);
            }
        } else {
            v = getKey(value);
        }

        return v;
    }

    protected Object getKey(Object o) {
        Object t;
        if (o instanceof Entity) {
            t = ((Entity) o).getId();
        } else if (o instanceof Enum) {
            t = o;
        } else {
            t = o;
        }
        return t;
    }

    public void applySettings(Element element) {
        if (!CaptionMode.ITEM.equals(this.captionMode)) {
            //TODO develop settings for DS optionGroup
            return;
        }
        final Element allOptionsElement = element.element("allOptions");
        final List optionsList = getOptionsList();
        if (allOptionsElement != null && optionsList != null) {
            List newOptionList = new ArrayList();
            List selectedOptionList = new ArrayList();
            final Element sortedOptionsElement = allOptionsElement.element("sortedOptions");
            final Element selectedOptionsElement = allOptionsElement.element("selectedOptions");

            for (Element e : Dom4j.elements(sortedOptionsElement, "option")) {
                for (Object o : optionsList) {
                    if (o.toString().equals(e.attributeValue("id"))) {
                        newOptionList.add(o);
                        break;
                    }
                }
            }

            for (Element e : Dom4j.elements(selectedOptionsElement, "option")) {
                for (Object o : optionsList) {
                    if (o.toString().equals(e.attributeValue("id"))) {
                        selectedOptionList.add(o);
                        break;
                    }
                }
            }
            for (Object o : optionsList) {
                if (!newOptionList.contains(o)) {
                    newOptionList.add(o);
                }
            }

            setOptionsList(newOptionList);
//            setValue(selectedOptionList);  todo: [degtyarjov] was commented to provide some functionality on control screen 
        }
    }

    public boolean saveSettings(Element element) {
        if (!CaptionMode.ITEM.equals(this.captionMode)) {
            //TODO develop settings for DS optionGroup
            return true;
        }
        Element allOptionsElement = element.element("allOptions");
        if (allOptionsElement != null) {
            element.remove(allOptionsElement);
        }
        allOptionsElement = element.addElement("allOptions");
        Element sortedOptionsElement = allOptionsElement.addElement("sortedOptions");
        Element selectedOptionsElement = allOptionsElement.addElement("selectedOptions");
        final List optionsList = getOptionsList();
        if (optionsList != null) {
            for (Object o : optionsList) {
                Element option = sortedOptionsElement.addElement("option");
                option.addAttribute("id", o.toString());
            }
        }

        if (isMultiSelect()) {
            final Set<Set> selectedSet = getValue();
            if (selectedSet != null) {
                for (Set set : selectedSet) {
                    for (Object o : set) {
                        Element option = selectedOptionsElement.addElement("option");
                        option.addAttribute("id", o.toString());
                    }
                }
            }
        } else {
            final Object value = getValue();
            if (value != null) {
                Element option = selectedOptionsElement.addElement("option");
                option.addAttribute("id", value.toString());
            }
        }
        return true;
    }
}
