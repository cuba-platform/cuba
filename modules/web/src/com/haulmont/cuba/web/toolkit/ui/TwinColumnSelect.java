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

import com.haulmont.cuba.toolkit.gwt.client.ui.VTwinColumnSelect;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TwinColSelect;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
@ClientWidget(VTwinColumnSelect.class)
public class TwinColumnSelect extends TwinColSelect {

    private OptionStyleGenerator styleGenerator;

    private boolean nativeSelect;

    private boolean layoutCaption = true;

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
            String style = styleGenerator.generateStyle(this, id, selected);
            if (!StringUtils.isEmpty(style)) {
                target.addAttribute("style", style);
            }
        }
        target.endTag("so");
        return keyIndex;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("manageCaption", isLayoutCaption());
    }

    @Override
    protected String getComponentType() {
        return nativeSelect ? "nativetwincolumn" : "twincoluumn";
    }

    public OptionStyleGenerator getStyleGenerator() {
        return styleGenerator;
    }

    public void setStyleGenerator(OptionStyleGenerator styleGenerator) {
        this.styleGenerator = styleGenerator;
        requestRepaint();
    }

    public boolean isNativeSelect() {
        return nativeSelect;
    }

    public void setNativeSelect(boolean nativeSelect) {
        this.nativeSelect = nativeSelect;
        requestRepaint();
    }

    public boolean isLayoutCaption() {
        return layoutCaption;
    }

    public void setLayoutCaption(boolean layoutCaption) {
        this.layoutCaption = layoutCaption;
        requestRepaint();
    }

    public interface OptionStyleGenerator {
        String generateStyle(AbstractSelect source, Object itemId, boolean selected);
    }

}
