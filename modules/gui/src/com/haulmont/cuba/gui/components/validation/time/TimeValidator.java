/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation.time;

/**
 * Base interface for date/time validators which contains all restrictions.
 */
public interface TimeValidator {

    /**
     * @return true if date or time in the past
     */
    boolean isPast();

    /**
     * @return true if date or time in the past or present
     */
    boolean isPastOrPresent();

    /**
     * @return true if date or time in the future
     */
    boolean isFuture();

    /**
     * @return true if date or time in the future or present
     */
    boolean isFutureOrPresent();

    /**
     * Sets check seconds to validator. Set true if validator should check seconds and nanos while it comparing dates
     * or times.
     *
     * @param checkSeconds check seconds and nanos option, false by default
     */
    void setCheckSeconds(boolean checkSeconds);
}
