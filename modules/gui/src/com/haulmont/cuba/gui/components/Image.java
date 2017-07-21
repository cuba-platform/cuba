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
import com.haulmont.cuba.gui.data.Datasource;

/**
 * The Image component is intended for displaying graphic content.
 * <p>
 * It can be bound to a datasource or configured manually.
 */
public interface Image extends ResourceView {
    String NAME = "image";

    /**
     * Sets datasource and its property.
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
     * Defines image scale mode.
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
         * concrete size of the object.
         */
        SCALE_DOWN,
        /**
         * The image will have a real size.
         */
        NONE
    }
}