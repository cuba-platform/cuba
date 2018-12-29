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

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.sys.BeanLocatorAware;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.FileDescriptorResource;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.Resource;
import com.haulmont.cuba.gui.components.StreamResource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.web.widgets.CubaImage;
import com.vaadin.event.MouseEvents;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WebImage extends WebAbstractResourceView<CubaImage> implements Image, InitializingBean {
    protected static final String IMAGE_STYLENAME = "c-image";

    protected ValueSource<FileDescriptor> valueSource;

    protected Subscription valueChangeSubscription;
    protected Subscription instanceChangeSubscription;

    protected MetaPropertyPath metaPropertyPath;

    protected ScaleMode scaleMode = ScaleMode.NONE;

    protected MouseEvents.ClickListener vClickListener;

    public WebImage() {
        component = createComponent();
    }

    protected CubaImage createComponent() {
        return new CubaImage();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent(component);
    }

    protected void initComponent(CubaImage component) {
        component.setPrimaryStyleName(IMAGE_STYLENAME);
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public void setValueSource(ValueSource<FileDescriptor> valueSource) {
        if (this.valueSource == valueSource) {
            return;
        }

        unbindValueSourceEvents();

        if (this.valueSource != null && valueSource == null) {
            component.setSource(null);
            this.valueSource = null;
            return;
        }

        this.valueSource = valueSource;

        bindValueSourceEvents();
        updateComponent();
    }

    protected void unbindValueSourceEvents() {
        if (valueChangeSubscription != null) {
            valueChangeSubscription.remove();
        }
        if (instanceChangeSubscription != null) {
            instanceChangeSubscription.remove();
        }
    }

    protected void bindValueSourceEvents() {
        if (valueSource == null) {
            return;
        }

        if (valueSource instanceof BeanLocatorAware) {
            ((BeanLocatorAware) valueSource).setBeanLocator(beanLocator);
        }

        valueChangeSubscription = valueSource.addValueChangeListener(event -> updateComponent());
        if (valueSource instanceof EntityValueSource) {
            instanceChangeSubscription = ((EntityValueSource<Entity, FileDescriptor>) valueSource)
                    .addInstanceChangeListener(event -> updateComponent());
        }
    }

    @Override
    public ValueSource<FileDescriptor> getValueSource() {
        return valueSource;
    }

    protected void updateComponent() {
        Object propertyValue = valueSource.getValue();
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
    public Subscription addClickListener(Consumer<ClickEvent> listener) {
        if (vClickListener == null) {
            vClickListener = e -> {
                ClickEvent event = new ClickEvent(WebImage.this, WebWrapperUtils.toMouseEventDetails(e));
                publish(ClickEvent.class, event);
            };
            component.addClickListener(vClickListener);
        }

        getEventHub().subscribe(ClickEvent.class, listener);

        return () -> removeClickListener(listener);
    }

    @Override
    public void removeClickListener(Consumer<ClickEvent> listener) {
        unsubscribe(ClickEvent.class, listener);

        if (!hasSubscriptions(ClickEvent.class)) {
            component.removeClickListener(vClickListener);
            vClickListener = null;
        }
    }
}