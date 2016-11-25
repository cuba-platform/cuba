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
 */

package com.haulmont.cuba.core.sys.validation;

import com.haulmont.cuba.core.global.Messages;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ExpressionFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CubaValidationMessagesInterpolator implements MessageInterpolator {

    private final Logger log = LoggerFactory.getLogger(CubaValidationMessagesInterpolator.class);

    protected Messages messages;
    protected ExpressionFactory expressionFactory;
    protected Locale locale;

    private static final Pattern LEFT_BRACE = Pattern.compile("\\{", Pattern.LITERAL);
    private static final Pattern RIGHT_BRACE = Pattern.compile("\\}", Pattern.LITERAL);
    private static final Pattern SLASH = Pattern.compile("\\\\", Pattern.LITERAL);
    private static final Pattern DOLLAR = Pattern.compile("\\$", Pattern.LITERAL);

    public CubaValidationMessagesInterpolator(Messages messages, Locale locale) {
        this.messages = messages;
        this.locale = locale;
        this.expressionFactory = ExpressionFactory.newInstance();
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        Locale defaultLocale = locale;
        return interpolate(messageTemplate, context, defaultLocale);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        String interpolatedMessage = messageTemplate;
        try {
            interpolatedMessage = interpolateMessage(messageTemplate, context, locale);
        } catch (ValidationException e) {
            log.error("Unable to interpolate validation message: {}", e.getMessage());
        }
        return interpolatedMessage;
    }

    protected String interpolateMessage(String messageTemplate, Context context, Locale locale) {
        String resolvedMessage = interpolateMessage(messageTemplate, locale, true);

        TokenCollector tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.PARAMETER);
        List<Token> tokens = tokenCollector.getTokenList();

        resolvedMessage = interpolateExpression(new TokenIterator(tokens), context, locale);

        tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.EL);
        tokens = tokenCollector.getTokenList();

        resolvedMessage = interpolateExpression(new TokenIterator(tokens), context, locale);

        resolvedMessage = replaceEscapedLiterals(resolvedMessage);

        return resolvedMessage;
    }

    protected String interpolateExpression(TokenIterator tokenIterator, Context context, Locale locale)
            throws MessageDescriptorFormatException {
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();

            String resolvedExpression = interpolate(context, locale, term);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    protected String replaceEscapedLiterals(String resolvedMessage) {
        resolvedMessage = LEFT_BRACE.matcher(resolvedMessage).replaceAll("{");
        resolvedMessage = RIGHT_BRACE.matcher(resolvedMessage).replaceAll("}");
        resolvedMessage = SLASH.matcher(resolvedMessage).replaceAll(Matcher.quoteReplacement("\\"));
        resolvedMessage = DOLLAR.matcher(resolvedMessage).replaceAll(Matcher.quoteReplacement("$"));
        return resolvedMessage;
    }

    protected String interpolate(Context context, Locale locale, String term) {
        InterpolationTerm expression = new InterpolationTerm(term, locale, expressionFactory);
        return expression.interpolate(context);
    }

    protected String interpolateMessage(String message, Locale locale, boolean recursive)
            throws MessageDescriptorFormatException {
        TokenCollector tokenCollector = new TokenCollector(message, InterpolationTermType.PARAMETER);
        TokenIterator tokenIterator = new TokenIterator(tokenCollector.getTokenList());
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();
            String resolvedParameterValue = resolveParameter(term, locale, recursive);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedParameterValue);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    protected String resolveParameter(String parameterName, Locale locale, boolean recursive)
            throws MessageDescriptorFormatException {
        String parameterValue;
        try {
            String messageCode = removeCurlyBraces(parameterName);
            parameterValue = messages.getTools().loadString(messageCode, locale);

            if (recursive) {
                //noinspection ConstantConditions
                parameterValue = interpolateMessage(parameterValue, locale, recursive);
            }
        } catch (MissingResourceException | UnsupportedOperationException e) {
            // return parameter itself
            parameterValue = parameterName;
        }
        return parameterValue;
    }

    protected String removeCurlyBraces(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }
}