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

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.SizeUnit;

public class ComponentSize {

    // FIXME: gg, why public?
    public final float value;
    @Deprecated
    public final int unit; // Component.UNITS_PIXELS or Component.UNITS_PERCENTAGE
    public final SizeUnit sizeUnit;

    /**
     * @deprecated use one of {@link #ComponentSize(float)} or {@link #ComponentSize(float, SizeUnit)}
     */
    @Deprecated
    public ComponentSize(float value, int unit) {
        this(value, ComponentsHelper.convertToSizeUnit(unit));
    }

    public ComponentSize(float value, SizeUnit sizeUnit) {
        this.value = value;
        this.sizeUnit = sizeUnit;
        this.unit = ComponentsHelper.convertFromSizeUnit(sizeUnit);
    }

    public ComponentSize(float value) {
        this(value, SizeUnit.PIXELS);
    }

    public boolean isOwnSize() {
        return value == -1 && sizeUnit == SizeUnit.PIXELS;
    }

    public boolean inPixels() {
        return sizeUnit == SizeUnit.PIXELS && value != -1;
    }

    public boolean inPercents() {
        return sizeUnit == SizeUnit.PERCENTAGE;
    }

    public static final ComponentSize OWN_SIZE = new ComponentSize(-1);

    public static ComponentSize parse(String sizeString) throws IllegalArgumentException {
        if (sizeString == null || sizeString.isEmpty()) {
            return ComponentSize.OWN_SIZE;
        }
        try {
            if (sizeString.endsWith(SizeUnit.PIXELS.getSymbol())) {
                String value = sizeString.substring(0, sizeString.length() - SizeUnit.PIXELS.getSymbol().length());
                return new ComponentSize(Float.parseFloat(value), SizeUnit.PIXELS);
            } else if (sizeString.endsWith(SizeUnit.PERCENTAGE.getSymbol())) {
                String value = sizeString.substring(0, sizeString.length() - SizeUnit.PERCENTAGE.getSymbol().length());
                return new ComponentSize(Float.parseFloat(value), SizeUnit.PERCENTAGE);
            } else { // default unit
                return new ComponentSize(Float.parseFloat(sizeString), SizeUnit.PIXELS);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unable to parse value: " + sizeString);
        }
    }
}