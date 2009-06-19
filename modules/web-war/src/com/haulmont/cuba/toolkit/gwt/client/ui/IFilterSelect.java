package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.Util;

/**
 * User: Nikolay Gorodnov
 * Date: 19.06.2009
 */
public class IFilterSelect extends
        com.itmill.toolkit.terminal.gwt.client.ui.IFilterSelect
{
    protected boolean fixedTextBoxWidth = false;

    @Override
    protected void updateAttributes(UIDL uidl) {
        if (uidl.hasAttribute("fixedTextBoxWidth")) {
            fixedTextBoxWidth = true;
        }
    }

    @Override
    protected void updateRootWidth() {
        if (width == null) {
            /*
             * When the width is not specified we must specify width for root
             * div so the popupopener won't wrap to the next line and also so
             * the size of the combobox won't change over time.
             */
            int tbWidth = Util.getRequiredWidth(tb);
            int openerWidth = Util.getRequiredWidth(popupOpener);
            int iconWidth = Util.getRequiredWidth(selectedItemIcon);

            int w = tbWidth + openerWidth + iconWidth;
            if (suggestionPopupMinWidth > w) {
                if (!fixedTextBoxWidth) {
                    setTextboxWidth(suggestionPopupMinWidth);
                    w = suggestionPopupMinWidth;
                }
            } else {
                /*
                 * Firefox3 has its own way of doing rendering so we need to
                 * specify the width for the TextField to make sure it actually
                 * is rendered as wide as FF3 says it is
                 */
                tb.setWidth((tbWidth - getTextboxPadding()) + "px");
            }
            DOM.setStyleAttribute(getElement(), "width", w + "px");
            // Freeze the initial width, so that it won't change even if the
            // icon size changes
            width = w + "px";

        } else {
            /*
             * When the width is specified we also want to explicitly specify
             * widths for textbox and popupopener
             */
            setTextboxWidth(getMainWidth() - getComponentPadding());
        }
    }
}
