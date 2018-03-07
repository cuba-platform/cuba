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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.FileDescriptorResource;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.Resource;
import com.haulmont.cuba.gui.components.StreamResource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.web.widgets.CubaImage;
import com.vaadin.event.MouseEvents;

import java.io.InputStream;
import java.util.function.Supplier;

public class WebImage extends WebAbstractResourceView<CubaImage> implements Image {
    protected static final String IMAGE_STYLENAME = "c-image";

    protected Datasource datasource;
    protected MetaPropertyPath metaPropertyPath;

    protected ScaleMode scaleMode = ScaleMode.NONE;

    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected WeakItemChangeListener weakItemChangeListener;

    protected MouseEvents.ClickListener vClickListener;

    public WebImage() {
        component = new CubaImage();
        component.setPrimaryStyleName(IMAGE_STYLENAME);
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
        if ((datasource == null && property != null) || (datasource != null && property == null)) {
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");
        }

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null))) {
            return;
        }

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

        throw new GuiDevelopmentException(
                "The Image component supports only FileDescriptor and byte[] datasource property value binding",
                getFrame().getId());
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
    public void addClickListener(ClickListener listener) {
        getEventRouter().addListener(ClickListener.class, listener);

        if (vClickListener == null) {
            vClickListener = e -> {
                ClickEvent event = new ClickEvent(WebImage.this, WebWrapperUtils.toMouseEventDetails(e));
                getEventRouter().fireEvent(ClickListener.class, ClickListener::onClick, event);
            };
            component.addClickListener(vClickListener);
        }
    }

    @Override
    public void removeClickListener(ClickListener listener) {
        getEventRouter().removeListener(ClickListener.class, listener);

        if (!getEventRouter().hasListeners(ClickListener.class)) {
            component.removeClickListener(vClickListener);
            vClickListener = null;
        }
    }
}