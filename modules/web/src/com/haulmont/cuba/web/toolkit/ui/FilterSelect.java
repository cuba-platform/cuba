package com.haulmont.cuba.web.toolkit.ui;

import com.itmill.toolkit.ui.Select;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.terminal.PaintException;

/**
 * User: Nikolay Gorodnov
 * Date: 19.06.2009
 */
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
