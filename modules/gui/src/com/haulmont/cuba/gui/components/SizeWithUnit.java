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

package com.haulmont.cuba.gui.components;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for representing a value-unit pair. Also contains utility methods for
 * parsing such pairs from a string.
 */
public class SizeWithUnit implements Serializable {
    public static final String SIZE_PATTERN_STRING = "^(-?\\d*(?:\\.\\d+)?)(%|px)?$";

    protected float size;
    protected SizeUnit unit;
    protected static final Pattern SIZE_PATTERN = Pattern.compile(SIZE_PATTERN_STRING);

    /**
     * Constructs a new SizeWithUnit object representing the pair (size, unit).
     *
     * @param size a numeric value
     * @param unit a unit
     */
    public SizeWithUnit(float size, SizeUnit unit) {
        this.size = size;
        this.unit = unit;
    }

    /**
     * Returns the numeric value stored in this object.
     *
     * @return the numeric value stored in this object
     */
    public float getSize() {
        return size;
    }

    /**
     * Returns the size unit stored in this object.
     *
     * @return the size unit stored in this object
     */
    public SizeUnit getUnit() {
        return unit;
    }

    public String stringValue() {
        return size + unit.getSymbol();
    }

    /**
     * Returns an object whose numeric value and unit are taken from the string
     * {@code sizeString}. If {@code sizeString} does not specify a unit and {@code defaultUnit} is not null,
     * {@code defaultUnit} is used as the unit. Null, empty or 'AUTO' string will produce {-1, SizeUnit#PIXELS}.
     *
     * @param sizeString  the string to be parsed
     * @param defaultUnit The unit to be used if {@code sizeString} does not contain any unit.
     *                    Use {@code null} for no default unit.
     * @return an object containing the parsed value and unit
     */
    public static SizeWithUnit parseStringSize(String sizeString, SizeUnit defaultUnit) {
        if (StringUtils.isEmpty(sizeString) || "auto".equalsIgnoreCase(sizeString)) {
            return new SizeWithUnit(-1, SizeUnit.PIXELS);
        }

        float size;
        SizeUnit unit;
        Matcher matcher = SIZE_PATTERN.matcher(sizeString);
        if (matcher.find()) {
            size = Float.parseFloat(matcher.group(1));
            if (size < 0) {
                size = -1;
                unit = SizeUnit.PIXELS;
            } else {
                String symbol = matcher.group(2);
                if ((symbol != null && symbol.length() > 0)
                        || defaultUnit == null) {
                    unit = SizeUnit.getUnitFromSymbol(symbol);
                } else {
                    unit = defaultUnit;
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid size argument: \"" + sizeString
                    + "\" (should match " + SIZE_PATTERN.pattern() + ")");
        }

        return new SizeWithUnit(size, unit);
    }

    /**
     * Returns an object whose numeric value and unit are taken from the string
     * size. Null, empty or 'AUTO' string will produce {-1, SizeUnit#PIXELS}.
     *
     * @param size the string to be parsed
     * @return an object containing the parsed value and unit
     */
    public static SizeWithUnit parseStringSize(String size) {
        return parseStringSize(size, null);
    }
}