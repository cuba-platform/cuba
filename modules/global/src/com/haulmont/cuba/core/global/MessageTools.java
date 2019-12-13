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

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.annotation.LocalizedValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Utility class to provide common functionality related to localized messages.
 * <br> Implemented as Spring bean to allow extension in application projects.
 * <br> A reference to this class can be obtained either via DI or by
 * {@link com.haulmont.cuba.core.global.Messages#getTools()} method.
 */
@Component(MessageTools.NAME)
public class MessageTools {

    /**
     * Prefix defining that the string is actually a key in a localized messages pack.
     */
    public static final String MARK = "msg://";
    public static final String MAIN_MARK = "mainMsg://";

    public static final String NAME = "cuba_MessageTools";

    private static final Logger log = LoggerFactory.getLogger(MessageTools.class);

    protected volatile Boolean useLocaleLanguageOnly;

    @Inject
    protected Messages messages;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ExtendedEntities extendedEntities;

    protected GlobalConfig globalConfig;

    @Inject
    public MessageTools(Configuration configuration) {
        globalConfig = configuration.getConfig(GlobalConfig.class);
    }

    /**
     * Get localized message by reference provided in the full format.
     * @param ref   reference to message in the following format: {@code msg://message_pack/message_id}
     * @return      localized message or input string itself if it doesn't begin with {@code msg://}
     */
    public String loadString(String ref) {
        return loadString(null, ref);
    }

    /**
     * Get localized message by reference provided in the full format.
     * @param ref   reference to message in the following format: {@code msg://message_pack/message_id}
     * @return      localized message or input string itself if it doesn't begin with {@code msg://}
     */
    public String loadString(String ref, Locale locale) {
        return loadString(null, ref, locale);
    }

    /**
     * Get localized message by reference provided in full or brief format.
     * @param messagesPack  messages pack to use if the second parameter is in brief format
     * @param ref           reference to message in the following format:
     * <ul>
     * <li>Full: {@code msg://message_pack/message_id}
     * <li>Brief: {@code msg://message_id}, in this case the first parameter is taken into account
     * <li>Message from a main messages pack: {@code mainMsg://message_id}
     * </ul>
     * @return localized message or input string itself if it doesn't begin with {@code msg://} or {@code mainMsg://}
     */
    @Nullable
    public String loadString(@Nullable String messagesPack, @Nullable String ref) {
        return loadString(messagesPack, ref, null);
    }

    /**
     * Get localized message by reference provided in full or brief format.
     * @param messagesPack  messages pack to use if the second parameter is in brief format
     * @param ref           reference to message in the following format:
     * @param locale        locale
     * <ul>
     * <li>Full: {@code msg://message_pack/message_id}
     * <li>Brief: {@code msg://message_id}, in this case the first parameter is taken into account
     * <li>Message from a main messages pack: {@code mainMsg://message_id}
     * </ul>
     * @return localized message or input string itself if it doesn't begin with {@code msg://} or {@code mainMsg://}
     */
    @Nullable
    public String loadString(@Nullable String messagesPack, @Nullable String ref, @Nullable Locale locale) {
        if (ref != null) {
            if (ref.startsWith(MARK)) {
                String path = ref.substring(6);
                final String[] strings = path.split("/");
                if (strings.length == 1 && messagesPack != null) {
                    if (locale == null) {
                        ref = messages.getMessage(messagesPack, strings[0]);
                    } else {
                        ref = messages.getMessage(messagesPack, strings[0], locale);
                    }
                } else if (strings.length == 2) {
                    if (locale == null) {
                        ref = messages.getMessage(strings[0], strings[1]);
                    } else {
                        ref = messages.getMessage(strings[0], strings[1], locale);
                    }
                } else {
                    throw new UnsupportedOperationException("Unsupported resource string format: '" + ref
                            + "', messagesPack=" + messagesPack);
                }
            } else if (ref.startsWith(MAIN_MARK)) {
                String path = ref.substring(10);

                if (locale == null) {
                    return messages.getMainMessage(path);
                } else {
                    return messages.getMainMessage(path, locale);
                }
            }
        }
        return ref;
    }

    /**
     * @return a localized name of an entity. Messages pack must be located in the same package as entity.
     */
    public String getEntityCaption(MetaClass metaClass) {
        return getEntityCaption(metaClass, null);
    }

    /**
     * @return a localized name of an entity with given locale or default if null. Messages pack must be located in the same package as entity.
     */
    public String getEntityCaption(MetaClass metaClass, @Nullable Locale locale) {
        Function<MetaClass, String> getMessage = locale != null ?
            mc -> messages.getMessage(mc.getJavaClass(), mc.getJavaClass().getSimpleName(), locale) :
            mc -> messages.getMessage(mc.getJavaClass(), mc.getJavaClass().getSimpleName());

        String message = getMessage.apply(metaClass);
        if (metaClass.getJavaClass().getSimpleName().equals(message)) {
            MetaClass original = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
            if (original != null)
                return getMessage.apply(original);
        }
        return message;
    }

    /**
     * @return a detailed localized name of an entity. Messages pack must be located in the same package as entity.
     */
    public String getDetailedEntityCaption(MetaClass metaClass) {
        return getDetailedEntityCaption(metaClass, null);
    }

    /**
     * @return a detailed localized name of an entity with given locale or default if null. Messages pack must be located in the same package as entity.
     */
    public String getDetailedEntityCaption(MetaClass metaClass, @Nullable Locale locale) {
        return getEntityCaption(metaClass, locale) + " (" + metaClass.getName() + ")";
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return              localized name
     */
    public String getPropertyCaption(MetaClass metaClass, String propertyName) {
        return getPropertyCaption(metaClass, propertyName, null);
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     *
     * @param metaClass    MetaClass containing the property
     * @param propertyName property's name
     * @param locale       locale, if value is null locale of current user is used
     * @return localized name
     */
    public String getPropertyCaption(MetaClass metaClass, String propertyName, @Nullable Locale locale) {
        Class originalClass = extendedEntities.getOriginalClass(metaClass);
        Class<?> ownClass = originalClass != null ? originalClass : metaClass.getJavaClass();
        String className = ownClass.getSimpleName();

        String key = className + "." + propertyName;
        String message;
        if (locale == null) {
            message = messages.getMessage(ownClass, key);
        } else {
            message = messages.getMessage(ownClass, key, locale);
        }

        if (!message.equals(key)) {
            return message;
        }

        MetaPropertyPath propertyPath = metaClass.getPropertyPath(propertyName);
        if (propertyPath != null) {
            return getPropertyCaption(propertyPath.getMetaProperty());
        } else {
            return message;
        }
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     *
     * @param property MetaProperty
     * @return localized name
     */
    public String getPropertyCaption(MetaProperty property) {
        return getPropertyCaption(property, null);
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     *
     * @param property MetaProperty
     * @param locale   locale, if value is null locale of current user is used
     * @return localized name
     */
    public String getPropertyCaption(MetaProperty property, @Nullable Locale locale) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null) {
            return property.getName();
        }

        String className = declaringClass.getSimpleName();
        if (locale == null) {
            return messages.getMessage(declaringClass, className + "." + property.getName());
        }

        return messages.getMessage(declaringClass, className + "." + property.getName(), locale);
    }

    /**
     * Checks whether a localized name of the property exists.
     * @param property  MetaProperty
     * @return          true if {@link #getPropertyCaption(com.haulmont.chile.core.model.MetaProperty)} returns a
     * string which has no dots inside or the first part before a dot is not equal to the declaring class
     */
    public boolean hasPropertyCaption(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null)
            return false;

        String caption = getPropertyCaption(property);
        int i = caption.indexOf('.');
        if (i > 0 && declaringClass.getSimpleName().equals(caption.substring(0, i)))
            return false;
        else
            return true;
    }

    /**
     * @deprecated Use {@link #getDefaultRequiredMessage(MetaClass, String)}
     * @return default required message for specified property.
     */
    @Deprecated
    public String getDefaultRequiredMessage(MetaProperty metaProperty) {
        String notNullMessage = getNotNullMessage(metaProperty);
        if (notNullMessage != null) {
            return notNullMessage;
        }

        return messages.formatMessage(messages.getMainMessagePack(),
                "validation.required.defaultMsg", getPropertyCaption(metaProperty));
    }

    /**
     * Get default required message for specified property of MetaClass.
     *
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return default required message for specified property of MetaClass
     */
    public String getDefaultRequiredMessage(MetaClass metaClass, String propertyName) {
        MetaPropertyPath propertyPath = metaClass.getPropertyPath(propertyName);
        if (propertyPath != null) {
            String notNullMessage = getNotNullMessage(propertyPath.getMetaProperty());
            if (notNullMessage != null) {
                return notNullMessage;
            }
        }

        return messages.formatMessage(messages.getMainMessagePack(),
                "validation.required.defaultMsg", getPropertyCaption(metaClass, propertyName));
    }

    /**
     * Get default required message for specified property of MetaClass if it has {@link NotNull} annotation.
     *
     * @param metaProperty MetaProperty
     * @return localized not null message
     */
    protected String getNotNullMessage(MetaProperty metaProperty) {
        String notNullMessage = (String) metaProperty.getAnnotations()
                .get(NotNull.class.getName() + "_notnull_message");
        if (notNullMessage != null
                && !"{javax.validation.constraints.NotNull.message}".equals(notNullMessage)) {
            if (notNullMessage.startsWith("{") && notNullMessage.endsWith("}")) {
                notNullMessage = notNullMessage.substring(1, notNullMessage.length() - 1);
                if (notNullMessage.startsWith(MAIN_MARK) || notNullMessage.startsWith(MARK)) {
                    return loadString(notNullMessage);
                }
            }
            // return as is, parameters and value interpolation are not supported
            return notNullMessage;
        }
        return null;
    }

    /**
     * Get message reference of an entity property.
     * Messages pack part of the reference corresponds to the entity's package.
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return              message key in the form {@code msg://message_pack/message_id}
     */
    public String getMessageRef(MetaClass metaClass, String propertyName) {
        MetaProperty property = metaClass.getProperty(propertyName);
        if (property == null) {
            throw new RuntimeException("Property " + propertyName + " is wrong for metaclass " + metaClass);
        }
        return getMessageRef(property);
    }

    /**
     * Get message reference of an entity property.
     * Messages pack part of the reference corresponds to the entity's package.
     *
     * @param property MetaProperty
     * @return message key in the form {@code msg://message_pack/message_id}
     */
    public String getMessageRef(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null)
            return MARK + property.getName();

        String className = declaringClass.getName();
        String packageName= "";
        int i = className.lastIndexOf('.');
        if (i > -1) {
            packageName = className.substring(0, i);
            className = className.substring(i + 1);
        }

        return MARK + packageName + "/" + className + "." + property.getName();
    }

    /**
     * Get localized value of an attribute based on {@link com.haulmont.cuba.core.entity.annotation.LocalizedValue} annotation.
     *
     * @param attribute attribute name
     * @param instance  entity instance
     * @return localized value or the value itself, if the value is null or the message pack can not be inferred
     */
    @Nullable
    public String getLocValue(String attribute, Instance instance) {
        String value = instance.getValue(attribute);
        if (value == null)
            return null;

        String mp = inferMessagePack(attribute, instance);
        if (mp == null)
            return value;
        else
            return messages.getMessage(mp, value);
    }

    /**
     * Returns message pack inferred from {@link com.haulmont.cuba.core.entity.annotation.LocalizedValue} annotation.
     * @param attribute attribute name
     * @param instance  entity instance
     * @return          inferred message pack or null
     */
    @Nullable
    public String inferMessagePack(String attribute, Instance instance) {
        Objects.requireNonNull(attribute, "attribute is null");
        Objects.requireNonNull(instance, "instance is null");

        MetaClass metaClass = metadata.getSession().getClassNN(instance.getClass());
        MetaProperty property = metaClass.getPropertyNN(attribute);
        Map<String, Object> attributes = metadata.getTools().getMetaAnnotationAttributes(property.getAnnotations(), LocalizedValue.class);
        String messagePack = (String) attributes.get("messagePack");
        if (!StringUtils.isBlank(messagePack))
            return messagePack;
        else {
            String messagePackExpr = (String) attributes.get("messagePackExpr");
            if (!StringUtils.isBlank(messagePackExpr)) {
                try {
                    return instance.getValueEx(messagePackExpr);
                } catch (Exception e) {
                    log.error("Unable to infer message pack from expression: " + messagePackExpr, e);
                }
            }
        }
        return null;
    }

    /**
     * @return whether to use a full locale representation, or language only. Returns true if all locales listed
     * in {@code cuba.availableLocales} app property are language only.
     */
    public boolean useLocaleLanguageOnly() {
        if (useLocaleLanguageOnly == null) {
            boolean found = false;
            for (Locale locale : globalConfig.getAvailableLocales().values()) {
                if (!StringUtils.isEmpty(locale.getCountry()) || !StringUtils.isEmpty(locale.getVariant())
                || !StringUtils.isEmpty(locale.getScript())) {
                    useLocaleLanguageOnly = false;
                    found = true;
                    break;
                }
            }
            if (!found)
                useLocaleLanguageOnly = true;
        }
        return useLocaleLanguageOnly;
    }

    /**
     * Locale representation depending on {@code cuba.useLocaleLanguageOnly} application property.
     *
     * @param locale locale instance
     * @return language code if {@code cuba.useLocaleLanguageOnly=true}, or full locale representation otherwise
     */
    public String localeToString(Locale locale) {
        Preconditions.checkNotNullArgument(locale);

        return useLocaleLanguageOnly() ? locale.getLanguage() : LocaleResolver.localeToString(locale);
    }

    /**
     * Trims locale to language-only if {@link #useLocaleLanguageOnly()} is true.
     * @param locale    a locale
     * @return          the locale with the same language and empty country and variant
     */
    public Locale trimLocale(Locale locale) {
        Preconditions.checkNotNullArgument(locale);

        return useLocaleLanguageOnly() ? Locale.forLanguageTag(locale.getLanguage()) : locale.stripExtensions();
    }

    /**
     * @return first locale from the list defined in {@code cuba.availableLocales} app property, taking into
     * account {@link #useLocaleLanguageOnly()} return value.
     */
    public Locale getDefaultLocale() {
        if (globalConfig.getAvailableLocales().isEmpty())
            throw new DevelopmentException("Invalid cuba.availableLocales application property");
        return globalConfig.getAvailableLocales().entrySet().iterator().next().getValue();
    }

    /**
     * @param temporalType a temporal type
     * @return default date format string for passed temporal type
     */
    public String getDefaultDateFormat(TemporalType temporalType) {
        return temporalType == TemporalType.DATE
                ? messages.getMainMessage("dateFormat")
                : messages.getMainMessage("dateTimeFormat");
    }
}