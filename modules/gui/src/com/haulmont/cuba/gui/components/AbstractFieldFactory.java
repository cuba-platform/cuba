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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import javax.persistence.TemporalType;
import java.lang.reflect.Method;

import static com.haulmont.cuba.gui.WindowManager.OpenType;
import static com.haulmont.cuba.gui.components.EntityLinkField.EntityLinkClickHandler;

public abstract class AbstractFieldFactory implements FieldFactory {

    protected ComponentsFactory componentsFactory = AppConfig.getFactory();

    @Override
    public Component createField(Datasource datasource, String property, Element xmlDescriptor) {
        MetaClass metaClass = datasource.getMetaClass();
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);

        if (mpp == null && DynamicAttributesUtils.isDynamicAttribute(property)) {
            mpp = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
        }

        if (mpp != null) {
            Range mppRange = mpp.getRange();
            if (mppRange.isDatatype()) {
                Datatype datatype = mppRange.asDatatype();
                String typeName = datatype.getName();

                MetaProperty metaProperty = mpp.getMetaProperty();
                if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                    CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                    if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                        return createEnumField(datasource, property);
                    }
                }

                if (xmlDescriptor != null
                        && "true".equalsIgnoreCase(xmlDescriptor.attributeValue("link"))) {
                    return createDatatypeLinkField(datasource, property, xmlDescriptor);
                } else if (typeName.equals(StringDatatype.NAME)) {
                    if (xmlDescriptor != null
                            && xmlDescriptor.attribute("mask") != null) {
                        return createMaskedField(datasource, property, xmlDescriptor);
                    } else {
                        return createStringField(datasource, property, xmlDescriptor);
                    }
                } else if (typeName.equals(UUIDDatatype.NAME)) {
                    return createUuidField(datasource, property);
                } else if (typeName.equals(BooleanDatatype.NAME)) {
                    return createBooleanField(datasource, property);
                } else if (typeName.equals(DateDatatype.NAME) || typeName.equals(DateTimeDatatype.NAME)) {
                    return createDateField(datasource, property, mpp, xmlDescriptor);
                } else if (typeName.equals(TimeDatatype.NAME)) {
                    return createTimeField(datasource, property, xmlDescriptor);
                } else if (datatype instanceof NumberDatatype) {
                    if (xmlDescriptor != null
                            && xmlDescriptor.attribute("mask") != null) {
                        MaskedField maskedField = (MaskedField) createMaskedField(datasource, property, xmlDescriptor);
                        maskedField.setValueMode(MaskedField.ValueMode.MASKED);
                        maskedField.setSendNullRepresentation(false);
                        return maskedField;
                    } else {
                        return createNumberField(datasource, property);
                    }
                }
            } else if (mppRange.isClass()) {
                MetaProperty metaProperty = mpp.getMetaProperty();
                Class<?> javaType = metaProperty.getJavaType();
                if (FileDescriptor.class.isAssignableFrom(javaType)) {
                    return createFileUploadField(datasource, property);
                }

                return createEntityField(datasource, property, mpp, xmlDescriptor);
            } else if (mppRange.isEnum()) {
                return createEnumField(datasource, property);
            }
        }

        String exceptionMessage;
        if (mpp != null) {
            exceptionMessage = String.format("Can't create field \"%s\" with data type: %s", property, mpp.getRange().asDatatype().getName());
        } else {
            exceptionMessage = String.format("Can't create field \"%s\" with given data type", property);
        }
        throw new UnsupportedOperationException(exceptionMessage);
    }

    protected Component createFileUploadField(Datasource datasource, String property) {
        UploadField uploadField = (FileUploadField) componentsFactory.createComponent(FileUploadField.NAME);
        FileUploadField fileUploadField = ((FileUploadField) uploadField);
        fileUploadField.setMode(FileUploadField.FileStoragePutMode.IMMEDIATE);

        Messages messages = AppBeans.get(Messages.NAME);

        fileUploadField.setUploadButtonCaption(null);
        fileUploadField.setUploadButtonDescription(messages.getMainMessage("upload.submit"));
        fileUploadField.setUploadButtonIcon("icons/upload.png");

        fileUploadField.setClearButtonCaption(null);
        fileUploadField.setClearButtonIcon("icons/remove.png");
        fileUploadField.setClearButtonDescription(messages.getMainMessage("upload.clear"));

        fileUploadField.setShowFileName(true);
        fileUploadField.setShowClearButton(true);

        fileUploadField.setDatasource(datasource, property);
        return uploadField;
    }

    protected Component createDatatypeLinkField(Datasource datasource, String property, Element xmlDescriptor) {
        EntityLinkField linkField = componentsFactory.createComponent(EntityLinkField.class);

        linkField.setDatasource(datasource, property);

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
                OpenType openType = OpenType.valueOf(openTypeAttribute);
                linkField.setScreenOpenType(openType);
            }
        }

        return linkField;
    }

    protected Component createUuidField(Datasource datasource, String property) {
        MaskedField maskedField = componentsFactory.createComponent(MaskedField.class);
        maskedField.setDatasource(datasource, property);
        maskedField.setMask("hhhhhhhh-hhhh-hhhh-hhhh-hhhhhhhhhhhh");
        return maskedField;
    }

    protected Component createNumberField(Datasource datasource, String property) {
        TextField textField = componentsFactory.createComponent(TextField.class);
        textField.setDatasource(datasource, property);
        return textField;
    }

    protected Component createBooleanField(Datasource datasource, String property) {
        CheckBox checkBox = componentsFactory.createComponent(CheckBox.class);
        checkBox.setDatasource(datasource, property);
        return checkBox;
    }

    protected Component createMaskedField(Datasource datasource, String property, Element xmlDescriptor) {
        MaskedField maskedField = componentsFactory.createComponent(MaskedField.class);
        maskedField.setDatasource(datasource, property);
        if (xmlDescriptor != null) {
            maskedField.setMask(xmlDescriptor.attributeValue("mask"));

            String valueModeStr = xmlDescriptor.attributeValue("valueMode", MaskedField.ValueMode.CLEAR.getId());
            maskedField.setValueMode(MaskedField.ValueMode.fromId(valueModeStr));
        }
        return maskedField;
    }

    protected Component createStringField(Datasource datasource, String property, Element xmlDescriptor) {
        TextInputField textField = null;

        if (xmlDescriptor != null) {
            final String rows = xmlDescriptor.attributeValue("rows");
            if (!StringUtils.isEmpty(rows)) {
                TextArea textArea = componentsFactory.createComponent(TextArea.class);
                textArea.setRows(Integer.parseInt(rows));
                textField = textArea;
            }
        }
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            MetaClass metaClass = datasource instanceof RuntimePropsDatasource ?
                    ((RuntimePropsDatasource) datasource).resolveCategorizedEntityClass() : datasource.getMetaClass();
            MetaPropertyPath mpp = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
            if (mpp != null) {
                CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(mpp.getMetaProperty());
                if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.STRING
                        && categoryAttribute.getRowsCount() != null && categoryAttribute.getRowsCount() > 1) {
                    TextArea textArea = componentsFactory.createComponent(TextArea.class);
                    textArea.setRows(categoryAttribute.getRowsCount());
                    textField = textArea;
                }
            }
        }

        if (textField == null) {
            textField = componentsFactory.createComponent(TextField.class);
        }

        textField.setDatasource(datasource, property);
        MetaProperty metaProperty = textField.getMetaProperty();

        final String maxLength = xmlDescriptor != null ? xmlDescriptor.attributeValue("maxLength") : null;
        if (!StringUtils.isEmpty(maxLength)) {
            ((TextInputField.MaxLengthLimited) textField).setMaxLength(Integer.parseInt(maxLength));
        } else {
            Integer len = (Integer) metaProperty.getAnnotations().get("length");
            if (len != null) {
                ((TextInputField.MaxLengthLimited) textField).setMaxLength(len);
            }
        }

        return textField;
    }

    protected Component createDateField(Datasource datasource, String property, MetaPropertyPath mpp,
                                        Element xmlDescriptor) {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.setDatasource(datasource, property);

        MetaProperty metaProperty = mpp.getMetaProperty();
        TemporalType tt = null;
        if (metaProperty != null) {
            if (metaProperty.getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME))) {
                tt = TemporalType.DATE;
            } else if (metaProperty.getAnnotations() != null) {
                tt = (TemporalType) metaProperty.getAnnotations().get("temporal");
            }
        }

        final String resolution = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("resolution");
        String dateFormat = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("dateFormat");

        DateField.Resolution dateResolution = DateField.Resolution.MIN;

        if (!StringUtils.isEmpty(resolution)) {
            dateResolution = DateField.Resolution.valueOf(resolution);
            dateField.setResolution(dateResolution);
        } else if (tt == TemporalType.DATE) {
            dateField.setResolution(DateField.Resolution.DAY);
        }

        if (dateFormat == null) {
            if (dateResolution == DateField.Resolution.DAY) {
                dateFormat = "msg://dateFormat";
            } else if (dateResolution == DateField.Resolution.MIN) {
                dateFormat = "msg://dateTimeFormat";
            }
        }
        Messages messages = AppBeans.get(Messages.NAME);

        if (!StringUtils.isEmpty(dateFormat)) {
            if (dateFormat.startsWith("msg://")) {
                dateFormat = messages.getMainMessage(dateFormat.substring(6, dateFormat.length()));
            }
            dateField.setDateFormat(dateFormat);
        } else {
            String formatStr;
            if (tt == TemporalType.DATE) {
                formatStr = messages.getMainMessage("dateFormat");
            } else {
                formatStr = messages.getMainMessage("dateTimeFormat");
            }
            dateField.setDateFormat(formatStr);
        }

        return dateField;
    }

    protected Component createTimeField(Datasource datasource, String property, Element xmlDescriptor) {
        TimeField timeField = componentsFactory.createComponent(TimeField.class);
        timeField.setDatasource(datasource, property);

        if (xmlDescriptor != null) {
            String showSeconds = xmlDescriptor.attributeValue("showSeconds");
            if (Boolean.parseBoolean(showSeconds)) {
                timeField.setShowSeconds(true);
            }
        }
        return timeField;
    }

    protected Component createEntityField(Datasource datasource, String property, MetaPropertyPath mpp, Element xmlDescriptor) {
        String linkAttribute = null;
        if (xmlDescriptor != null) {
            linkAttribute = xmlDescriptor.attributeValue("link");
        }

        if (!Boolean.parseBoolean(linkAttribute)) {
            CollectionDatasource optionsDatasource = getOptionsDatasource(datasource, property);

            if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
                DynamicAttributesMetaProperty metaProperty = (DynamicAttributesMetaProperty) mpp.getMetaProperty();
                CategoryAttribute attribute = metaProperty.getAttribute();
                if (Boolean.TRUE.equals(attribute.getLookup())) {
                    optionsDatasource = new DsBuilder(datasource.getDsContext())
                            .setMetaClass(metaProperty.getRange().asClass())
                            .setViewName(View.MINIMAL)
                            .buildCollectionDatasource();
                    optionsDatasource.refresh();
                }
            }

            PickerField pickerField;
            if (optionsDatasource == null) {
                pickerField = componentsFactory.createComponent(PickerField.class);
                PickerField.LookupAction lookupAction = pickerField.addLookupAction();
                if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
                    DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
                    dynamicAttributesGuiTools.initEntityLookupAction(lookupAction, (DynamicAttributesMetaProperty) mpp.getMetaProperty());
                }
                pickerField.addClearAction();
            } else {
                LookupPickerField lookupPickerField = componentsFactory.createComponent(LookupPickerField.class);
                lookupPickerField.setOptionsDatasource(optionsDatasource);

                pickerField = lookupPickerField;

                pickerField.removeAction(PickerField.LookupAction.NAME);
            }

            if (xmlDescriptor != null) {
                String captionProperty = xmlDescriptor.attributeValue("captionProperty");
                if (StringUtils.isNotEmpty(captionProperty)) {
                    pickerField.setCaptionMode(CaptionMode.PROPERTY);
                    pickerField.setCaptionProperty(captionProperty);
                }
            }

            pickerField.setDatasource(datasource, property);

            return pickerField;
        } else {
            EntityLinkField linkField = componentsFactory.createComponent(EntityLinkField.class);

            linkField.setDatasource(datasource, property);

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
                    OpenType openType = OpenType.valueOf(openTypeAttribute);
                    linkField.setScreenOpenType(openType);
                }
            }

            return linkField;
        }
    }

    protected Component createEnumField(Datasource datasource, String property) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(datasource, property);

        return lookupField;
    }

    @Nullable
    protected abstract CollectionDatasource getOptionsDatasource(Datasource datasource, String property);

    protected static class InvokeEntityLinkClickHandler implements EntityLinkClickHandler {
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
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    method = controller.getClass().getMethod(invokeMethodName);
                    try {
                        method.invoke(controller);
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                } catch (NoSuchMethodException e1) {
                    throw new IllegalStateException("No suitable methods named " + invokeMethodName + " for invoke");
                }
            }
        }
    }
}