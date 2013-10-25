/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.FieldEvents;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Select;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

@SuppressWarnings("serial")
public class FilterSelect extends Select implements Action.Container {

    private boolean fixedTextBoxWidth = false;

    private boolean showOptionsDescriptions = false;

    private Map<Object, String> itemDescriptions;

    private Object itemDescriptionPropertyId;

    protected int fetched;

    protected static Log log = LogFactory.getLog(FilterSelect.class);
    protected boolean isFirstChange = true;

    protected boolean textInputAllowed = true;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (actionManager != null) {
            actionManager.paintActions(null, target);
        }
        if (fixedTextBoxWidth) {
            target.addAttribute("fixedTextBoxWidth", true);
        }
        if (showOptionsDescriptions) {
            target.addAttribute("optionsDesc", true);
        }
        if (!textInputAllowed) {
            target.addAttribute("textInputAllowed", false);
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

    public void setTextInputAllowed(boolean textInputAllowed) {
        this.textInputAllowed = textInputAllowed;
        requestRepaint();
    }

    public boolean isTextInputAllowed() {
        return textInputAllowed;
    }

    @Override
    protected List getFilteredOptions() {
        if (filterstring == null || filterstring.equals("")
                || filteringMode == FILTERINGMODE_OFF) {
            prevfilterstring = null;

            log.trace("getFilteredOptions (container: " + items + "): no filterstring");

            Object value = getValue();
            boolean valueFound = true;

            if (!isFirstChange) {
                if (items instanceof Ordered) {
                    log.trace("getFilteredOptions (container: " + items + "): ordered collection, iterating through items for current page");
                    filteredOptions = new LinkedList<Object>();
                    valueFound = false;

                    int count = (currentPage + 1) * getPageLength();
                    count = Math.max(count, fetched);

                    Object itemId = ((Ordered) items).firstItemId();
                    if (itemId != null) {
                        Object prevItemId = itemId;
                        filteredOptions.add(prevItemId);
                        if (value == null || value.equals(prevItemId))
                            valueFound = true;

                        fetched = 0;
                        for (int i = 0; i < count; i++) {
                            itemId = ((Ordered) items).nextItemId(prevItemId);
                            if (itemId == null)
                                break;
                            filteredOptions.add(itemId);
                            if (value == null || value.equals(itemId))
                                valueFound = true;
                            prevItemId = itemId;
                            fetched++;
                        }
                    }
                } else {
                    log.trace("getFilteredOptions (container: " + items + "): loading all itemIds");
                    filteredOptions = new LinkedList<Object>(getItemIds());
                    fetched = filteredOptions.size();
                }
            } else {
                filteredOptions = new LinkedList<Object>();
                if (this.getNullSelectionItemId() != null)
                    filteredOptions.add(this.getNullSelectionItemId());
                valueFound = false;
            }

            if (!valueFound && value != null) {
                log.trace("getFilteredOptions (container: " + items + "): value is not in filteredOptions, adding it");
                if (filteredOptions.size() >= getPageLength())
                    ((LinkedList) filteredOptions).removeLast();
                filteredOptions.add(value);
            }

            return filteredOptions;
        }

        if (filterstring.equals(prevfilterstring)) {
            log.trace("getFilteredOptions (container: " + items + "): same filterstring, returning previous filteredOptions");
            return filteredOptions;
        }

        log.trace("getFilteredOptions (container: " + items + "): loading all itemIds and filtering");
        Collection items = getItemIds();
        prevfilterstring = filterstring;

        filteredOptions = new LinkedList<Object>();
        for (final Object itemId : items) {
            String caption = getItemCaption(itemId);
            if (caption == null || caption.equals("")) {
                continue;
            } else {
                caption = caption.toLowerCase();
            }
            switch (filteringMode) {
                case FILTERINGMODE_CONTAINS:
                    if (caption.contains(filterstring)) {
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
                List<Object> list = new ArrayList<Object>();
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

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        boolean valueAssigned = false;

        // Selection change
        if (variables.containsKey("selected")) {
            final String[] ka = (String[]) variables.get("selected");

            // Single select mode only
            if (ka.length == 0) {

                // Allows deselection only if the deselected item is visible
                final Object current = getValue();
                final Collection<?> visible = getVisibleItemIds();
                if (visible != null && visible.contains(current)) {
                    setValue(null, true);
                    valueAssigned = true;
                }
            } else {
                final Object id = itemIdMapper.get(ka[0]);
                if (id != null && id.equals(getNullSelectionItemId())) {
                    setValue(null, true);
                    valueAssigned = true;
                } else {
                    setValue(id, true);
                    valueAssigned = true;
                }
            }
        }

        String newFilter;
        if ((newFilter = (String) variables.get("filter")) != null) {
            // this is a filter request
            currentPage = (Integer) variables.get("page");
            filterstring = newFilter.toLowerCase();
            optionRepaint();
        } else if (isNewItemsAllowed()) {
            // New option entered (and it is allowed)
            final String newitem = (String) variables.get("newitem");
            if (newitem != null && newitem.length() > 0) {
                getNewItemHandler().addNewItem(newitem);
                // rebuild list
                filterstring = null;
                prevfilterstring = null;
            } else {
                if (!valueAssigned && !isReadOnly()) {
                    // revert value
                    if (isNullSelectionAllowed())
                        setValue(null, true);
                    else
                        optionRepaint();
                    return;
                }
            }
        }

        if (variables.containsKey(FieldEvents.FocusEvent.EVENT_ID)) {
            fireEvent(new FieldEvents.FocusEvent(this));
        }
        if (variables.containsKey(FieldEvents.BlurEvent.EVENT_ID)) {
            fireEvent(new FieldEvents.BlurEvent(this));
        }

        Object value = getValue();
        if (items instanceof Ordered && isFirstChange && variables.containsKey("page") && value != null) {
            Object itemId = ((Ordered) items).firstItemId();
            int count = 1;
            for (int i = 0; i < items.size(); i++) {
                if (itemId == null || value.equals(itemId))
                    break;
                itemId = ((Ordered) items).nextItemId(itemId);
                count++;
            }
            currentPage = Math.round(count / getPageLength());
        }
        isFirstChange = false;
        if (actionManager != null) {
            actionManager.handleActions(variables, this);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if (actionManager != null) {
            actionManager.setViewer(this);
        }
    }

    @Override
    protected ActionManager getActionManager() {
        if (actionManager == null) {
            actionManager = new ActionManager(this);
        }
        return actionManager;
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        getActionManager().removeActionHandler(actionHandler);
    }
}
