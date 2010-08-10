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

import java.util.Map;

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
        if (isSelected(id) && keyIndex < selectedKeys.length) {
            target.addAttribute("selected", true);
            selectedKeys[keyIndex++] = key;
        }
        if (styleGenerator != null) {
            target.addAttribute("styles", styleGenerator.generateStyles(this, id));
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
        Map<String, String> generateStyles(AbstractSelect source, Object itemId);
    }

}
