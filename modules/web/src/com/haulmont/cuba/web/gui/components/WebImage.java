/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.web.toolkit.ui.CubaImage;
import com.vaadin.shared.util.SharedUtil;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

public class WebImage extends WebAbstractComponent<CubaImage> implements Image {
    protected static final String IMAGE_STYLENAME = "c-image";

    protected Resource value;

    protected Datasource datasource;
    protected MetaPropertyPath metaPropertyPath;

    protected ScaleMode scaleMode = ScaleMode.NONE;

    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected WeakItemChangeListener weakItemChangeListener;

    protected Runnable imageResourceUpdateHandler;

    protected static final Map<Class<? extends Resource>, Class<? extends Resource>> resourcesClasses;

    static {
        ImmutableMap.Builder<Class<? extends Resource>, Class<? extends Resource>> builder =
                new ImmutableMap.Builder<>();

        builder.put(UrlResource.class, WebUrlResource.class);
        builder.put(ClasspathResource.class, WebClasspathResource.class);
        builder.put(ThemeResource.class, WebThemeResource.class);
        builder.put(FileDescriptorResource.class, WebFileDescriptorResource.class);
        builder.put(FileResource.class, WebFileResource.class);
        builder.put(StreamResource.class, WebStreamResource.class);
        builder.put(RelativePathResource.class, WebRelativePathResource.class);

        resourcesClasses = builder.build();
    }

    public WebImage() {
        component = new CubaImage();
        component.setPrimaryStyleName(IMAGE_STYLENAME);

        imageResourceUpdateHandler = () -> {
            com.vaadin.server.Resource vRes = this.value == null ? null : ((WebAbstractResource) this.value).getResource();
            component.setSource(vRes);
        };
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null)))
            return;

        if (this.datasource != null) {
            metaPropertyPath = null;

            component.setSource(null);

            //noinspection unchecked
            this.datasource.removeItemPropertyChangeListener(weakItemPropertyChangeListener);
            weakItemPropertyChangeListener = null;

            //noinspection unchecked
            this.datasource.removeItemChangeListener(weakItemChangeListener);
            weakItemChangeListener = null;

            this.datasource = null;
        }

        if (datasource != null) {
            //noinspection unchecked
            this.datasource = datasource;

            metaPropertyPath = AppBeans.get(MetadataTools.class)
                    .resolveMetaPropertyPathNN(datasource.getMetaClass(), property);

            updateComponent();

            itemPropertyChangeListener = e -> {
                if (e.getProperty().equals(metaPropertyPath.toString())) {
                    updateComponent();
                }
            };
            weakItemPropertyChangeListener = new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener);
            //noinspection unchecked
            this.datasource.addItemPropertyChangeListener(weakItemPropertyChangeListener);

            itemChangeListener = e ->
                    updateComponent();

            weakItemChangeListener = new WeakItemChangeListener(datasource, itemChangeListener);
            //noinspection unchecked
            datasource.addItemChangeListener(weakItemChangeListener);
        }
    }

    protected void updateComponent() {
        Object propertyValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
        Resource resource = createImageResource(propertyValue);

        updateValue(resource);
    }

    protected void updateValue(Resource value) {
        Resource oldValue = this.value;
        if (oldValue != null) {
            ((WebAbstractResource) oldValue).setResourceUpdatedHandler(null);
        }

        this.value = value;

        com.vaadin.server.Resource vResource = null;
        if (value != null && ((WebAbstractResource) value).hasSource()) {
            vResource = ((WebAbstractResource) value).getResource();
        }
        component.setSource(vResource);

        if (value != null) {
            ((WebAbstractResource) value).setResourceUpdatedHandler(imageResourceUpdateHandler);
        }

        getEventRouter().fireEvent(SourceChangeListener.class, SourceChangeListener::sourceChanged,
                new SourceChangeEvent(this, oldValue, this.value));
    }

    protected Resource createImageResource(final Object resourceObject) {
        if (resourceObject == null) {
            return null;
        }

        if (resourceObject instanceof FileDescriptor) {
            FileDescriptorResource imageResource = createResource(FileDescriptorResource.class);
            imageResource.setFileDescriptor((FileDescriptor) resourceObject);
            return imageResource;
        }

        if (resourceObject instanceof byte[]) {
            StreamResource imageResource = createResource(StreamResource.class);
            Supplier<InputStream> streamSupplier = () ->
                    new ByteArrayDataProvider((byte[]) resourceObject).provide();
            imageResource.setStreamSupplier(streamSupplier);
            return imageResource;
        }

        throw new GuiDevelopmentException("The Image component supports only FileDescriptor and byte[] datasource property value binding", getFrame().getId());
    }

    @Override
    public Resource getSource() {
        return value;
    }

    @Override
    public void setSource(Resource resource) {
        if (SharedUtil.equals(this.value, resource)) {
            return;
        }
        updateValue(resource);
    }

    @Override
    public <T extends Resource> T setSource(Class<T> type) {
        T resource = createResource(type);

        updateValue(resource);

        return resource;
    }

    @Override
    public <T extends Resource> T createResource(Class<T> type) {
        Class<? extends Resource> imageResourceClass = resourcesClasses.get(type);
        if (imageResourceClass == null) {
            throw new IllegalStateException(String.format("Can't find image resource class for '%s'", type.getTypeName()));
        }

        try {
            return type.cast(imageResourceClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating the '%s' image resource instance",
                    type.getTypeName()), e);
        }
    }

    @Override
    public void addSourceChangeListener(SourceChangeListener listener) {
        getEventRouter().addListener(SourceChangeListener.class, listener);
    }

    @Override
    public void removeSourceChangeListener(SourceChangeListener listener) {
        getEventRouter().removeListener(SourceChangeListener.class, listener);
    }

    @Override
    public ScaleMode getScaleMode() {
        return this.scaleMode;
    }

    @Override
    public void setScaleMode(ScaleMode scaleMode) {
        Preconditions.checkNotNullArgument(scaleMode);

        this.scaleMode = scaleMode;

        component.setScaleMode(scaleMode.name().toLowerCase().replace("_", "-"));
    }

    @Override
    public void setAlternateText(String alternateText) {
        component.setAlternateText(alternateText);
    }

    @Override
    public String getAlternateText() {
        return component.getAlternateText();
    }
}
