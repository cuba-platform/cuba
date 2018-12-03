/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */
package com.haulmont.cuba.web.widgets;

import com.google.common.base.Strings;
import com.haulmont.cuba.web.widgets.client.twincolselect.CubaTwinColSelectState;
import com.vaadin.data.provider.DataGenerator;
import com.vaadin.ui.TwinColSelect;
import elemental.json.JsonObject;

@SuppressWarnings("serial")
public class CubaTwinColSelect<V> extends TwinColSelect<V> {

    protected CubaOptionStyleProvider<V> styleProvider;

    public CubaTwinColSelect() {
        addDataGenerator(createDataGenerator());
    }

    public void setOptionStyleProvider(CubaOptionStyleProvider<V> styleProvider) {
        this.styleProvider = styleProvider;
        refreshDataItems();
    }

    protected DataGenerator<V> createDataGenerator() {
        return new TwinColumnDataGenerator();
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

    protected void refreshDataItems() {
        getDataProvider().refreshAll();
    }

    protected class TwinColumnDataGenerator implements DataGenerator<V> {

        @Override
        public void generateData(V item, JsonObject jsonObject) {
            if (styleProvider != null) {
                String style = styleProvider.getStyleName(item, isSelected(item));
                if (!Strings.isNullOrEmpty(style)) {
                    jsonObject.put("style", style);
                }
            }
        }
    }

    public interface CubaOptionStyleProvider<V> {

        String getStyleName(V item, boolean selected);
    }
}