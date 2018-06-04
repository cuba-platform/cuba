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

package com.haulmont.cuba.testsupport;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

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