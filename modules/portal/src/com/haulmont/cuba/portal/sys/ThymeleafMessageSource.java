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

package com.haulmont.cuba.portal.sys;

import com.haulmont.cuba.core.global.Messages;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Locale;

public class ThymeleafMessageSource implements MessageSource {
    @Inject
    protected Messages messages;

    @Override
    public String getMessage(@Nonnull String code, Object[] args, String defaultMessage, @Nonnull Locale locale) {
        if (args != null && args.length > 0) {
            return messages.formatMainMessage(code, args, locale);
        }

        return messages.getMainMessage(code, locale);
    }

    @Nonnull
    @Override
    public String getMessage(@Nonnull String code, Object[] args, @Nonnull Locale locale) throws NoSuchMessageException {
        if (args != null && args.length > 0) {
            return messages.formatMainMessage(code, args, locale);
        }

        return messages.getMainMessage(code, locale);
    }

    @Nonnull
    @Override
    public String getMessage(@Nonnull MessageSourceResolvable resolvable, @Nonnull Locale locale) throws NoSuchMessageException {
        throw new NoSuchMessageException("Unsupported message resolving mechanism");
    }
}