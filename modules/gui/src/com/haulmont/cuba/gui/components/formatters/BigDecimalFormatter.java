/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Ilya Grachev
 * Created: 26.08.2009 12:29:31
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.formatters;

import org.dom4j.Element;

/**
 * @deprecated Need to use {@link com.haulmont.cuba.gui.components.formatters.NumberFormatter} class
 */
@Deprecated
public class BigDecimalFormatter extends NumberFormatter {
    private static final long serialVersionUID = 6497597645156934900L;

    public BigDecimalFormatter(Element element) {
        super(element);
    }
}
