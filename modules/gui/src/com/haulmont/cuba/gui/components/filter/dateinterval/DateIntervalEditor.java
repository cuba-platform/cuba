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

package com.haulmont.cuba.gui.components.filter.dateinterval;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.dateinterval.predefined.PredefinedDateInterval;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.inject.Inject;
import java.util.*;

/**
 * Windows that is used for editing the "In interval" date condition of the generic filter component
 */
public class DateIntervalEditor extends AbstractWindow {

    @WindowParam(required = true)
    protected String dateIntervalDescription;

    @Inject
    protected TextField<Integer> numberField;

    @Inject
    protected LookupField<DateIntervalValue.TimeUnit> timeUnitLookup;

    @Inject
    protected OptionsGroup<DateIntervalValue.Type, DateIntervalValue.Type> typeOptionsGroup;

    @Inject
    protected CheckBox includingCurrentCheckBox;

    @Inject
    protected LookupField<PredefinedDateInterval> predefinedIntervalsLookup;

    @Inject
    protected ThemeConstants themeConstants;

    protected Multimap<DateIntervalValue.Type, Component> componentsVisibilityMap = ArrayListMultimap.create();

    protected DateIntervalValue intervalValue;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions()
                .setResizable(true)
                .setWidth(themeConstants.get("cuba.gui.filter.dateIntervalEditor.width"));

        componentsVisibilityMap.putAll(DateIntervalValue.Type.LAST, Arrays.asList(timeUnitLookup, numberField, includingCurrentCheckBox));
        componentsVisibilityMap.putAll(DateIntervalValue.Type.NEXT, Arrays.asList(timeUnitLookup, numberField, includingCurrentCheckBox));
        componentsVisibilityMap.putAll(DateIntervalValue.Type.PREDEFINED, Arrays.asList(predefinedIntervalsLookup));

        fillEnumLookup(typeOptionsGroup, DateIntervalValue.Type.class);
        fillEnumLookup(timeUnitLookup, DateIntervalValue.TimeUnit.class);
        fillPredefinedIntervalsLookup();
        numberField.setDatatype(Datatypes.get(Integer.class));

        intervalValue = AppBeans.getPrototype(DateIntervalValue.NAME, dateIntervalDescription);

        if (intervalValue.getType() == null) {
            intervalValue.setType(DateIntervalValue.Type.LAST);
            intervalValue.setNumber(5);
            intervalValue.setTimeUnit(DateIntervalValue.TimeUnit.DAY);
        }

        initComponentsVisibility(intervalValue.getType());

        typeOptionsGroup.setValue(intervalValue.getType());
        numberField.setValue(intervalValue.getNumber());
        timeUnitLookup.setValue(intervalValue.getTimeUnit());
        includingCurrentCheckBox.setValue(intervalValue.isIncludingCurrent());
        predefinedIntervalsLookup.setValue(intervalValue.getPredefinedDateInterval());

        typeOptionsGroup.addValueChangeListener(e -> {
            DateIntervalValue.Type type = e.getValue();
            intervalValue.setType(type);
            initComponentsVisibility(type);
        });
        numberField.addValueChangeListener(e -> intervalValue.setNumber(e.getValue()));
        timeUnitLookup.addValueChangeListener(e -> intervalValue.setTimeUnit(e.getValue()));
        includingCurrentCheckBox.addValueChangeListener(e -> intervalValue.setIncludingCurrent(e.getValue()));
        predefinedIntervalsLookup.addValueChangeListener(e -> intervalValue.setPredefinedDateInterval(e.getValue()));
    }

    @Override
    public void ready() {
        super.ready();
        setFocus();
    }

    protected void setFocus() {
        switch (intervalValue.getType()) {
            case LAST:
            case NEXT:
                numberField.focus();
                break;
            case PREDEFINED:
                predefinedIntervalsLookup.focus();
                break;
        }
    }

    protected void fillEnumLookup(OptionsField optionsField, Class<? extends Enum> enumClass) {
        Map<String, Object> options = new LinkedHashMap<>();
        for (Enum anEnum : enumClass.getEnumConstants()) {
            options.put(messages.getMessage(anEnum), anEnum);
        }
        optionsField.setOptionsMap(options);
    }

    public DateIntervalValue getDateIntervalValue() {
        return intervalValue;
    }

    protected void initComponentsVisibility(DateIntervalValue.Type type) {
        componentsVisibilityMap.values().forEach(component -> component.setVisible(false));
        componentsVisibilityMap.get(type).forEach(component -> component.setVisible(true));
    }

    protected void fillPredefinedIntervalsLookup() {
        Map<String, PredefinedDateInterval> optionsMap = new LinkedHashMap<>();
        for (PredefinedDateInterval interval : getPredefinedDateIntervals()) {
            optionsMap.put(interval.getLocalizedCaption(), interval);
        }
        predefinedIntervalsLookup.setOptionsMap(optionsMap);
    }

    protected Collection<PredefinedDateInterval> getPredefinedDateIntervals() {
        List<PredefinedDateInterval> intervals = new ArrayList<>(AppBeans.getAll(PredefinedDateInterval.class).values());
        intervals.sort(new AnnotationAwareOrderComparator());
        return intervals;
    }

    public void commit() {
        if (validateAll()) {
            close(COMMIT_ACTION_ID);
        }
    }

    public void close() {
        close(CLOSE_ACTION_ID);
    }
}
