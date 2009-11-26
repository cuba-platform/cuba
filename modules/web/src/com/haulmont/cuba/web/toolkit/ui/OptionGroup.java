/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 24.11.2009 16:31:35
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.PaintException;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

@SuppressWarnings("serial")
public class OptionGroup extends com.vaadin.ui.OptionGroup {
    protected Set<Object> disabledOptions = null;

    public void setOptionDisabled(Object id, boolean disabled) {
        if (disabled) {
            if (disabledOptions == null) {
                disabledOptions = new HashSet<Object>();
            }
            disabledOptions.add(id);
        } else {
            if (disabledOptions == null) return;
            disabledOptions.remove(id);
            if (disabledOptions.isEmpty()) {
                disabledOptions = null;
            }
        }
    }

    public boolean isOptionDisabled(Object id) {
        return disabledOptions != null && disabledOptions.contains(id);
    }

    public Set<Object> getDisabledOptions() {
        if (disabledOptions != null) {
            return Collections.unmodifiableSet(disabledOptions);
        } else {
            return Collections.emptySet();
        }
    }

    protected int paintOption(PaintTarget target, String[] selectedKeys,
                              int keyIndex, Object id, String key, String caption, Resource icon
    ) throws PaintException {
        target.startTag("so");
        if (icon != null) {
            target.addAttribute("icon", icon);
        }
        target.addAttribute("caption", caption);
        if (id != null && id.equals(getNullSelectionItemId())) {
            target.addAttribute("nullselection", true);
        }
        target.addAttribute("key", key);
        if (isSelected(id) && keyIndex < selectedKeys.length) {
            target.addAttribute("selected", true);
            selectedKeys[keyIndex++] = key;
        }
        if (isOptionDisabled(id)) {
            target.addAttribute("disabled", true);
        }
        target.endTag("so");
        return keyIndex;
    }
}
