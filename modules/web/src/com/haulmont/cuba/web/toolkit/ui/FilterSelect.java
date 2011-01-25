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

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Select;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

@SuppressWarnings("serial")
public class FilterSelect extends Select {

    private boolean fixedTextBoxWidth = false;

    private int fetched;

    private static Log log = LogFactory.getLog(FilterSelect.class);

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (fixedTextBoxWidth) {
            target.addAttribute("fixedTextBoxWidth", true);
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
    }

    public void disablePaging() {
        this.pageLength = 200;
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

                int count = (currentPage + 1) * pageLength;

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
}
