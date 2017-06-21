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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.data.Datasource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.EventObject;
import java.util.function.Supplier;

/**
 * The Image component is intended for displaying graphic content.
 *
 * It can be bound to a datasource or configured manually.
 */
public interface Image extends Component, Component.HasCaption {
    String NAME = "image";

    /**
     * @return {@link ImageResource} instance
     */
    ImageResource getSource();

    /**
     * Sets the given {@link ImageResource} to the component.
     *
     * @param resource ImageResource instance
     */
    void setSource(ImageResource resource);

    /**
     * Creates the image resource with the given <code>type</code> and sets it to the component.
     *
     * @param type image resource class to be created
     * @return created image resource instance
     */
    <T extends ImageResource> T setSource(Class<T> type);

    /**
     * Set datasource and its property.
     */
    void setDatasource(Datasource datasource, String property);

    /**
     * @return datasource instance
     */
    Datasource getDatasource();

    /**
     * @return datasource property path
     */
    MetaPropertyPath getMetaPropertyPath();

    /**
     * Creates image resource implementation by its type.
     *
     * @param type image resource type
     * @return image resource instance with given type
     */
    <T extends ImageResource> T createResource(Class<T> type);

    /**
     * @return image scale mode
     */
    ScaleMode getScaleMode();

    /**
     * Applies the given scale mode to the image.
     *
     * @param scaleMode scale mode
     */
    void setScaleMode(ScaleMode scaleMode);

    /**
     * Marker interface to indicate that the implementing class can be used as a image resource.
     */
    interface ImageResource {
    }

    /**
     * A resource which represents an image which can be loaded from the given <code>URL</code>.
     */
    interface UrlImageResource extends ImageResource {
        UrlImageResource setUrl(URL url);

        URL getUrl();
    }

    /**
     * A resource that represents an image stored in the file system as the given <code>File</code>.
     */
    interface FileImageResource extends ImageResource {
        FileImageResource setFile(File file);

        File getFile();
    }

    /**
     * A resource that represents a theme image, e.g., <code>VAADIN/themes/yourtheme/some/path/image.png</code>
     */
    interface ThemeImageResource extends ImageResource {
        ThemeImageResource setPath(String path);

        String getPath();
    }

    /**
     * A resource that represents an image, which can be obtained from the <code>FileStorage</code> using the given
     * <code>FileDescriptor</code>.
     */
    interface FileDescriptorImageResource extends ImageResource {
        FileDescriptorImageResource setFileDescriptor(FileDescriptor fileDescriptor);

        FileDescriptor getFileDescriptor();
    }

    /**
     * A resource that represents an image located in classpath with the given <code>path</code>.
     */
    interface ClasspathImageResource extends ImageResource {
        ClasspathImageResource setPath(String path);

        String getPath();
    }

    /**
     * A resource that is a streaming representation of an image.
     */
    interface StreamImageResource extends ImageResource {
        StreamImageResource setStreamSupplier(Supplier<InputStream> streamSupplier);

        Supplier<InputStream> getStreamSupplier();
    }

    /**
     * Adds a listener that will be notified when a source of an image is changed.
     */
    void addSourceChangeListener(SourceChangeListener listener);

    /**
     * Removes a listener that will be notified when a source of an image is changed.
     */
    void removeSourceChangeListener(SourceChangeListener listener);

    /**
     * Listener that will be notified when a source of an image is changed.
     */
    @FunctionalInterface
    interface SourceChangeListener {
        void sourceChanged(SourceChangeEvent event);
    }

    /**
     * SourceChangeEvent is fired when a source of an image is changed.
     */
    class SourceChangeEvent extends EventObject {
        protected ImageResource oldSource;
        protected ImageResource newSource;

        public SourceChangeEvent(Object source, ImageResource oldSource, ImageResource newSource) {
            super(source);

            this.oldSource = oldSource;
            this.newSource = newSource;
        }

        @Override
        public Image getSource() {
            return (Image) super.getSource();
        }

        public ImageResource getOldSource() {
            return oldSource;
        }

        public ImageResource getNewSource() {
            return newSource;
        }
    }

    /**
     * Defines image scale mode
     */
    enum ScaleMode {
        /**
         * The image will be stretched according to the size of the component.
         */
        FILL,
        /**
         * The image will be compressed or stretched to the minimum measurement of the component while preserving the
         * proportions.
         */
        CONTAIN,
        /**
         * The content changes size by comparing the difference between NONE and CONTAIN, in order to find the smallest
         * concrete size of the object
         */
        SCALE_DOWN,
        /**
         * The image will have a real size.
         */
        NONE
    }
}