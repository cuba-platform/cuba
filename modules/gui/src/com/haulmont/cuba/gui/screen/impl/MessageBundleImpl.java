/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.screen.impl;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.screen.MessageBundle;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(MessageBundle.NAME)
public class MessageBundleImpl implements MessageBundle {

    protected Messages messages;
    protected String messagePack;

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Override
    public String getMessagesPack() {
        return messagePack;
    }

    @Override
    public void setMessagesPack(String messagePack) {
        this.messagePack = messagePack;
    }

    @Override
    public String getMessage(String key) {
        if (Strings.isNullOrEmpty(messagePack)) {
            throw new IllegalStateException("messagePack is not set");
        }

        return messages.getMessage(messagePack, key);
    }

    @Override
    public String formatMessage(String key, Object... params) {
        if (Strings.isNullOrEmpty(messagePack)) {
            throw new IllegalStateException("messagePack is not set");
        }

        return messages.formatMessage(messagePack, key, params);
    }
}