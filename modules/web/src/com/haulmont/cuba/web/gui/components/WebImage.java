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
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.web.gui.components.imageresources.*;
import com.haulmont.cuba.web.toolkit.ui.CubaImage;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.util.SharedUtil;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

public class WebImage extends WebAbstractComponent<CubaImage> implements Image {
    protected static final String IMAGE_STYLENAME = "c-image";

    protected ImageResource value;

    protected Datasource datasource;
    protected MetaPropertyPath metaPropertyPath;

    protected ScaleMode scaleMode = ScaleMode.NONE;

    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected WeakItemChangeListener weakItemChangeListener;

    protected Runnable imageResourceUpdateHandler;

    protected static final Map<Class<? extends ImageResource>, Class<? extends ImageResource>> resourcesClasses;

    static {
        ImmutableMap.Builder<Class<? extends ImageResource>, Class<? extends ImageResource>> builder =
                new ImmutableMap.Builder<>();

        builder.put(UrlImageResource.class, WebUrlImageResource.class);
        builder.put(ClasspathImageResource.class, WebClasspathImageResource.class);
        builder.put(ThemeImageResource.class, WebThemeImageResource.class);
        builder.put(FileDescriptorImageResource.class, WebFileDescriptorImageResource.class);
        builder.put(FileImageResource.class, WebFileImageResource.class);
        builder.put(StreamImageResource.class, WebStreamImageResource.class);
        builder.put(RelativePathImageResource.class, WebRelativePathImageResource.class);

        resourcesClasses = builder.build();
    }

    public WebImage() {
        component = new CubaImage();
        component.setPrimaryStyleName(IMAGE_STYLENAME);

        imageResourceUpdateHandler = () -> {
            Resource vRes = this.value == null ? null : ((WebAbstractImageResource) this.value).getResource();
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
        ImageResource resource = createImageResource(propertyValue);

        updateValue(resource);
    }

    protected ImageResource createImageResource(final Object resourceObject) {
        if (resourceObject == null) {
            return null;
        }

        if (resourceObject instanceof FileDescriptor) {
            FileDescriptorImageResource imageResource = createResource(FileDescriptorImageResource.class);
            imageResource.setFileDescriptor((FileDescriptor) resourceObject);
            return imageResource;
        }

        if (resourceObject instanceof byte[]) {
            StreamImageResource imageResource = createResource(StreamImageResource.class);
            Supplier<InputStream> streamSupplier = () ->
                    new ByteArrayDataProvider((byte[]) resourceObject).provide();
            imageResource.setStreamSupplier(streamSupplier);
            return imageResource;
        }

        throw new GuiDevelopmentException("The Image component supports only FileDescriptor and byte[] datasource property value binding", getFrame().getId());
    }

    @Override
    public ImageResource getSource() {
        return value;
    }

    @Override
    public void setSource(ImageResource resource) {
        if (SharedUtil.equals(this.value, resource)) {
            return;
        }
        updateValue(resource);
    }

    @Override
    public <T extends ImageResource> T setSource(Class<T> type) {
        T resource = createResource(type);

        updateValue(resource);

        return resource;
    }

    protected void updateValue(ImageResource value) {
        ImageResource oldValue = this.value;
        if (oldValue != null) {
            ((WebAbstractImageResource) oldValue).setResourceUpdatedHandler(null);
        }

        this.value = value;

        Resource vResource = null;
        if (value != null && ((WebAbstractImageResource) value).hasSource()) {
            vResource = ((WebAbstractImageResource) value).getResource();
        }
        component.setSource(vResource);

        if (value != null) {
            ((WebAbstractImageResource) value).setResourceUpdatedHandler(imageResourceUpdateHandler);
        }

        getEventRouter().fireEvent(SourceChangeListener.class, SourceChangeListener::sourceChanged,
                new SourceChangeEvent(this, oldValue, this.value));
    }

    @Override
    public <T extends ImageResource> T createResource(Class<T> type) {
        Class<? extends Image.ImageResource> imageResourceClass = resourcesClasses.get(type);
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

    public abstract static class WebAbstractImageResource implements WebImageResource {
        protected Resource resource;
        protected Runnable resourceUpdateHandler;

        protected boolean hasSource = false;

        @Override
        public Resource getResource() {
            if (resource == null) {
                createResource();
            }
            return resource;
        }

        protected boolean hasSource() {
            return hasSource;
        }

        protected void fireResourceUpdateEvent() {
            resource = null;

            if (resourceUpdateHandler != null) {
                resourceUpdateHandler.run();
            }
        }

        protected void setResourceUpdatedHandler(Runnable resourceUpdated) {
            this.resourceUpdateHandler = resourceUpdated;
        }

        protected abstract void createResource();
    }

    public abstract static class WebAbstractStreamSettingsImageResource extends WebAbstractImageResource implements HasStreamSettings {
        protected long cacheTime = DownloadStream.DEFAULT_CACHETIME;
        protected int bufferSize;
        protected String fileName;

        @Override
        public void setCacheTime(long cacheTime) {
            this.cacheTime = cacheTime;

            if (resource != null) {
                ((StreamResource) resource).setCacheTime(cacheTime);
            }
        }

        @Override
        public long getCacheTime() {
            return cacheTime;
        }

        @Override
        public void setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;

            if (resource != null) {
                ((StreamResource) resource).setBufferSize(bufferSize);
            }
        }

        @Override
        public int getBufferSize() {
            return bufferSize;
        }

        @Override
        public void setFileName(String fileName) {
            this.fileName = fileName;

            if (resource != null) {
                ((StreamResource) resource).setFilename(fileName);
            }
        }

        @Override
        public String getFileName() {
            return fileName;
        }
    }
}
