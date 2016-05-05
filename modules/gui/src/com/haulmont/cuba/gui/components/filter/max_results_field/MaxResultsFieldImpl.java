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
 */

package com.haulmont.cuba.gui.components.filter.max_results_field;

import com.google.common.base.Splitter;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component(MaxResultsFieldHelper.NAME)
public class MaxResultsFieldImpl implements MaxResultsFieldHelper {

    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected FilterHelper filterHelper;
    @Inject
    protected ThemeConstantsManager themeConstantsManager;
    @Inject
    ClientConfig clientConfig;

    public LookupField createMaxResultsLookupField() {
        LookupField maxResultsLookupField = componentsFactory.createComponent(LookupField.class);
        setUpMaxResultsLookupField(maxResultsLookupField);

        return maxResultsLookupField;
    }

    public LookupField setUpMaxResultsLookupField(LookupField maxResultsLookupField) {
        ThemeConstants theme = themeConstantsManager.getConstants();

        maxResultsLookupField.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsLookupField.setWidth(theme.get("cuba.gui.Filter.maxResults.lookup.width"));
        filterHelper.setLookupTextInputAllowed(maxResultsLookupField, false);
        filterHelper.setLookupNullSelectionAllowed(maxResultsLookupField, false);

        List<Integer> maxResultOptions = new ArrayList<>();
        String maxResultOptionsStr = clientConfig.getGenericFilterMaxResultsOptions();
        Iterable<String> split = Splitter.on(",").trimResults().split(maxResultOptionsStr);
        for (String option : split) {
            if ("NULL".equals(option)) {
                filterHelper.setLookupNullSelectionAllowed(maxResultsLookupField, true);
            } else {
                try {
                    Integer value = Integer.valueOf(option);
                    maxResultOptions.add(value);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        maxResultsLookupField.setOptionsList(maxResultOptions);

        return maxResultsLookupField;
    }
}
