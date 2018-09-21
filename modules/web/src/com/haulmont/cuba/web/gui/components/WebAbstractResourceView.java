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
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.*;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.AbstractEmbedded;

import java.util.Map;
import java.util.function.Consumer;

public abstract class WebAbstractResourceView<T extends AbstractEmbedded> extends WebAbstractComponent<T>
        implements ResourceView {

    protected Resource resource;

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

    protected Runnable resourceUpdateHandler;

    protected WebAbstractResourceView() {
        resourceUpdateHandler = () -> {
            com.vaadin.server.Resource vRes = this.resource == null ? null : ((WebAbstractResource) this.resource).getResource();
            component.setSource(vRes);
        };
    }

    @Override
    public Resource getSource() {
        return resource;
    }

    @Override
    public void setSource(Resource resource) {
        if (SharedUtil.equals(this.resource, resource)) {
            return;
        }
        updateValue(resource);
    }

    protected void updateValue(Resource value) {
        Resource oldValue = this.resource;
        if (oldValue != null) {
            ((WebAbstractResource) oldValue).setResourceUpdatedHandler(null);
        }

        this.resource = value;

        com.vaadin.server.Resource vResource = null;
        if (value != null && ((WebAbstractResource) value).hasSource()) {
            vResource = ((WebAbstractResource) value).getResource();
        }
        component.setSource(vResource);

        if (value != null) {
            ((WebAbstractResource) value).setResourceUpdatedHandler(resourceUpdateHandler);
        }

        publish(SourceChangeEvent.class, new SourceChangeEvent(this, oldValue, this.resource));
    }

    @Override
    public <R extends Resource> R setSource(Class<R> type) {
        R resource = createResource(type);

        updateValue(resource);

        return resource;
    }

    @Override
    public <R extends Resource> R createResource(Class<R> type) {
        Class<? extends Resource> resourceClass = resourcesClasses.get(type);
        if (resourceClass == null) {
            throw new IllegalStateException(String.format("Can't find resource class for '%s'", type.getTypeName()));
        }

        try {
            return type.cast(resourceClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating the '%s' resource instance",
                    type.getTypeName()), e);
        }
    }

    @Override
    public void setAlternateText(String alternateText) {
        component.setAlternateText(alternateText);
    }

    @Override
    public String getAlternateText() {
        return component.getAlternateText();
    }

    @Override
    public Subscription addSourceChangeListener(Consumer<SourceChangeEvent> listener) {
        return getEventHub().subscribe(SourceChangeEvent.class, listener);
    }

    @Override
    public void removeSourceChangeListener(Consumer<SourceChangeEvent> listener) {
        unsubscribe(SourceChangeEvent.class, listener);
    }
}