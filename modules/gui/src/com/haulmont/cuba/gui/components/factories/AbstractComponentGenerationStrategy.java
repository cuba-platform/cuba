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

package com.haulmont.cuba.gui.components.factories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public abstract class AbstractComponentGenerationStrategy implements ComponentGenerationStrategy {

    protected Messages messages;
    protected ComponentsFactory componentsFactory;

    public AbstractComponentGenerationStrategy(Messages messages) {
        this.messages = messages;
    }

    protected Component createComponentInternal(ComponentGenerationContext context) {
        MetaClass metaClass = context.getMetaClass();
        MetaPropertyPath mpp = resolveMetaPropertyPath(metaClass, context.getProperty());
        Element xmlDescriptor = context.getXmlDescriptor();

        if (mpp != null) {
            Range mppRange = mpp.getRange();
            if (mppRange.isDatatype()) {
                Class type = mppRange.asDatatype().getJavaClass();

                MetaProperty metaProperty = mpp.getMetaProperty();
                if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                    CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                    if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                        return createEnumField(context);
                    }
                }

                if (xmlDescriptor != null
                        && "true".equalsIgnoreCase(xmlDescriptor.attributeValue("link"))) {
                    return createDatatypeLinkField(context);
                } else {
                    boolean hasMaskAttribute = xmlDescriptor != null
                            && xmlDescriptor.attribute("mask") != null;

                    if (type.equals(String.class)) {
                        if (hasMaskAttribute) {
                            return createMaskedField(context);
                        } else {
                            return createStringField(context, mpp);
                        }
                    } else if (type.equals(UUID.class)) {
                        return createUuidField(context);
                    } else if (type.equals(Boolean.class)) {
                        return createBooleanField(context);
                    } else if (type.equals(java.sql.Date.class) || type.equals(Date.class)) {
                        return createDateField(context);
                    } else if (type.equals(Time.class)) {
                        return createTimeField(context);
                    } else if (Number.class.isAssignableFrom(type)) {
                        if (hasMaskAttribute) {
                            return createMaskedField(context);
                        } else {
                            Field currencyField = createCurrencyField(context, mpp);
                            if (currencyField != null) {
                                return currencyField;
                            }

                            return createNumberField(context);
                        }
                    }
                }
            } else if (mppRange.isClass()) {
                MetaProperty metaProperty = mpp.getMetaProperty();
                Class<?> javaType = metaProperty.getJavaType();

                if (FileDescriptor.class.isAssignableFrom(javaType)) {
                    return createFileUploadField(context);
                }

                if (!Collection.class.isAssignableFrom(javaType)) {
                    return createEntityField(context, mpp);
                }
            } else if (mppRange.isEnum()) {
                return createEnumField(context);
            }
        }

        return null;
    }

    protected Component createDatatypeLinkField(ComponentGenerationContext context) {
        EntityLinkField linkField = componentsFactory.createComponent(EntityLinkField.class);

        setDatasource(linkField, context);
        setLinkFieldAttributes(linkField, context);

        return linkField;
    }

    protected Field createEnumField(ComponentGenerationContext context) {
        LookupField component = componentsFactory.createComponent(LookupField.class);
        setDatasource(component, context);
        return component;
    }

    protected Component createMaskedField(ComponentGenerationContext context) {
        MaskedField maskedField = componentsFactory.createComponent(MaskedField.class);
        setDatasource(maskedField, context);

        Element xmlDescriptor = context.getXmlDescriptor();
        if (xmlDescriptor != null) {
            maskedField.setMask(xmlDescriptor.attributeValue("mask"));

            String valueModeStr = xmlDescriptor.attributeValue("valueMode");
            if (StringUtils.isNotEmpty(valueModeStr)) {
                maskedField.setValueMode(MaskedField.ValueMode.valueOf(valueModeStr.toUpperCase()));
            }
        }
        maskedField.setValueMode(MaskedField.ValueMode.MASKED);
        maskedField.setSendNullRepresentation(false);

        return maskedField;
    }

    protected Component createStringField(ComponentGenerationContext context, MetaPropertyPath mpp) {
        TextInputField textField = null;

        Element xmlDescriptor = context.getXmlDescriptor();
        if (xmlDescriptor != null) {
            final String rows = xmlDescriptor.attributeValue("rows");
            if (!StringUtils.isEmpty(rows)) {
                TextArea textArea = componentsFactory.createComponent(TextArea.class);
                textArea.setRows(Integer.parseInt(rows));
                textField = textArea;
            }
        }

        if (DynamicAttributesUtils.isDynamicAttribute(context.getProperty()) && mpp != null) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(mpp.getMetaProperty());
            if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.STRING
                    && categoryAttribute.getRowsCount() != null && categoryAttribute.getRowsCount() > 1) {
                TextArea textArea = componentsFactory.createComponent(TextArea.class);
                textArea.setRows(categoryAttribute.getRowsCount());
                textField = textArea;
            }
        }

        if (textField == null) {
            textField = componentsFactory.createComponent(TextField.class);
        }

        setDatasource(textField, context);

        String maxLength = xmlDescriptor != null ? xmlDescriptor.attributeValue("maxLength") : null;
        if (StringUtils.isNotEmpty(maxLength)) {
            ((TextInputField.MaxLengthLimited) textField).setMaxLength(Integer.parseInt(maxLength));
        }

        return textField;
    }

    protected Field createUuidField(ComponentGenerationContext context) {
        MaskedField maskedField = componentsFactory.createComponent(MaskedField.class);
        setDatasource(maskedField, context);
        maskedField.setMask("hhhhhhhh-hhhh-hhhh-hhhh-hhhhhhhhhhhh");
        maskedField.setSendNullRepresentation(false);
        return maskedField;
    }

    protected Field createBooleanField(ComponentGenerationContext context) {
        CheckBox component = componentsFactory.createComponent(CheckBox.class);
        setDatasource(component, context);
        return component;
    }

    protected Component createDateField(ComponentGenerationContext context) {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        setDatasource(dateField, context);

        Element xmlDescriptor = context.getXmlDescriptor();
        final String resolution = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("resolution");
        String dateFormat = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("dateFormat");

        DateField.Resolution dateResolution = DateField.Resolution.MIN;

        if (StringUtils.isNotEmpty(resolution)) {
            dateResolution = DateField.Resolution.valueOf(resolution);
            dateField.setResolution(dateResolution);
        }

        if (dateFormat == null) {
            if (dateResolution == DateField.Resolution.DAY) {
                dateFormat = "msg://dateFormat";
            } else if (dateResolution == DateField.Resolution.MIN) {
                dateFormat = "msg://dateTimeFormat";
            }
        }

        if (StringUtils.isNotEmpty(dateFormat)) {
            if (dateFormat.startsWith("msg://")) {
                dateFormat = messages.getMainMessage(dateFormat.substring(6, dateFormat.length()));
            }
            dateField.setDateFormat(dateFormat);
        }

        return dateField;
    }

    protected Component createTimeField(ComponentGenerationContext context) {
        TimeField timeField = componentsFactory.createComponent(TimeField.class);
        setDatasource(timeField, context);

        Element xmlDescriptor = context.getXmlDescriptor();
        if (xmlDescriptor != null) {
            String showSeconds = xmlDescriptor.attributeValue("showSeconds");
            if (Boolean.parseBoolean(showSeconds)) {
                timeField.setShowSeconds(true);
            }
        }

        return timeField;
    }

    protected Field createNumberField(ComponentGenerationContext context) {
        TextField component = componentsFactory.createComponent(TextField.class);
        setDatasource(component, context);
        return component;
    }

    @Nullable
    protected Field createCurrencyField(ComponentGenerationContext context, MetaPropertyPath mpp) {
        if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty()))
            return null;

        Object currencyAnnotation = mpp.getMetaProperty().getAnnotations().get(CurrencyValue.class.getName());
        if (currencyAnnotation == null) {
            return null;
        }

        CurrencyField component = componentsFactory.createComponent(CurrencyField.class);
        setDatasource(component, context);
        return component;
    }

    protected Field createFileUploadField(ComponentGenerationContext context) {
        FileUploadField fileUploadField = (FileUploadField) componentsFactory.createComponent(FileUploadField.NAME);
        fileUploadField.setMode(FileUploadField.FileStoragePutMode.IMMEDIATE);

        fileUploadField.setUploadButtonCaption(null);
        fileUploadField.setUploadButtonDescription(messages.getMainMessage("upload.submit"));
        fileUploadField.setUploadButtonIcon("icons/upload.png");

        fileUploadField.setClearButtonCaption(null);
        fileUploadField.setClearButtonDescription(messages.getMainMessage("upload.clear"));
        fileUploadField.setClearButtonIcon("icons/remove.png");

        fileUploadField.setShowFileName(true);
        fileUploadField.setShowClearButton(true);

        setDatasource(fileUploadField, context);

        return fileUploadField;
    }

    protected Component createEntityField(ComponentGenerationContext context, MetaPropertyPath mpp) {
        String linkAttribute = null;
        Element xmlDescriptor = context.getXmlDescriptor();
        if (xmlDescriptor != null) {
            linkAttribute = xmlDescriptor.attributeValue("link");
        }

        if (!Boolean.parseBoolean(linkAttribute)) {
            CollectionDatasource optionsDatasource = context.getOptionsDatasource();

            if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
                DynamicAttributesMetaProperty metaProperty = (DynamicAttributesMetaProperty) mpp.getMetaProperty();
                CategoryAttribute attribute = metaProperty.getAttribute();
                if (Boolean.TRUE.equals(attribute.getLookup())) {
                    DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
                    optionsDatasource = dynamicAttributesGuiTools
                            .createOptionsDatasourceForLookup(metaProperty.getRange().asClass(),
                                    attribute.getJoinClause(), attribute.getWhereClause());
                }
            }

            PickerField pickerField;
            if (optionsDatasource == null) {
                pickerField = componentsFactory.createComponent(PickerField.class);
                setDatasource(pickerField, context);

                if (mpp.getMetaProperty().getType() == MetaProperty.Type.ASSOCIATION) {
                    pickerField.addLookupAction();
                    if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
                        DynamicAttributesGuiTools dynamicAttributesGuiTools =
                                AppBeans.get(DynamicAttributesGuiTools.class);
                        DynamicAttributesMetaProperty dynamicAttributesMetaProperty =
                                (DynamicAttributesMetaProperty) mpp.getMetaProperty();
                        dynamicAttributesGuiTools.initEntityPickerField(pickerField,
                                dynamicAttributesMetaProperty.getAttribute());
                    }
                    boolean actionsByMetaAnnotations = ComponentsHelper.createActionsByMetaAnnotations(pickerField);
                    if (!actionsByMetaAnnotations) {
                        pickerField.addClearAction();
                    }
                } else {
                    pickerField.addOpenAction();
                    pickerField.addClearAction();
                }
            } else {
                LookupPickerField lookupPickerField = componentsFactory.createComponent(LookupPickerField.class);

                setDatasource(lookupPickerField, context);
                lookupPickerField.setOptionsDatasource(optionsDatasource);

                pickerField = lookupPickerField;

                ComponentsHelper.createActionsByMetaAnnotations(pickerField);
            }

            if (xmlDescriptor != null) {
                String captionProperty = xmlDescriptor.attributeValue("captionProperty");
                if (StringUtils.isNotEmpty(captionProperty)) {
                    pickerField.setCaptionMode(CaptionMode.PROPERTY);
                    pickerField.setCaptionProperty(captionProperty);
                }
            }

            return pickerField;
        } else {
            EntityLinkField linkField = componentsFactory.createComponent(EntityLinkField.class);

            setDatasource(linkField, context);
            setLinkFieldAttributes(linkField, context);

            return linkField;
        }
    }

    protected void setLinkFieldAttributes(EntityLinkField linkField, ComponentGenerationContext context) {
        Element xmlDescriptor = context.getXmlDescriptor();
        if (xmlDescriptor != null) {
            String linkScreen = xmlDescriptor.attributeValue("linkScreen");
            if (StringUtils.isNotEmpty(linkScreen)) {
                linkField.setScreen(linkScreen);
            }

            final String invokeMethodName = xmlDescriptor.attributeValue("linkInvoke");
            if (StringUtils.isNotEmpty(invokeMethodName)) {
                linkField.setCustomClickHandler(new InvokeEntityLinkClickHandler(invokeMethodName));
            }

            String openTypeAttribute = xmlDescriptor.attributeValue("linkScreenOpenType");
            if (StringUtils.isNotEmpty(openTypeAttribute)) {
                WindowManager.OpenType openType = WindowManager.OpenType.valueOf(openTypeAttribute);
                linkField.setScreenOpenType(openType);
            }
        }
    }

    protected MetaPropertyPath resolveMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);

        if (mpp == null && DynamicAttributesUtils.isDynamicAttribute(property)) {
            mpp = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
        }

        return mpp;
    }

    protected void setDatasource(Field field, ComponentGenerationContext context) {
        if (context.getDatasource() != null && StringUtils.isNotEmpty(context.getProperty())) {
            field.setDatasource(context.getDatasource(), context.getProperty());
        }
    }

    protected static class InvokeEntityLinkClickHandler implements EntityLinkField.EntityLinkClickHandler {
        protected final String invokeMethodName;

        public InvokeEntityLinkClickHandler(String invokeMethodName) {
            this.invokeMethodName = invokeMethodName;
        }

        @Override
        public void onClick(EntityLinkField field) {
            Window frame = ComponentsHelper.getWindow(field);
            if (frame == null) {
                throw new IllegalStateException("Please specify Frame for EntityLinkField");
            }

            Object controller = ComponentsHelper.getFrameController(frame);
            Method method;
            try {
                method = controller.getClass().getMethod(invokeMethodName, EntityLinkField.class);
                try {
                    method.invoke(controller, field);
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Can't invoke method with name '%s'",
                            invokeMethodName), e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    method = controller.getClass().getMethod(invokeMethodName);
                    try {
                        method.invoke(controller);
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format("Can't invoke method with name '%s'",
                                invokeMethodName), ex);
                    }
                } catch (NoSuchMethodException e1) {
                    throw new IllegalStateException(String.format("No suitable methods named '%s' for invoke",
                            invokeMethodName));
                }
            }
        }
    }
}
