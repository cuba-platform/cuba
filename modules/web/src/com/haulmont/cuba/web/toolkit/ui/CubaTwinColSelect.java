/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.twincolselect.CubaTwinColSelectState;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.TwinColSelect;
import org.apache.commons.lang.StringUtils;

/**
 * @author gorodnov
 * @version $Id$
 */
@SuppressWarnings("serial")
public class CubaTwinColSelect extends TwinColSelect {

    private OptionStyleGenerator styleGenerator;

    @Override
    protected void paintItem(PaintTarget target, Object itemId)
            throws PaintException {
        super.paintItem(target, itemId);

        if (styleGenerator != null) {
            String style = styleGenerator.generateStyle(this, itemId, isSelected(itemId));
            if (!StringUtils.isEmpty(style)) {
                target.addAttribute("style", style);
            }
        }
    }

    public OptionStyleGenerator getStyleGenerator() {
        return styleGenerator;
    }

    public void setStyleGenerator(OptionStyleGenerator styleGenerator) {
        this.styleGenerator = styleGenerator;
        markAsDirty();
    }

    public interface OptionStyleGenerator {
        String generateStyle(AbstractSelect source, Object itemId, boolean selected);
    }

    public boolean isAddAllBtnEnabled() {
        return getState(false).addAllBtnEnabled;
    }

    public void setAddAllBtnEnabled(boolean addAllBtnEnabled) {
        if (isAddAllBtnEnabled() != addAllBtnEnabled) {
            getState(true).addAllBtnEnabled = addAllBtnEnabled;
        }
    }

    @Override
    protected CubaTwinColSelectState getState() {
        return (CubaTwinColSelectState) super.getState();
    }

    @Override
    protected CubaTwinColSelectState getState(boolean markAsDirty) {
        return (CubaTwinColSelectState) super.getState(markAsDirty);
    }
}