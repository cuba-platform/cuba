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

import com.haulmont.bali.events.Subscription;

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * A class that implements this interface is intended for viewing different resources, e.g.
 * {@link UrlResource}, {@link FileResource}, etc.
 */
public interface ResourceView extends Component, Component.HasCaption, HasContextHelp,
        HasHtmlCaption, HasHtmlDescription {

    /**
     * @return {@link Resource} instance
     */
    Resource getSource();

    /**
     * Sets the given {@link Resource} to the component.
     *
     * @param resource Resource instance
     */
    void setSource(Resource resource);

    /**
     * Creates the resource with the given <code>type</code> and sets it to the component.
     *
     * @param type resource class to be created
     * @param <R>  {@link Resource} inheritor
     *
     * @return new resource instance
     */
    <R extends Resource> R setSource(Class<R> type);

    /**
     * Creates resource implementation by its type.
     *
     * @param type resource class to be created
     * @param <R> {@link Resource} inheritor
     *
     * @return new resource instance with given type
     */
    <R extends Resource> R createResource(Class<R> type);

    /**
     * Sets this component's alternate text that can be presented instead of the component's normal content for
     * accessibility purposes.
     *
     * @param alternateText a short, human-readable description of this component's content
     */
    void setAlternateText(String alternateText);

    /**
     * Gets this component's alternate text that can be presented instead of the component's normal content for
     * accessibility purposes.
     *
     * @return alternate text
     */
    String getAlternateText();

    /**
     * Adds a listener that will be notified when a source is changed.
     */
    Subscription addSourceChangeListener(Consumer<SourceChangeEvent> listener);

    /**
     * Removes a listener that will be notified when a source is changed.
     *
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeSourceChangeListener(Consumer<SourceChangeEvent> listener);

    /**
     * SourceChangeEvent is fired when a source is changed.
     */
    class SourceChangeEvent extends EventObject {
        protected Resource oldSource;
        protected Resource newSource;

        public SourceChangeEvent(ResourceView source, Resource oldSource, Resource newSource) {
            super(source);

            this.oldSource = oldSource;
            this.newSource = newSource;
        }

        @Override
        public ResourceView getSource() {
            return (ResourceView) super.getSource();
        }

        public Resource getOldSource() {
            return oldSource;
        }

        public Resource getNewSource() {
            return newSource;
        }
    }

    /**
     * Marker interface to indicate that the implementing class supports MIME type setting.
     */
    interface HasMimeType {
        /**
         * Sets the mime type of the resource.
         *
         * @param mimeType the MIME type to be set
         */
        void setMimeType(String mimeType);

        /**
         * @return resource MIME type
         */
        String getMimeType();
    }

    /**
     * Marker interface to indicate that the implementing class has stream settings (such as cache time, buffer size
     * or file name).
     */
    interface HasStreamSettings {
        /**
         * Sets the length of cache expiration time.
         * <p>
         * This gives the adapter the possibility cache streams sent to the client. The caching may be made in adapter
         * or at the client if the client supports caching. Zero or negative value disables the caching of this stream.
         * </p>
         *
         * @param cacheTime the cache time in milliseconds
         */
        void setCacheTime(long cacheTime);

        /**
         * @return resource cache time
         */
        long getCacheTime();

        /**
         * Sets the size of the download buffer used for this resource.
         *
         * @param bufferSize the size of the buffer in bytes
         */
        void setBufferSize(int bufferSize);

        /**
         * @return buffer size
         */
        int getBufferSize();

        /**
         * Sets the filename.
         *
         * @param fileName the filename to set
         */
        void setFileName(String fileName);

        /**
         * @return resource file name
         */
        String getFileName();
    }
}
