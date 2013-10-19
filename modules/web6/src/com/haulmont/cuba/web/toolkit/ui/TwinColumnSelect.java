/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VTwinColumnSelect;
import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TwinColSelect;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@ClientWidget(VTwinColumnSelect.class)
public class TwinColumnSelect extends TwinColSelect {

    private OptionStyleGenerator styleGenerator;

    private boolean showOptionsDescriptions = false;

    private Map<Object, String> itemDescriptions;

    private Object itemDescriptionPropertyId;

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
        if (isShowOptionsDescriptions()) {
            target.addAttribute("desc", getItemDescription(id));
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
        if (showOptionsDescriptions) {
            target.addAttribute("optionsDesc", true);
        }
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

    public interface OptionStyleGenerator {
        String generateStyle(AbstractSelect source, Object itemId, boolean selected);
    }

}
