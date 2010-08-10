/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 10.08.2010 15:49:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.TwinColSelect;

@SuppressWarnings("serial")
public class TwinColumnSelect extends TwinColSelect {

    private OptionStyleGenerator styleGenerator;

    @Override
    protected int paintOptions(PaintTarget target, String[] selectedKeys, int keyIndex, Object id, String key, String caption, Resource icon) throws PaintException {
        target.startTag("so");
        if (icon != null) {
            target.addAttribute("icon", icon);
        }
        target.addAttribute("caption", caption);
        if (id != null && id.equals(getNullSelectionItemId())) {
            target.addAttribute("nullselection", true);
        }
        target.addAttribute("key", key);
        final boolean selected = isSelected(id) && keyIndex < selectedKeys.length;
        if (selected) {
            target.addAttribute("selected", true);
            selectedKeys[keyIndex++] = key;
        }
        if (styleGenerator != null) {
            target.addAttribute("style", styleGenerator.generateStyle(this, id, selected));
        }
        target.endTag("so");
        return keyIndex;
    }

    public OptionStyleGenerator getStyleGenerator() {
        return styleGenerator;
    }

    public void setStyleGenerator(OptionStyleGenerator styleGenerator) {
        this.styleGenerator = styleGenerator;
        requestRepaint();
    }

    public interface OptionStyleGenerator {
        String generateStyle(AbstractSelect source, Object itemId, boolean selected);
    }

}
