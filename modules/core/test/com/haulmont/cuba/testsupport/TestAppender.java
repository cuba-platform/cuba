/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
* @author krivopustov
* @version $Id$
*/
public class TestAppender extends AppenderBase<ILoggingEvent> {

    private List<String> messages = Collections.synchronizedList(new ArrayList<>());

    public List<String> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }

    public Stream<String> filterMessages(Predicate<String> predicate) {
        return messages.stream().filter(predicate);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        messages.add(eventObject.getMessage() == null ? "" : eventObject.getMessage());
    }
}
