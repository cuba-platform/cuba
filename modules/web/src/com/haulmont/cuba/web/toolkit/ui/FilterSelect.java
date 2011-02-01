/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 19.06.2009 18:13:08
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Select;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

@SuppressWarnings("serial")
public class FilterSelect extends Select {

    private boolean fixedTextBoxWidth = false;

    private boolean showOptionsDescriptions = false;

    private Map<Object, String> itemDescriptions;

    private Object itemDescriptionPropertyId;

    private int fetched;

    private static Log log = LogFactory.getLog(FilterSelect.class);

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (fixedTextBoxWidth) {
            target.addAttribute("fixedTextBoxWidth", true);
        }
        if (showOptionsDescriptions) {
            target.addAttribute("optionsDesc", true);
        }
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        setNullSelectionAllowed(!required);
    }

    public boolean isFixedTextBoxWidth() {
        return fixedTextBoxWidth;
    }

    public void setFixedTextBoxWidth(boolean fixedTextBoxWidth) {
        this.fixedTextBoxWidth = fixedTextBoxWidth;
        requestRepaint();
    }

    public boolean isShowOptionsDescriptions() {
        return showOptionsDescriptions;
    }

    public void setShowOptionsDescriptions(boolean showOptionsDescriptions) {
        this.showOptionsDescriptions = showOptionsDescriptions;
        requestRepaint();
    }

    public Object getItemDescriptionPropertyId() {
        return itemDescriptionPropertyId;
    }

    public void setItemDescriptionPropertyId(Object itemDescriptionPropertyId) {
        this.itemDescriptionPropertyId = itemDescriptionPropertyId;
        requestRepaint();
    }

    public void setItemDescription(Object itemId, String desc) {
        if (itemId == null) return;
        if (itemDescriptions == null) {
            itemDescriptions = new HashMap<Object, String>();
        }
        itemDescriptions.put(itemId, desc);
        requestRepaint();
    }

    public String getItemDescription(Object itemId) {
        if (itemId == null) return null;

        String desc = null;
        if (getItemDescriptionPropertyId() != null) {
            final Property p = getContainerProperty(itemId,
                    getItemDescriptionPropertyId());
            if (p != null) {
                desc = p.toString();
            }
        } else if (itemDescriptions != null) {
            desc = itemDescriptions.get(itemId);
        }

        return desc == null ? "" : desc;
    }

    public void disablePaging() {
        setPageLength(0);
    }

    @Override
    protected List getFilteredOptions() {
        if (filterstring == null || filterstring.equals("")
                || filteringMode == FILTERINGMODE_OFF) {
            prevfilterstring = null;

            log.trace("getFilteredOptions (container: " + items +  "): no filterstring");

            Object value = getValue();
            boolean valueFound = true;

            if (items instanceof Ordered) {
                log.trace("getFilteredOptions (container: " + items +  "): ordered collection, iterating through items for current page");
                filteredOptions = new LinkedList();
                valueFound = false;

                int count = (currentPage + 1) * getPageLength();

                Object itemId = ((Ordered) items).firstItemId();
                if (itemId != null) {
                    Object prevItemId = itemId;
                    filteredOptions.add(prevItemId);
                    if (value == null || value.equals(prevItemId))
                        valueFound = true;

                    for (int i = 0; i < count; i++) {
                        itemId = ((Ordered) items).nextItemId(prevItemId);
                        if (itemId == null)
                            break;
                        filteredOptions.add(itemId);
                        if (value == null || value.equals(itemId))
                            valueFound = true;
                        prevItemId = itemId;
                    }
                }
            } else {
                log.trace("getFilteredOptions (container: " + items +  "): loading all itemIds");
                filteredOptions = new LinkedList(getItemIds());
            }

            fetched = Math.max(fetched, filteredOptions.size());

            if (!valueFound && value != null) {
                log.trace("getFilteredOptions (container: " + items +  "): value is not in filteredOptions, adding it");
                filteredOptions.add(value);
            }
            
            return filteredOptions;
        }

        if (filterstring.equals(prevfilterstring)) {
            log.trace("getFilteredOptions (container: " + items +  "): same filterstring, returning previous filteredOptions");
            return filteredOptions;
        }

        log.trace("getFilteredOptions (container: " + items +  "): loading all itemIds and filtering");
        Collection items = getItemIds();
        prevfilterstring = filterstring;

        filteredOptions = new LinkedList();
        for (final Iterator it = items.iterator(); it.hasNext();) {
            final Object itemId = it.next();
            String caption = getItemCaption(itemId);
            if (caption == null || caption.equals("")) {
                continue;
            } else {
                caption = caption.toLowerCase();
            }
            switch (filteringMode) {
            case FILTERINGMODE_CONTAINS:
                if (caption.indexOf(filterstring) > -1) {
                    filteredOptions.add(itemId);
                }
                break;
            case FILTERINGMODE_STARTSWITH:
            default:
                if (caption.startsWith(filterstring)) {
                    filteredOptions.add(itemId);
                }
                break;
            }
        }

        fetched = Math.max(fetched, filteredOptions.size());
        return filteredOptions;
    }

    @Override
    public Collection getVisibleItemIds() {
        if (isVisible()) {
            if (items instanceof Ordered) {
                List list = new ArrayList();
                Object itemId = ((Ordered) items).firstItemId();
                if (itemId != null) {
                    Object prevItemId = itemId;
                    list.add(prevItemId);
                    for (int i = 0; i < fetched; i++) {
                        itemId = ((Ordered) items).nextItemId(prevItemId);
                        if (itemId == null)
                            break;
                        list.add(itemId);
                        prevItemId = itemId;
                    }
                }
                return list;
            } else
                return getItemIds();
        }
        return null;
    }

    @Override
    protected int getTotalMatchesSize() {
        if (filterstring == null || filterstring.equals("")
                || filteringMode == FILTERINGMODE_OFF) {
            return items.size();
        } else
            return filteredOptions.size();
    }

    @Override
    protected int paintOptions(
            PaintTarget target, String[] selectedKeys, int keyIndex,
            Object id, String key, String caption, Resource icon
    ) throws PaintException {
        target.startTag("so");
        if (icon != null) {
            target.addAttribute("icon", icon);
        }
        target.addAttribute("caption", caption);
        if (id != null && id.equals(getNullSelectionItemId())) {
            target.addAttribute("nullselection", true);
        }
        if (isShowOptionsDescriptions()) {
            target.addAttribute("desc", getItemDescription(id));
        }
        target.addAttribute("key", key);
        if (isSelected(id) && keyIndex < selectedKeys.length) {
            target.addAttribute("selected", true);
            selectedKeys[keyIndex++] = key;
        }
        target.endTag("so");
        return keyIndex;
    }
}
