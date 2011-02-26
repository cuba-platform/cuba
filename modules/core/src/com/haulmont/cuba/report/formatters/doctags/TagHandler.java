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

import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextRange;

import java.util.regex.Matcher;

/**
 * Handle doctags in format strings
 */
public interface TagHandler {
    /**
     * Insert image in Doc document
     *
     * @param xComponent    Document object
     * @param destination   Text
     * @param textRange     Place for insert
     * @param paramValue    Image URL
     * @param paramsMatcher Matcher for parameters regexp
     * @throws Exception
     */
    public void handleTag(XComponent xComponent, XText destination, XTextRange textRange,
                          String paramValue, Matcher paramsMatcher) throws Exception;
}