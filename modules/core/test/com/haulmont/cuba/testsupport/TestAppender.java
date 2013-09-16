/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author krivopustov
* @version $Id$
*/
public class TestAppender implements Appender {

    private List<String> messages = Collections.synchronizedList(new ArrayList<String>());

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public void addFilter(Filter newFilter) {
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void clearFilters() {
    }

    @Override
    public void close() {
    }

    @Override
    public void doAppend(LoggingEvent event) {
        messages.add(event.getMessage() == null ? "" : event.getMessage().toString());
    }

    @Override
    public String getName() {
        return "TestAppender";
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void setLayout(Layout layout) {
    }

    @Override
    public Layout getLayout() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
