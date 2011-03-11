/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 26.02.11 12:11
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.doctags;

import com.haulmont.cuba.report.formatters.oo.OOOConnection;
import com.haulmont.cuba.report.formatters.oo.OfficeComponent;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextRange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handle doctags in format strings
 */
public interface TagHandler {

    /**
     * Get Regexp Pattern for match format string
     *
     * @return Pattern
     */
    Pattern getTagPattern();

    /**
     * @param officeComponent OpenOffice Objects
     * @param destination     Text
     * @param textRange       Place for insert
     * @param paramValue      Parameter
     * @param matcher         Matcher for parameters regexp
     * @throws Exception
     */
    void handleTag(OfficeComponent officeComponent,
                   XText destination, XTextRange textRange,
                   Object paramValue, Matcher matcher)
            throws Exception;
}