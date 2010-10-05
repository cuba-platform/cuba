package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Container;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ui.VFilterSelect;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Select;

import java.util.*;

/**
 * User: Nikolay Gorodnov
 * Date: 19.06.2009
 */
@SuppressWarnings("serial")
@ClientWidget(VFilterSelect.class)
public class FilterSelect extends Select {

    private boolean fixedTextBoxWidth = false;

    private int fetched;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (fixedTextBoxWidth) {
            target.addAttribute("fixedTextBoxWidth", true);
        }
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

            if (items instanceof Ordered) {
                filteredOptions = new LinkedList();

                int count = (currentPage + 1) * pageLength;

                Object itemId = ((Ordered) items).firstItemId();
                if (itemId != null) {
                    Object prevItemId = itemId;
                    filteredOptions.add(prevItemId);
                    for (int i = 0; i < count; i++) {
                        itemId = ((Ordered) items).nextItemId(prevItemId);
                        if (itemId == null)
                            break;
                        filteredOptions.add(itemId);
                        prevItemId = itemId;
                    }
                }
            } else
                filteredOptions = new LinkedList(getItemIds());

            fetched = Math.max(fetched, filteredOptions.size());
            return filteredOptions;
        }

        if (filterstring.equals(prevfilterstring)) {
            return filteredOptions;
        }

        Collection items;
        if (prevfilterstring != null
                && filterstring.startsWith(prevfilterstring)) {
            items = filteredOptions;
        } else {
            items = getItemIds();
        }
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
