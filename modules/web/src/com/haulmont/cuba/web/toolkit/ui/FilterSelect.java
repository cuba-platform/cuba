package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ui.VFilterSelect;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Select;

/**
 * User: Nikolay Gorodnov
 * Date: 19.06.2009
 */
@SuppressWarnings("serial")
@ClientWidget(VFilterSelect.class)
public class FilterSelect extends Select {

    private boolean fixedTextBoxWidth = false;

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
}
