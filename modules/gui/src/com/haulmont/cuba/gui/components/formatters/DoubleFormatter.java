/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Evgeny Zaharchenko
 * Created: 19.02.11 18:59
 *
 * $Id$
 */

package com.haulmont.cuba.gui.components.formatters;

import org.dom4j.Element;

/**
 * @deprecated Need to use {@link com.haulmont.cuba.gui.components.formatters.NumberFormatter} class
 */
@Deprecated
public class DoubleFormatter extends NumberFormatter {
    private static final long serialVersionUID = 5002355442398684447L;

    public DoubleFormatter(Element element) {
        super(element);
    }
}
