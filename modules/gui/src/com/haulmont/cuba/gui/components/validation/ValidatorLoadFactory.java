/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components.validation;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.MessageTools;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Specific bean for loading validators.
 */
@Component(ValidatorLoadFactory.NAME)
public class ValidatorLoadFactory {
    public static final String NAME = "cuba_ValidatorFactory";

    protected BeanLocator beanLocator;
    protected MessageTools messageTools;

    protected final Map<String, BiFunction<Element, String, AbstractValidator>> validatorsMap
            = ImmutableMap.<String, BiFunction<Element, String, AbstractValidator>>builder()
            .put("decimalMin", this::loadDecimalMinValidator)
            .put("decimalMax", this::loadDecimalMaxValidator)
            .put("doubleMin", this::loadDoubleMinValidator)
            .put("doubleMax", this::loadDoubleMaxValidator)
            .put("digits", this::loadDigitsValidator)
            .put("future", this::loadFutureValidator)
            .put("futureOrPresent", this::loadFutureOrPresentValidator)
            .put("max", this::loadMaxValidator)
            .put("min", this::loadMinValidator)
            .put("negativeOrZero", this::loadValidatorWithoutAttributes)
            .put("negative", this::loadValidatorWithoutAttributes)
            .put("notBlank", this::loadValidatorWithoutAttributes)
            .put("notEmpty", this::loadValidatorWithoutAttributes)
            .put("notNull", this::loadValidatorWithoutAttributes)
            .put("pastOrPresent", this::loadPastOrPresentValidator)
            .put("past", this::loadPastValidator)
            .put("positiveOrZero", this::loadValidatorWithoutAttributes)
            .put("positive", this::loadValidatorWithoutAttributes)
            .put("regexp", this::loadRegexpValidator)
            .put("size", this::loadSizeValidator)
            .build();

    @Inject
    protected void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Inject
    protected void setMessageTools(MessageTools messageTools) {
        this.messageTools = messageTools;
    }

    /**
     * Creates validator from XML element.
     *
     * @param element     validator element
     * @param messagePack message pack
     * @return validator or null if there is no such element
     */
    @Nullable
    public AbstractValidator createValidator(Element element, String messagePack) {
        BiFunction<Element, String, AbstractValidator> function = validatorsMap.get(element.getName());
        if (function != null) {
            return function.apply(element, messagePack);
        }
        return null;
    }

    protected AbstractValidator loadDecimalMinValidator(Element element, String messagePack) {
        String value = element.attributeValue("value");
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Min value is not defined");
        }
        BigDecimal decimalValue = new BigDecimal(value);
        DecimalMinValidator validator = beanLocator.getPrototype(DecimalMinValidator.NAME, decimalValue);

        Boolean inclusive = loadInclusive(element);
        if (inclusive != null) {
            validator.setInclusive(inclusive);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadDecimalMaxValidator(Element element, String messagePack) {
        String value = element.attributeValue("value");
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Max value is not defined");
        }
        BigDecimal decimalValue = new BigDecimal(value);
        DecimalMaxValidator validator = beanLocator.getPrototype(DecimalMaxValidator.NAME, decimalValue);

        Boolean inclusive = loadInclusive(element);
        if (inclusive != null) {
            validator.setInclusive(inclusive);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadDoubleMinValidator(Element element, String messagePack) {
        String value = element.attributeValue("value");
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Min value is not defined");
        }
        Double doubleValue = Double.valueOf(value);
        DoubleMinValidator validator = beanLocator.getPrototype(DoubleMinValidator.NAME, doubleValue);

        Boolean inclusive = loadInclusive(element);
        if (inclusive != null) {
            validator.setInclusive(inclusive);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadDoubleMaxValidator(Element element, String messagePack) {
        String value = element.attributeValue("value");
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Max value is not defined");
        }
        Double doubleValue = Double.valueOf(value);
        DoubleMaxValidator validator = beanLocator.getPrototype(DoubleMaxValidator.NAME, doubleValue);

        Boolean inclusive = loadInclusive(element);
        if (inclusive != null) {
            validator.setInclusive(inclusive);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadDigitsValidator(Element element, String messagePack) {
        String integer = element.attributeValue("integer");
        if (Strings.isNullOrEmpty(integer)) {
            throw new IllegalArgumentException("Integer value is not defined");
        }
        int integerValue = Integer.parseInt(integer);

        String fraction = element.attributeValue("fraction");
        if (Strings.isNullOrEmpty(fraction)) {
            throw new IllegalArgumentException("Fraction value is not defined");
        }
        int fractionValue = Integer.parseInt(fraction);

        DigitsValidator validator = beanLocator.getPrototype(DigitsValidator.NAME, integerValue, fractionValue);
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadFutureValidator(Element element, String messagePack) {
        FutureValidator validator = beanLocator.getPrototype(FutureValidator.NAME);

        Boolean checkSeconds = loadCheckSeconds(element);
        if (checkSeconds != null) {
            validator.setCheckSeconds(checkSeconds);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadFutureOrPresentValidator(Element element, String messagePack) {
        FutureOrPresentValidator validator = beanLocator.getPrototype(FutureOrPresentValidator.NAME);

        Boolean checkSeconds = loadCheckSeconds(element);
        if (checkSeconds != null) {
            validator.setCheckSeconds(checkSeconds);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadPastValidator(Element element, String messagePack) {
        PastValidator validator = beanLocator.getPrototype(PastValidator.NAME);

        Boolean checkSeconds = loadCheckSeconds(element);
        if (checkSeconds != null) {
            validator.setCheckSeconds(checkSeconds);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadPastOrPresentValidator(Element element, String messagePack) {
        PastOrPresentValidator validator = beanLocator.getPrototype(PastOrPresentValidator.NAME);

        Boolean checkSeconds = loadCheckSeconds(element);
        if (checkSeconds != null) {
            validator.setCheckSeconds(checkSeconds);
        }
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadMaxValidator(Element element, String messagePack) {
        String value = element.attributeValue("value");
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Max value is not defined");
        }
        long maxValue = Long.parseLong(value);

        MaxValidator validator = beanLocator.getPrototype(MaxValidator.NAME, maxValue);
        validator.setMessage(loadMessage(element, messagePack));
        return validator;
    }

    protected AbstractValidator loadMinValidator(Element element, String messagePack) {
        String value = element.attributeValue("value");
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Min value is not defined");
        }
        long minValue = Long.parseLong(value);

        MinValidator validator = beanLocator.getPrototype(MinValidator.NAME, minValue);
        validator.setMessage(loadMessage(element, messagePack));
        return validator;
    }

    protected AbstractValidator loadValidatorWithoutAttributes(Element element, String messagePack) {
        AbstractValidator validator;
        switch (element.getName()) {
            case "negativeOrZero":
                validator = beanLocator.getPrototype(NegativeOrZeroValidator.NAME);
                break;
            case "negative":
                validator = beanLocator.getPrototype(NegativeValidator.NAME);
                break;
            case "notBlank":
                validator = beanLocator.getPrototype(NotBlankValidator.NAME);
                break;
            case "notEmpty":
                validator = beanLocator.getPrototype(NotEmptyValidator.NAME);
                break;
            case "notNull":
                validator = beanLocator.getPrototype(NotNullValidator.NAME);
                break;
            case "positiveOrZero":
                validator = beanLocator.getPrototype(PositiveOrZeroValidator.NAME);
                break;
            case "positive":
                validator = beanLocator.getPrototype(PositiveValidator.NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown validator element: " + element.getName());
        }
        validator.setMessage(loadMessage(element, messagePack));
        return validator;
    }

    protected AbstractValidator loadRegexpValidator(Element element, String messagePack) {
        String regexp = element.attributeValue("regexp");

        Preconditions.checkNotNullArgument(regexp);

        RegexpValidator validator = beanLocator.getPrototype(RegexpValidator.NAME, regexp);
        validator.setMessage(loadMessage(element, messagePack));

        return validator;
    }

    protected AbstractValidator loadSizeValidator(Element element, String messagePack) {
        SizeValidator validator = beanLocator.getPrototype(SizeValidator.NAME);

        String min = element.attributeValue("min");
        if (min != null) {
            validator.setMin(Integer.parseInt(min));
        }
        String max = element.attributeValue("max");
        if (max != null) {
            validator.setMax(Integer.parseInt(max));
        }

        validator.setMessage(loadMessage(element, messagePack));
        return validator;
    }

    @Nullable
    protected Boolean loadInclusive(Element element) {
        String inclusive = element.attributeValue("inclusive");
        if (inclusive != null) {
            return Boolean.parseBoolean(inclusive);
        }
        return null;
    }

    @Nullable
    protected Boolean loadCheckSeconds(Element element) {
        String checkSeconds = element.attributeValue("checkSeconds");
        if (checkSeconds != null) {
            return Boolean.parseBoolean(checkSeconds);
        }
        return null;
    }

    @Nullable
    protected String loadMessage(Element element, String messagePack) {
        String message = element.attributeValue("message");
        if (!Strings.isNullOrEmpty(message)) {
            return messageTools.loadString(messagePack, message);
        }
        return null;
    }
}
