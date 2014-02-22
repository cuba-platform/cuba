/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.annotation.LocalizedValue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Objects;

/**
 * Utility class to provide common functionality related to localized messages.
 * <p/> Implemented as Spring bean to allow extension in application projects.
 * <p/> A reference to this class can be obtained either via DI or by
 * {@link com.haulmont.cuba.core.global.Messages#getTools()} method.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(MessageTools.NAME)
public class MessageTools {

    /**
     * Prefix defining that the string is actually a key in a localized messages pack.
     */
    public static final String MARK = "msg://";

    public static final String NAME = "cuba_MessageTools";

    private Log log = LogFactory.getLog(getClass());

    protected volatile Boolean useLocaleLanguageOnly;

    @Inject
    protected Messages messages;

    @Inject
    protected Metadata metadata;

    protected GlobalConfig globalConfig;

    @Inject
    public MessageTools(Configuration configuration) {
        globalConfig = configuration.getConfig(GlobalConfig.class);
    }

    /**
     * Get localized message by reference provided in the full format.
     * @param ref   reference to message in the following format: <code>msg://message_pack/message_id</code>
     * @return      localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public String loadString(String ref) {
        return loadString(null, ref);
    }

    /**
     * Get localized message by reference provided in full or brief format.
     * @param messagesPack  messages pack to use if the second parameter is in brief format
     * @param ref           reference to message in the following format:
     * <ul>
     * <li>Full: <code>msg://message_pack/message_id</code>
     * <li>Brief: <code>msg://message_id</code>, in this case the first parameter is taken into account
     * </ul>
     * @return localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    @Nullable
    public String loadString(@Nullable String messagesPack, @Nullable String ref) {
        if (ref != null && ref.startsWith(MARK)) {
            String path = ref.substring(6);
            final String[] strings = path.split("/");
            if (strings.length == 1 && messagesPack != null) {
                ref = messages.getMessage(messagesPack, strings[0]);
            } else if (strings.length == 2) {
                ref = messages.getMessage(strings[0], strings[1]);
            } else {
                throw new UnsupportedOperationException("Unsupported resource string format: '" + ref
                        + "', messagesPack=" + messagesPack);
            }
        }
        return ref;
    }

    /**
     * @return a localized name of an entity. Messages pack must be located in the same package as entity.
     */
    public String getEntityCaption(MetaClass metaClass) {
        String className = metaClass.getJavaClass().getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return messages.getMessage(metaClass.getJavaClass(), className);
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return              localized name
     */
    public String getPropertyCaption(MetaClass metaClass, String propertyName) {
        Class<?> ownClass = metaClass.getJavaClass();
        String className = ownClass.getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        String key = className + "." + propertyName;
        String message = messages.getMessage(ownClass, key);
        if (!message.equals(key))
            return message;

        MetaPropertyPath propertyPath = metaClass.getPropertyPath(propertyName);
        if (propertyPath != null)
            return getPropertyCaption(propertyPath.getMetaProperty());
        else
            return message;
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     * @param property  MetaProperty
     * @return          localized name
     */
    public String getPropertyCaption(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null)
            return property.getName();

        String className = declaringClass.getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return messages.getMessage(declaringClass, className + "." + property.getName());
    }

    /**
     * Checks whether a localized name of the property exists.
     * @param property  MetaProperty
     * @return          true if {@link #getPropertyCaption(com.haulmont.chile.core.model.MetaProperty)} returns a
     * string which has no dots inside or the first part befor a dot is not equal to the declaring class
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
     * @return default required message for specified property.
     */
    public String getDefaultRequiredMessage(MetaProperty metaProperty) {
        return messages.formatMessage(messages.getMainMessagePack(),
                "validation.required.defaultMsg", getPropertyCaption(metaProperty));
    }

    /**
     * Get message reference of an entity property.
     * Messages pack part of the reference corresponds to the entity's package.
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return              message key in the form <code>msg://message_pack/message_id</code>
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
     * @param property  MetaProperty
     * @return          message key in the form <code>msg://message_pack/message_id</code>
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
     * @param attribute attribute name
     * @param instance  entity instance
     * @return          localized value or the value itself, if the value is null or the message pack
     * can not be inferred
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
        LocalizedValue annotation = property.getAnnotatedElement().getAnnotation(LocalizedValue.class);
        if (annotation != null) {
            if (!StringUtils.isBlank(annotation.messagePack()))
                return annotation.messagePack();
            else if (!StringUtils.isBlank(annotation.messagePackExpr())) {
                try {
                    return instance.getValueEx(annotation.messagePackExpr());
                } catch (Exception e) {
                    log.error("Unable to infer message pack from expression: " + annotation.messagePackExpr(), e);
                }
            }
        }
        return null;
    }

    /**
     * @return whether to use a full locale representation, or language only. Returns true if all locales listed
     * in <code>cuba.availableLocales</code> app property are language only.
     */
    public boolean useLocaleLanguageOnly() {
        if (useLocaleLanguageOnly == null) {
            boolean found = false;
            for (Locale locale : globalConfig.getAvailableLocales().values()) {
                if (!StringUtils.isEmpty(locale.getCountry()) || !StringUtils.isEmpty(locale.getVariant())) {
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
     * Locale representation depending on <code>cuba.useLocaleLanguageOnly</code> application property.
     * @param locale    locale instance
     * @return language code if <code>cuba.useLocaleLanguageOnly=true</code>, or full locale representation otherwise
     */
    public String localeToString(Locale locale) {
        return useLocaleLanguageOnly() ? locale.getLanguage() : locale.toString();
    }

    /**
     * Trims locale to language-only if {@link #useLocaleLanguageOnly()} is true.
     * @param locale    a locale
     * @return          the locale with the same language and empty country and variant
     */
    public Locale trimLocale(Locale locale) {
        return useLocaleLanguageOnly() ? Locale.forLanguageTag(locale.getLanguage()) : locale;
    }

    /**
     * @return first locale from the list defined in <code>cuba.availableLocales</code> app property, taking into
     * account {@link #useLocaleLanguageOnly()} return value.
     */
    public Locale getDefaultLocale() {
        if (globalConfig.getAvailableLocales().isEmpty())
            throw new DevelopmentException("Invalid cuba.availableLocales application property");
        return globalConfig.getAvailableLocales().entrySet().iterator().next().getValue();
    }
}