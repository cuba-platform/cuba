/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.twincolselect.CubaTwinColSelectState;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.TwinColSelect;
import org.apache.commons.lang.StringUtils;

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