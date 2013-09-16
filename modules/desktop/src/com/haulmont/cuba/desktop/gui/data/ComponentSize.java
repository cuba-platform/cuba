/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.gui.components.Component;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class ComponentSize {

    private static final String PIXEL = "px";
    private static final String PERCENT = "%";

    public final float value;
    public final int unit; // Component.UNITS_PIXELS or Component.UNITS_PERCENTAGE

    public ComponentSize(float value, int unit) {
        this.value = value;
        this.unit = unit;
    }

    public ComponentSize(float value) {
        this(value, Component.UNITS_PIXELS);
    }

    public boolean isOwnSize() {
        return value == -1 && unit == Component.UNITS_PIXELS;
    }

    public boolean inPixels() {
        return unit == Component.UNITS_PIXELS && value != -1;
    }

    public boolean inPercents() {
        return unit == Component.UNITS_PERCENTAGE;
    }

    public static final ComponentSize OWN_SIZE = new ComponentSize(-1);

    public static ComponentSize parse(String sizeString) throws IllegalArgumentException {
        if (sizeString == null || sizeString.isEmpty()) {
            return ComponentSize.OWN_SIZE;
        }
        try {
            if (sizeString.endsWith(PIXEL)) {
                String value = sizeString.substring(0, sizeString.length() - PIXEL.length());
                return new ComponentSize(Float.parseFloat(value), Component.UNITS_PIXELS);
            }
            else if (sizeString.endsWith(PERCENT)) {
                String value = sizeString.substring(0, sizeString.length() - PERCENT.length());
                return new ComponentSize(Float.parseFloat(value), Component.UNITS_PERCENTAGE);
            }
            else { // default unit
                return new ComponentSize(Float.parseFloat(sizeString), Component.UNITS_PIXELS);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unable to parse value: " + sizeString);
        }
    }
}
